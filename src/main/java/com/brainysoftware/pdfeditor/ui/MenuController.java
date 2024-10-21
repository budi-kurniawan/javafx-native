package com.brainysoftware.pdfeditor.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.brainysoftware.pdfeditor.Constants;
import com.brainysoftware.pdfeditor.LiveValues;
import com.brainysoftware.pdfeditor.PDFHandler;
import com.brainysoftware.pdfeditor.PropertyManager;
import com.brainysoftware.pdfeditor.ui.component.ImageBox;
import com.brainysoftware.pdfeditor.ui.event.DocumentOpenedEvent;
import com.brainysoftware.pdfeditor.ui.event.EventManager;
import com.brainysoftware.pdfeditor.ui.event.ImageAddedEvent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 * @author Budi Kurniawan (https://brainysoftware.com)
 * 
 */

public class MenuController implements Initializable {

	@FXML
	private MenuBar menuBar;

	@FXML
	private void handleAboutAction(final ActionEvent event) {
		GUIManager.getInstance().showMessageDialog("PDFEditor 0.2", "Author: Budi Kurniawan (https://brainysoftware.com)");
	}

	@FXML
	private void handleOpenDocument(final ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open PDF Document");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.pdf"));

		String lastOpenedDir = PropertyManager.getInstance().getLastOpenDir();
		if (lastOpenedDir == null) {
		    lastOpenedDir = System.getProperty("user.home");
		}
        fileChooser.setInitialDirectory(new File(lastOpenedDir));

		// TODO need better way to pass Stage to showOpenDialog()
		File selectedFile = fileChooser.showOpenDialog(GUIManager.currentStage);
		if (selectedFile != null) {
		    PropertyManager.getInstance().setLastOpenDir(selectedFile.getParent().toString());
			EventManager.getInstance().fireDocumentOpenedEvent(new DocumentOpenedEvent(selectedFile.toPath()));
		}
	}

	@FXML
	private void handleSaveDocument(final ActionEvent event) {
		if (!LiveValues.directSave) {
		    return; // not permitted
		}
		ComponentManager cm = ComponentManager.getInstance();
		PDFHandler handler = PDFHandler.getInstance();
		PDDocument doc = handler.clone();
		int numPages = handler.getNumPages();
		for (int i = 0; i < numPages; i++) {
			final int pageNo = i;
			List<ImageBox> imageBoxes = cm.getImageBoxes(i);
			imageBoxes.forEach(imageBox -> {
		    	Insets margin = StackPane.getMargin(imageBox);
		    	float insertionX = (float) (margin.getLeft() / GUIManager.currentPDFPageWidth);
		    	float imageHeight = (float) imageBox.getImageFitHeight();
		    	float insertionY = (float) ((GUIManager.currentPDFPageHeight - margin.getTop() - imageHeight) / GUIManager.currentPDFPageHeight);
		    	try {
		    		handler.addImageToDocument(doc, imageBox.getImagePath(), pageNo, insertionX, insertionY,
		    		        (float) imageBox.getImageFitWidth(), (float) imageBox.getImageFitHeight());
				} catch (IOException e) {
					e.printStackTrace();
				}				
			});
		}
		handler.save(doc, LiveValues.savedDocument.toString());
		LiveValues.allowOverrideFile = true;
		// Enable saveDocumentMenuItem
		GUIManager.getInstance().setDisableMenuItemById(GUIManager.SAVE_DOCUMENT_MENU_ITEM, false);
        PropertyManager.getInstance().setAsFirstRecentDocument(LiveValues.openedDocument);
	}
	
    @FXML
    private void handleSaveAsDocument(final ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Document");          
        // first let save in the folder in which the opened document was located
        Path saveDir = LiveValues.savedDocument != null ? 
                LiveValues.savedDocument.getParent() : LiveValues.openedDocument.getParent();
        fileChooser.setInitialDirectory(saveDir.toFile());
        fileChooser.setInitialFileName(LiveValues.openedDocument.getFileName().toString());
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF File", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(GUIManager.currentStage);
        if (selectedFile == null) {
            return;
        } else {
            LiveValues.savedDocument = selectedFile.toPath();
            LiveValues.directSave = true;
            LiveValues.openedDocument = LiveValues.savedDocument;
            GUIManager.getInstance().setWindowTitle(LiveValues.openedDocument.toString());
            handleSaveDocument(event);
        }       
    }
	
	@FXML
	private void handleInsertImage(final ActionEvent event) {
		String lastOpenedDir = PropertyManager.getInstance().getProperty(Constants.PROPERTY_LAST_OPENED_DIR);
		System.out.println("read lastOpenedDir:" + lastOpenedDir);
		FileChooser fileChooser = new FileChooser();
		if (lastOpenedDir != null) {
			fileChooser.setInitialDirectory(new File(lastOpenedDir));
		}
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.bmp", "*.gif", "*.jpeg", "*.jpg", "*.png"));
		// TODO need better way to pass Stage to showOpenDialog()
		File selectedFile = fileChooser.showOpenDialog(GUIManager.currentStage);
		if (selectedFile != null) {
			String parentDir = selectedFile.getParent().toString();
			System.out.println("Write lastOpenedDir to property: " + parentDir);
			PropertyManager.getInstance().setLastOpenDir(parentDir);
			EventManager.getInstance().fireImageAddedEvent(new ImageAddedEvent(
					selectedFile, GUIManager.currentPageNo));
		}
	}

	@FXML
	private void handleExit(final ActionEvent event) {
		System.exit(0);
	}

	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
	    GUIManager.Builder.build(menuBar);
		menuBar.setFocusTraversable(true);
		System.out.println("Initialize MenuController...");
	}
}