package com.brainysoftware.pdfeditor.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.brainysoftware.pdfeditor.ui.component.ImageBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TestApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		Button btn = new Button();
		btn.setText("Say 'Hello World'");

		StackPane root = new StackPane();
		root.setStyle("-fx-background-color: brown;");
		root.getChildren().add(btn);
		
		Path path = Paths.get("./");
		// path points to a different directoy when a native image is executed
		String imageUrl = path.toAbsolutePath() + "/src/main/resources/image/gorilla.png";
		imageUrl = "C:\\Users\\budi2020\\Documents\\JavaProjects\\pdf-editor/src/main/resources/image/gorilla.png";
		
		String str = "imageUrl : " + imageUrl;
	    Path logPath = Paths.get("C:\\Users\\budi2020\\Documents\\JavaProjects\\pdf-editor\\log.txt");

	    try {
	        Files.writeString(logPath, str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("============= image url:" + imageUrl);
		double preferredWidth = 200;
		double preferredHeight = 200;
		ImageBox imageBox = new ImageBox(imageUrl, preferredWidth, preferredHeight, true);
		imageBox.setOnMousePressed(e -> {
			imageBox.showBorder(true);
			imageBox.setPosition(e.getSceneX(),  e.getSceneY(),  e.getX(),  e.getY());			
		});	
		
		root.setOnMousePressed(e -> {
			if (e.getSource() == e.getTarget() || e.getTarget() == root.getChildren().get(0)) {
				// direct click on PDF page and not propagation from an image
				root.getChildren().filtered(iv -> iv instanceof ImageBox)
				        .forEach(iv -> ((ImageBox) iv).showBorder(false));
			}
		});
		
		
		int left = 100;
		int top = 10;
		
		StackPane.setAlignment(imageBox, Pos.TOP_LEFT);
		StackPane.setMargin(imageBox, new Insets(top, 0, 0, left));					
		root.getChildren().add(imageBox);
		
		root.setOnMousePressed(e -> {
			imageBox.setPressPosition(e.getX(),  e.getY());
			if (e.getSource() == e.getTarget() || e.getTarget() == root.getChildren().get(0)) {
				// direct click on PDF page and not propagation from an image
				root.getChildren().filtered(iv -> iv instanceof ImageBox)
				        .forEach(iv -> ((ImageBox) iv).showBorder(false));
			}
		});

        
		Scene scene = new Scene(root, 400, 450);
		primaryStage.setTitle("Gorilla World!");
        //primaryStage.getIcons().add(new Image(TestApp.class.getResourceAsStream("/image/icon.png")));
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/icon.png")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// See https://stackoverflow.com/questions/28253169/javafx-how-to-make-the-border-of-imageview-when-i-click-the-imageview
//	@Override
//	public void start(Stage primaryStage) {
//		
//	    PseudoClass imageViewBorder = PseudoClass.getPseudoClass("border");
//
//		Image image = new Image("C:/users/budi2020/Desktop/MySignature.jpg");
//	    ImageView imageview = new ImageView(image);
//
//	    BorderPane imageViewWrapper = new BorderPane(imageview);
//	    imageViewWrapper.getStyleClass().add("image-view-wrapper");
//
//	    BooleanProperty imageViewBorderActive = new SimpleBooleanProperty() {
//	        @Override
//	        protected void invalidated() {
//	            imageViewWrapper.pseudoClassStateChanged(imageViewBorder, get());
//	        }
//	    };
//
//	    imageview.setOnMouseClicked(ev -> imageViewBorderActive
//	            .set(!imageViewBorderActive.get()));
//
//	    BorderPane root = new BorderPane(imageViewWrapper);
//	    root.setPadding(new Insets(15));
//
//	    Scene scene = new Scene(root, 700, 400);
//        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
//	    primaryStage.setScene(scene);
//	    primaryStage.show();	}

	public static void main(String[] args) {
		launch();
	}
}