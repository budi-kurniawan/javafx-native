package com.brainysoftware.pdfeditor.ui.listener;

import java.util.EventListener;

import com.brainysoftware.pdfeditor.ui.ComponentManager;
import com.brainysoftware.pdfeditor.ui.GUIManager;
import com.brainysoftware.pdfeditor.ui.component.ImageBox;
import com.brainysoftware.pdfeditor.ui.event.ImageAddedEvent;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class ImageAddedListener implements EventListener {
    public void handleAddedImage(ImageAddedEvent event) {
		ImageBox imageBox = new ImageBox(event.getFile().getAbsolutePath(), 
		        GUIManager.ADDED_IMAGE_PREFERRED_WIDTH, GUIManager.ADDED_IMAGE_PREFERRED_HEIGHT, true);
		ComponentManager.getInstance().addImageBox(GUIManager.currentPageNo, imageBox);
		GUIManager.getInstance().getImageStackPane().addImageBox(imageBox);
    }
}