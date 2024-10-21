package com.brainysoftware.pdfeditor.ui.component;

import com.brainysoftware.pdfeditor.Point;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class BorderedImageView extends ImageView {
	private Image originalImage;
	private WritableImage borderedImage;
	private static final Color BORDER_COLOR = Color.BLACK;
    private static final Color HEADER_COLOR = Color.BLACK;
	private static final int HEADER_HEIGHT = 12;
	
	public Point point = new Point();
	public Point point0 = new Point();
	public Point point1 = new Point();
	private String imagePath;
	
	public BorderedImageView(Image image) {
		super(image);
		this.originalImage = image;
		this.borderedImage = createBorderedImage(image);
	}

	public BorderedImageView(Image image, boolean showBorder) {
		this(image);
		this.showBorder(showBorder);
	}

	public BorderedImageView(String imagePath, boolean showBorder) {
		// add "file:" to prevent an invalid URL error in Linux
		super("file:" + imagePath);
		this.imagePath = imagePath;
		this.originalImage = super.getImage();
		this.borderedImage = createBorderedImage(this.originalImage);
		this.showBorder(showBorder);
	}

	public void showBorder(boolean show) {
		this.setImage(show? this.borderedImage : this.originalImage);
		if (!show) {
		    Insets insets = StackPane.getMargin(this);
		    StackPane.setMargin(this, new Insets(insets.getTop() + HEADER_HEIGHT, 0, 0, insets.getLeft()));
		    System.out.println("Hide border");
		}
	}

	private WritableImage createBorderedImage(Image image) {
		int height = (int) image.getHeight();
		int width = (int) image.getWidth();
		PixelReader pixelReader = image.getPixelReader();
		WritableImage writableImage = new WritableImage(width, height + HEADER_HEIGHT);
		PixelWriter pixelWriter = writableImage.getPixelWriter();

		for (int y = 0; y < HEADER_HEIGHT; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setColor(x, y, HEADER_COLOR);
            }
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = pixelReader.getColor(x, y);
				pixelWriter.setColor(x, y + HEADER_HEIGHT, color);
			}
		}
		// draw borders
		int y = HEADER_HEIGHT + height - 1;
		for (int x = 0; x < width; x++) {
			pixelWriter.setColor(x, 0, BORDER_COLOR);
			pixelWriter.setColor(x, y, BORDER_COLOR);			
		}
		int x = width - 1;
		for (y = 0; y < height + HEADER_HEIGHT; y++) {
			pixelWriter.setColor(0, y, BORDER_COLOR);
			pixelWriter.setColor(x, y, BORDER_COLOR);			
		}
		return writableImage;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
	
}
