package com.brainysoftware.pdfeditor.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.brainysoftware.pdfeditor.ui.event.EventManager;
import com.brainysoftware.pdfeditor.ui.listener.DocumentOpenedListener;
import com.brainysoftware.pdfeditor.ui.listener.ImageAddedListener;
import com.brainysoftware.pdfeditor.ui.listener.PageSelectedListener;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * @author Budi Kurniawan (http://brainysoftware.com)
 */
public class MainController implements Initializable {
    
    public static final String TOOLBAR_GROUP_TABLE = "tablesToolBarGroup";
    public static final String TOOLBAR_GROUP_SQL = "sqlToolBarGroup";

    @FXML private MenuBar menuBar;
    @FXML private ToolBar toolBar;
    @FXML private Label label;
    @FXML private VBox centerPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize MainController...");

        GUIManager.getInstance().updateRecentDocumentsMenu();
        EventManager em = EventManager.getInstance();
        em.registerDocumentOpenedListener(new DocumentOpenedListener());
        em.registerImageAddedListener(new ImageAddedListener());
        em.registerPageSelectedListener(new PageSelectedListener());
    }
    
    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            KeyCode keyCode = keyEvent.getCode();
            if (keyCode == KeyCode.DOWN) {
                System.out.println("execute down");
            } else if (keyCode == KeyCode.UP) {
                System.out.println("execute UP");
            }
        }
    }
}