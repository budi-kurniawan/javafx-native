package com.brainysoftware.pdfeditor;

import java.nio.file.Path;

public class LiveValues {
	
	// indicates whether a request to save an open document is allowed to override the selected path
	public static boolean allowOverrideFile = false;
	
	// Indicates whether or not the user has updated the document and the user needs to be reminded
	// when closing the document
	public static boolean documentEdited = false;
	
	// the opened document. Can be null.
	public static Path openedDocument = null; // unfortunately JavaFX FileChoose does not work with Path

	// the File to which an edited document has been saved. Can be null
	public static Path savedDocument = null; // unfortunately JavaFX FileChoose does not work with Path

	// whether user has previously save the current document and therefore a SaveDialog will not be shown
	public static boolean directSave = false;
}
