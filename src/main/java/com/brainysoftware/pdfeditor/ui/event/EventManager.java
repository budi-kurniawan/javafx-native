package com.brainysoftware.pdfeditor.ui.event;

import java.util.ArrayList;
import java.util.List;

import com.brainysoftware.pdfeditor.ui.listener.DocumentOpenedListener;
import com.brainysoftware.pdfeditor.ui.listener.ImageAddedListener;
import com.brainysoftware.pdfeditor.ui.listener.PageSelectedListener;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class EventManager {
    private static EventManager instance;
    private List<ImageAddedListener> imageAddedListeners = new ArrayList<>();
    private List<DocumentOpenedListener> documentOpenedListeners = new ArrayList<>();
    private List<PageSelectedListener> pageSelectedListeners = new ArrayList<>();
    
    private EventManager() {
    }
    
    public static EventManager getInstance() {
        if (instance == null) {
            synchronized(EventManager.class) {
                if (instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }
    
    public void registerImageAddedListener(ImageAddedListener listener) {
        imageAddedListeners.add(listener);
    }
    
    public void registerDocumentOpenedListener(DocumentOpenedListener listener) {
        documentOpenedListeners.add(listener);
    }
    

    public void registerPageSelectedListener(PageSelectedListener listener) {
        pageSelectedListeners.add(listener);
    }
    
    public void fireImageAddedEvent(ImageAddedEvent event) {
    	imageAddedListeners.forEach(listener -> listener.handleAddedImage(event));
    }
    
    public void fireDocumentOpenedEvent(DocumentOpenedEvent event) {
        documentOpenedListeners.forEach(listener -> listener.handleOpenedDocument(event));
    }    

    public void firePageSelectedEvent(PageSelectedEvent event) {
        pageSelectedListeners.forEach(listener -> listener.handlePageSelected(event));
    }    

}