package com.brainysoftware.pdfeditor.ui.component;

import java.io.ByteArrayInputStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ThumbnailView extends VBox {

	private int pageNo;
	private Image image;
	private static final Background focusBackground = new Background( new BackgroundFill( Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY ) );
    private static final Background unfocusBackground = new Background( new BackgroundFill( Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY ) );
    
	
	public ThumbnailView(byte[] imageData, int pageNo) {
		this.image = new Image(new ByteArrayInputStream(imageData));
		this.pageNo = pageNo;
		init();
	}
	
	private void init() {
		ImageView thumbnailImageView = new ImageView(image);
		this.setPadding(new Insets(5));
	    this.setAlignment(Pos.CENTER);
	    String style_inner = "-fx-border-color: #a0a0a0;"
	              + "-fx-border-width: 3;"
	              + "-fx-border-style: solid;";
	    this.setStyle(style_inner);		    
	    this.getChildren().add(thumbnailImageView);
	    Label label = new Label("" + (pageNo + 1));
	    this.getChildren().add(label);
	    
//	    Integer count2 = Integer.valueOf(count);
//	    borderBox.setOnMouseClicked(e ->
//        {
//        	borderBox.requestFocus();
//        	//merge any images in current page, before showing a different file
//    		mergeImages(count2);
//        	showPage(file, count2);
//        	borderBox.setBackground(focusBackground);
//        });
	    
//	    if (count == 0) {
//	    	firstBorderBox = borderBox;
//	    }
	    
        // use different backgrounds for focused and unfocused states
//	    borderBox.backgroundProperty().bind( Bindings
//                .when( borderBox.focusedProperty() )
//                .then( focusBackground )
//                .otherwise( unfocusBackground )
//        );
	    
//	    thumbnailContainer.getChildren().add(borderBox);
//	    count++;		   

	}

	public void setBorderVisible(boolean visible) {
		if (visible) {
			this.setBackground(focusBackground);
		} else {
			this.setBackground(unfocusBackground);
		}		
	}
	
	public int getPageNo() {
		return pageNo;
	}
}
