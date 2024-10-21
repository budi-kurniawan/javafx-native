package com.brainysoftware.pdfeditor.ui.listener;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import com.brainysoftware.pdfeditor.LiveValues;
import com.brainysoftware.pdfeditor.PDFHandler;
import com.brainysoftware.pdfeditor.PropertyManager;
import com.brainysoftware.pdfeditor.ui.GUIManager;
import com.brainysoftware.pdfeditor.ui.component.ThumbnailView;
import com.brainysoftware.pdfeditor.ui.event.DocumentOpenedEvent;
import com.brainysoftware.pdfeditor.ui.event.EventManager;
import com.brainysoftware.pdfeditor.ui.event.PageSelectedEvent;

import javafx.scene.layout.VBox;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class DocumentOpenedListener implements EventListener {
	
	/**
	 * This listener performs various tasks. Separate it into multiple listeners
		if it becomes more complex.
	 * @param event
	 */
	public void handleOpenedDocument(DocumentOpenedEvent event) {
		System.out.println("handleOpenedDocument in DocumentOpenedListener");
		GUIManager guiManager = GUIManager.getInstance();
		// 1. remember the opened file
		Path path = event.getPath();
		if (!Files.exists(path)) {
		    guiManager.showErrorDialog("File not found", "\"" + path.toString() 
		            + " does not exist and will be removed from Recent Documents\"");
		    PropertyManager.getInstance().removeFromRecentDocuments(path);
		    guiManager.updateRecentDocumentsMenu();
		    return;
		}
		guiManager.setWindowTitle(path.toString());
		LiveValues.openedDocument = path;

		// 2. by default do not allow overriding the file.
		LiveValues.allowOverrideFile = false;
		
		// 3. Create thumbnails and display the page.
		PDFHandler pdfHandler = PDFHandler.getInstance();
		pdfHandler.openDocument(path);
		VBox thumbnailContainer = guiManager.getThumbnailContainer();
		thumbnailContainer.getChildren().clear();
    	EventManager.getInstance().firePageSelectedEvent(new PageSelectedEvent(this, /*pageNo=*/ 0));
		guiManager.setVisibleMenuById("documentMenu", true);
		
		// Add the opened document as the first recent document
		PropertyManager.getInstance().setAsFirstRecentDocument(path);
		
		// populate thumbnails
		List<byte[]> thumbnails = pdfHandler.getThumbnails();
		int count = 0;
		for (byte[] thumbnail : thumbnails) {
			ThumbnailView view = new ThumbnailView(thumbnail, count);
			if (count == 0) {
				GUIManager.selectedThumbnailView = view;
			}
		    view.setOnMouseClicked(e -> 
		    {
            	if (GUIManager.selectedThumbnailView != null) {
            		GUIManager.selectedThumbnailView.setBorderVisible(false);
            	}
            	view.setBorderVisible(true);
            	GUIManager.selectedThumbnailView = view;
            	GUIManager.currentPageNo = view.getPageNo();
            	EventManager.getInstance().firePageSelectedEvent(new PageSelectedEvent(view, view.getPageNo()));
            });
		    thumbnailContainer.getChildren().add(view);
		    count++;		   
		}
		if (GUIManager.selectedThumbnailView != null) {
	        GUIManager.selectedThumbnailView.setBorderVisible(true);
		}
		
		// Enable Save As menu item
		guiManager.setDisableMenuItemById(GUIManager.SAVE_AS_DOCUMENT_MENU_ITEM, false);
	}
}