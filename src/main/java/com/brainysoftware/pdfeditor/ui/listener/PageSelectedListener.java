package com.brainysoftware.pdfeditor.ui.listener;

import java.io.ByteArrayInputStream;
import java.util.EventListener;
import java.util.List;

import com.brainysoftware.pdfeditor.PDFHandler;
import com.brainysoftware.pdfeditor.ui.ComponentManager;
import com.brainysoftware.pdfeditor.ui.GUIManager;
import com.brainysoftware.pdfeditor.ui.component.ImageBox;
import com.brainysoftware.pdfeditor.ui.component.ImageStackPane;
import com.brainysoftware.pdfeditor.ui.event.PageSelectedEvent;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class PageSelectedListener implements EventListener {
    public void handlePageSelected(PageSelectedEvent event) {
		PDFHandler handler = PDFHandler.getInstance();
		ImageStackPane imageStackPane = GUIManager.getInstance().getImageStackPane();
		imageStackPane.getChildren().clear();
		Image image = new Image(
				new ByteArrayInputStream(handler.getPageAsPng(event.getPageNo())));
		GUIManager.currentPDFPageWidth = image.getWidth();
		GUIManager.currentPDFPageHeight = image.getHeight();
		ImageView imageView = new ImageView(image);
		
		// need to call imageStackPane.resize() because its automatic resizing is diabled.
		imageStackPane.resize(image.getWidth(), image.getHeight());
		StackPane.setAlignment(imageView, Pos.TOP_CENTER);
		
		// rotate
//		Rotate rotate = new Rotate();  
//	    rotate.setAngle(180);
//	    rotate.setPivotX(GUIUtil.currentPDFPageWidth / 2);  
//	    rotate.setPivotY(GUIUtil.currentPDFPageHeight / 2);
//	    imageView.getTransforms().add(rotate);
	    // end of rotate
		
		imageStackPane.getChildren().add(imageView);	
		List<ImageBox> imageBoxes = ComponentManager.getInstance().getImageBoxes(event.getPageNo());
		imageBoxes.forEach(ib -> imageStackPane.getChildren().add(ib));
    }
}