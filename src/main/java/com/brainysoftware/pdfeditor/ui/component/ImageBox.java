package com.brainysoftware.pdfeditor.ui.component;

import com.brainysoftware.pdfeditor.Point;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ImageBox extends VBox {
    
    private Point scenePoint = new Point();
    private Point pressPoint = new Point();
    private Point point = new Point();
    private Point resizeScenePoint = new Point();
    private Point resizePoint = new Point();

    private String imagePath;
    private ImageView imageView;
    private Image image;
    private double preferredWidth = -1;
    private double preferredHeight = -1;
    private static final String BORDER_COLOR = "yellow";
    private static final double RESIZE_HANDLE_WIDTH = 8.0;
    private static final double RESIZE_HANDLE_HEIGHT = 8.0;
    private static final Color RESIZE_HANDLE_COLOR = Color.ALICEBLUE;

    public ImageBox(String imagePath, double preferredWidth, double preferredHeight, boolean showBorder) {
        super();
        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        this.imagePath = imagePath;
        this.showBorder(showBorder);
        // add "file:" to prevent an invalid URL error in Linux
        this.image = new Image("file:" + this.imagePath);
        init();
    }

    private void init() {
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        if (this.preferredWidth != -1 && imageWidth > this.preferredWidth) {
            imageHeight *= this.preferredWidth / imageWidth;
            imageWidth = this.preferredWidth;
        }
        if (this.preferredHeight != -1 && imageHeight > this.preferredHeight) {
            imageWidth *= this.preferredHeight / imageHeight;
            imageHeight = this.preferredHeight;
        }
        imageView = new ImageView(image);
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        getChildren().add(imageView);
        //VBox.setMargin(imageView, new Insets(10, 0, 0, 0));
        setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE); // set size to the size of children
//        setStyle("-fx-cursor:hand");
        setCursor(Cursor.MOVE);
        Rectangle resizeHandle = new Rectangle(
                RESIZE_HANDLE_WIDTH, RESIZE_HANDLE_HEIGHT, RESIZE_HANDLE_COLOR);
        resizeHandle.setCursor(Cursor.NW_RESIZE);
        HBox hbox = new HBox(resizeHandle);
        VBox.setMargin(hbox, new Insets(-RESIZE_HANDLE_HEIGHT, 0, 0, 0));
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        getChildren().add(hbox);
        
        this.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - scenePoint.x + pressPoint.x - point.x;
            double dy = e.getSceneY() - scenePoint.y + pressPoint.y - point.y;
            e.setDragDetect(true);
            StackPane.setMargin(this, new Insets(dy, 0, 0, dx));                    
        });
        
        resizeHandle.setOnMousePressed(e -> {
        	resizePoint.x = e.getX();
        	resizePoint.y = e.getY();
        	resizeScenePoint.x = e.getSceneX();
        	resizeScenePoint.y = e.getSceneY();        			
        	e.consume();
        });
        resizeHandle.setOnMouseDragged(e -> {
            double dx = e.getX() - resizePoint.x;
            double dy = e.getY() - resizePoint.y;
            double newWidth = Math.max(5, imageView.getFitWidth() + dx);
            double newHeight = Math.max(5, imageView.getFitHeight() + dy);
            imageView.setFitWidth(newWidth);
            imageView.setFitHeight(newHeight);
            e.setDragDetect(true);
        	e.consume();
        });

    }
    
    public void showBorder(boolean show) {
    	if (show) {
            setStyle("-fx-border-color: " + BORDER_COLOR);
    	} else {
            setStyle("-fx-border-color: transparent");
    	}
    }
    
    public void setPosition(double x1, double y1, double x2, double y2) {
        scenePoint.x = x1;
        scenePoint.y = y1;
        point.x = x2;
        point.y = y2;
    }

    public void setPressPosition(double x, double y) {
        pressPoint.x = x;
        pressPoint.y = y;
    }
    
    public String getImagePath() {
    	return this.imagePath;
    }

    public double getImageFitWidth() {
        return imageView.getFitWidth();
    }

    public double getImageFitHeight() {
    	return imageView.getFitHeight();
    }

    // returns image as a byte array
    // TODO needs to be tested
    public byte[] getImage() {
    	int width = (int) image.getWidth();
    	int height = (int) image.getHeight();
    	byte[] data = new byte[width * height * 4];
    	image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), data, 0, width * 4);
    	return data;
    }
}
