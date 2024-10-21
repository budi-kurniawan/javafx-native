package com.brainysoftware.pdfeditor.ui.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * A StackPane that 
 * - disables resize by Parent
 * - supports clipping of its children using a Rectangle called clipper
 */
public class ImageStackPane extends StackPane {

	private ImageBox activeImageBox; // active inserted imageView
    final Rectangle clipper = new Rectangle();

	public ImageStackPane() {
		init();
	}
	
	public void addImageBox(ImageBox imageBox) {
		StackPane.setAlignment(imageBox, Pos.TOP_LEFT);
		StackPane.setMargin(imageBox, new Insets(0, 0, 0, 0));	// initial Insets to make sure Insets is not null
		this.getChildren().add(imageBox);
		imageBox.setOnMousePressed(e -> {
			imageBox.showBorder(true);
			// force order to be highest by removing and re-adding imageView
			this.getChildren().remove(imageBox);
			this.getChildren().add(imageBox); 
			activeImageBox = imageBox;
			imageBox.setPosition(e.getSceneX(),  e.getSceneY(),  e.getX(),  e.getY());
			// hide border of other ImageBoxes
			this.getChildren().filtered(iv -> e.getSource() != iv && iv instanceof ImageBox)
			        .forEach(iv -> ((ImageBox) iv).showBorder(false));
		});	
	}

	@Override
	public boolean isResizable() {
        // This method is invoked by the parent and returning false prevents the resize method 
	    // from being called. 
	    // The purpose is to prevent resizing caused by dragging a child ImageBox
	    return false;
	}
	
	@Override
	/**
	 * With isResizable() returning false, this method will not be called by Parent, but
	 * we call it when a page (an image) is added.
	 */
	public void resize(double width, double height) {
        super.resize(width, height);
        clipper.setWidth(width);
        clipper.setHeight(height);
        this.setClip(clipper);
	    
	}
	
	private void init() {
		this.setOnMousePressed(e -> {
			if (activeImageBox != null) {
			    activeImageBox.setPressPosition(e.getX(),  e.getY());
				if (e.getSource() == e.getTarget() || e.getTarget() == this.getChildren().get(0)) {
					// direct click on PDF page and not propagation from an image
					this.getChildren().filtered(iv -> iv instanceof ImageBox)
					        .forEach(iv -> ((ImageBox) iv).showBorder(false));
				}
			}
		});
	}
	
	
}
