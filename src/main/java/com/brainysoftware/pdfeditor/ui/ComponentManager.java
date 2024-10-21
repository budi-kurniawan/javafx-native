package com.brainysoftware.pdfeditor.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brainysoftware.pdfeditor.ui.component.ImageBox;

public class ComponentManager {
	
	private static ComponentManager instance;
	// ImageBoxes in a page
	private Map<Integer, List<ImageBox>> imageMap = new HashMap<>();
	private ComponentManager() {
	}
	
	public static ComponentManager getInstance() {
		synchronized(ComponentManager.class) {
			if (instance == null) {
				instance = new ComponentManager();			
			}
		}
		return instance;
	}
	
	public List<ImageBox> getImageBoxes(int pageNo) {
		List<ImageBox> imageBoxes = imageMap.get(pageNo);
		return (imageBoxes == null ? Collections.emptyList() : imageBoxes);
	}
	
	public void addImageBox(int pageNo, ImageBox imageBox) {
		List<ImageBox> imageBoxes = imageMap.get(pageNo);
		if (imageBoxes == null) {
			imageBoxes = new ArrayList<>();
			imageMap.put(pageNo, imageBoxes);
		}
		imageBoxes.add(imageBox);		
	}	
}
