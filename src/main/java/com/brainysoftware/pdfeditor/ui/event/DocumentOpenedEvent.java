package com.brainysoftware.pdfeditor.ui.event;

import java.nio.file.Path;
import java.util.EventObject;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class DocumentOpenedEvent extends EventObject {
    private static final long serialVersionUID = 1123887L;
    private Path path;
    public DocumentOpenedEvent(Path source) {
        super(source);
        this.path = source;
    }
    
    public Path getPath() {
    	return path;
    }
}
