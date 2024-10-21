package com.brainysoftware.pdfeditor.ui.event;

import java.util.EventObject;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class PageSelectedEvent extends EventObject {
    private static final long serialVersionUID = 1092340L;
    private int pageNo;
    public PageSelectedEvent(Object source, int pageNo) {
    	super(source);
        this.pageNo = pageNo;
    }

    public int getPageNo() {
    	return pageNo;
    }
}
