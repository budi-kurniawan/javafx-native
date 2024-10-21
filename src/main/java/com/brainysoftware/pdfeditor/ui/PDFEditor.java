package com.brainysoftware.pdfeditor.ui;

import com.brainysoftware.pdfeditor.PDFHandler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PDFEditor extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.<Parent>load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        GUIManager.currentScene = scene;
        GUIManager.currentStage = stage;

        stage.setTitle("PDF Editor - Brainy Software Inc.");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/icon.png")));
        
        stage.setScene(scene);
        stage.show();        
    }
    
    @Override
    public void stop() {
    	PDFHandler.getInstance().close();
    }

    public static void main(String[] args) {
        launch();
    }    
}