package com.brainysoftware.pdfeditor.ui;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;

import com.brainysoftware.pdfeditor.PropertyManager;
import com.brainysoftware.pdfeditor.ui.component.ImageStackPane;
import com.brainysoftware.pdfeditor.ui.component.ThumbnailView;
import com.brainysoftware.pdfeditor.ui.event.DocumentOpenedEvent;
import com.brainysoftware.pdfeditor.ui.event.EventManager;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Budi Kurniawan (http://brainysoftware.com)
 */
public class GUIManager {
	
	public static Stage currentStage = null;
	public static Scene currentScene = null;
	public static double currentPDFPageWidth = 0;
	public static double currentPDFPageHeight = 0;
	public static int currentPageNo = 0; // 0-based
	public static ThumbnailView selectedThumbnailView = null;
	public static final double ADDED_IMAGE_PREFERRED_WIDTH = 300;
	public static final double ADDED_IMAGE_PREFERRED_HEIGHT = 300;
    
	
    public static final String RECENT_DOCUMENTS_MENU = "recentDocumentsMenu";
	public static final String SAVE_DOCUMENT_MENU_ITEM = "saveDocumentMenuItem";
    public static final String SAVE_AS_DOCUMENT_MENU_ITEM = "saveAsDocumentMenuItem";

	private static GUIManager instance;
    private MenuBar menuBar;

	public static GUIManager getInstance() {
        return instance;
    }
	
	public static class Builder {
	    public static GUIManager build(MenuBar menuBar) {
	        synchronized(GUIManager.class) {
	            if (instance == null) {
	                instance = new GUIManager(menuBar);
	            }
	        }
	        return instance;
	    }
	}
	
	private GUIManager(MenuBar menuBar) {
	    this.menuBar = menuBar;
	}


	public void setWindowTitle(String title) {
	    currentStage.setTitle(title);
	}
	
    public void showMessageDialog(String header, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setTitle("Message");
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }
    
    public void showErrorDialog(String header, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }
    
    public ButtonType showConfirmDialog(String header, String content) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm");
        confirmAlert.setHeaderText(header);
        confirmAlert.setContentText(content);
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
    
    public String showInputDialog(String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public Menu getMenu(MenuBar menuBar, String id) {
        if (menuBar == null) {
            return null;
        }
        ObservableList<Menu> menus = menuBar.getMenus();
        for (Menu menu : menus) {
        	if (id.equals(menu.getId())) {
        		return menu;
        	} else {
        		// try submenus. Note that Menu is derived from MenuItem
        		ObservableList<MenuItem> menuItems = menu.getItems();
        		for (MenuItem menuItem : menuItems) {
        			if (menuItem instanceof Menu && id.equals(menuItem.getId())) {
        				return (Menu) menuItem;
        			}
        		}
        	}
        }
        return null;
    }
    
    public MenuItem getMenuItem(String id) {
        ObservableList<Menu> menus = menuBar.getMenus();
        for (Menu menu : menus) {
            ObservableList<MenuItem> items = menu.getItems();
            for (MenuItem item : items) {
                if (id.equals(item.getId())) {
                    return item;
                }
            }
        }
        return null;
    }
    
    public Group getToolBarGroup(ToolBar toolBar, String id) {
        ObservableList<Node> items = toolBar.getItems();
        for (Node node : items) {
            if (id.equals(node.getId())) {
                return (Group) node;
            }
        }
        return null;
    } 
    
    public Node getToolBarNode(ToolBar toolBar, String id) {
        ObservableList<Node> items = toolBar.getItems();
        for (Node node : items) {
            if (node instanceof Group) {
                Group group = (Group) node;
                ObservableList<Node> children = group.getChildren();
                for (Node child : children) {
                    if (child instanceof HBox) {
                        HBox hBox = (HBox) child;
                        ObservableList<Node> buttons = hBox.getChildren();
                        for (Node child2 : buttons) {
                            if (id.equals(child2.getId())) {
                                return child2;
                            }
                        }
                    }
                }
            }
        }
        return null;
    } 

    public <T> TableColumn<T, String> createTableColumn(T type, String label,
            String propertyName, int minWidth) {
        TableColumn<T, String> column = new TableColumn<>(label);
        column.setMinWidth(minWidth);
        column.setCellValueFactory(
                new PropertyValueFactory<T, String>(propertyName));
        return column;
    }

    
    public Node getRootNode(Node control) {
        Node node = control;
        while (node.getParent() != null) {
            node = node.getParent();
        }
        return node;
    }
    
    private static NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    public static String formatMoney(BigDecimal value) {
        String result = null;
        try  {
            result = numberFormat.format(value  );
        } catch (Exception e    ) {
            System.out.println("error:  " + e.getMessage() + ", value:"
             + value);
        }
        return result;
    }
    
    public List<File> chooseFiles(String title, File initDir) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(initDir);
        return fileChooser.showOpenMultipleDialog(null);
    }
    
    public void setVisibleMenuById(String menuId, boolean value) {
        ObservableList<Menu> menus = menuBar.getMenus();
        for (Menu menu : menus) {
        	if (menuId.equals(menu.getId())) {
        		menu.setVisible(value);
       		 	break;
       	 	}
        }
    }

    public void setDisableMenuItemById(String menuId, boolean value) {
        ObservableList<Menu> menus = menuBar.getMenus();
        for (Menu menu : menus) {
            if (menuId.equals(menu.getId())) {
                menu.setVisible(value);
                break;
            }
            menu.getItems().forEach(menuItem -> {
                if (menuId.equals(menuItem.getId())) {
                    menuItem.setDisable(value);
                    return;
                }
            });
        }
    }

    public ImageStackPane getImageStackPane() {
		return (ImageStackPane) currentScene.lookup("#imageStackPane");
    }
    
    public VBox getThumbnailContainer() {
		return (VBox) currentScene.lookup("#thumbnailContainer");

    }
    
    public void updateRecentDocumentsMenu() {
        PropertyManager propertyManager = PropertyManager.getInstance();
        List<String> recentDocuments = propertyManager.getRecentDocuments();
        Menu rdMenu = getMenu(menuBar, RECENT_DOCUMENTS_MENU);
        if (rdMenu != null ) {
            rdMenu.getItems().forEach(menuItem -> {
                menuItem.setOnAction(null);
            });
            rdMenu.getItems().clear();
            if (recentDocuments.size() > 0) {
                GUIManager.getInstance().setDisableMenuItemById(RECENT_DOCUMENTS_MENU, false);
            }
            int count = 1;
            for (String recentDocument : recentDocuments) {
                MenuItem menuItem = new MenuItem(count++ + ". " + recentDocument);
                menuItem.setOnAction(ev -> {
                    EventManager.getInstance().fireDocumentOpenedEvent(
                            new DocumentOpenedEvent(Paths.get(recentDocument)));
                });
                rdMenu.getItems().add(menuItem);
            }
        }        
        
    }
    
//    private static void mergeImages(int pageNo) {
//    	// merge any images in the shown page
//		Scene scene = GUIUtil.currentScene;
//		ImageStackPane imageStackPane = (ImageStackPane) scene.lookup("#imageStackPane");
//		imageStackPane.getChildren().filtered(iv -> iv instanceof ImageBox).forEach(iv -> {
//				PDFHandler handler = PDFHandler.getInstance();
//				ImageBox biv = (ImageBox) iv;
//		    	Insets margin = StackPane.getMargin(biv);
//		    	float insertionX = (float) (margin.getLeft() / GUIUtil.currentPDFPageWidth);
//		    	float imageHeight = (float) biv.getImageHeight();
//		    	float insertionY = (float) ((GUIUtil.currentPDFPageHeight - margin.getTop() - imageHeight) / GUIUtil.currentPDFPageHeight);
//		    	System.out.println("insert into " + biv.getImagePath() + " at (" + insertionX + ", " + insertionY + ")");
//		    	try {					
//					handler.addImage(biv.getImagePath(), pageNo, insertionX, insertionY);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		});
//		
//		
//    }
    
}