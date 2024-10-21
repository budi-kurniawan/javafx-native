package com.brainysoftware.pdfeditor.ui.event;

import java.io.File;
import java.util.EventObject;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class ImageAddedEvent extends EventObject {
    private static final long serialVersionUID = 1090L;
    private File file;
    private int pageNo;
    public ImageAddedEvent(File source, int pageNo) {
        super(source);
        this.file = source;
        this.pageNo = pageNo;
    }
    public File getFile() {
        return file;
    }
    
    public int getPageNo() {
    	return pageNo;
    }
}
