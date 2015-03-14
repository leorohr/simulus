package com.simulus.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.simulus.EditorApp;
import com.simulus.MainApp;

public class EditorControlsController implements Initializable {

	@FXML
	Button grassButton;
	@FXML
	Button dirtButton;
	@FXML
	Button waterButton;
	@FXML
	Button blockButton;
	@FXML
	Button eraserButton;
	@FXML
	Button roadHorizontalButton;
	@FXML
	Button roadVerticalButton;
	@FXML
	Button interButton;
	@FXML
	Button openMapButton;
	@FXML
	Button saveMapButton;
	@FXML
	Button clearMapButton;
	@FXML
	Button validateMapButton;
	@FXML
	Button simulateButton;
	@FXML
	TextField nameTextField;
	@FXML
	TextField dateTextField;
	@FXML
	TextField descTextField;
	@FXML
	TextField authorTextField;
//	@FXML
//	ChoiceBox<String> gridSizeChoiceBox = new ChoiceBox<String>();
	@FXML
	ComboBox<String> mapListCB;
	@FXML 
	Hyperlink simulusLink;
	
	File[] matchingFiles;


	// TODO
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		grassButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		
		dirtButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		
		waterButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		
		blockButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		
		eraserButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
	
		roadHorizontalButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});

		roadVerticalButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});

		interButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});

		openMapButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		
		validateMapButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});

		saveMapButton.setOnAction((event) -> {
			
			if ("".equals(getMapName())){
				
	        	Alert alert = new Alert(AlertType.WARNING);
	        	alert.initOwner(EditorApp.getInstance().getPrimaryStage());
	        	alert.setTitle("Map Name");
	        	alert.setHeaderText("A map name is required!");
	        	Optional<ButtonType> result = alert.showAndWait();
	        	if(result.get() != ButtonType.OK)
	        		return;
			}else {
				EditorApp.getInstance().selectButton((Button) event.getSource());
				populateMapList();
			}
				
		});
		
		clearMapButton.setOnAction((event) -> {
			
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.initOwner(EditorApp.getInstance().getPrimaryStage());
        	alert.setTitle("New Map");
        	alert.setHeaderText("The current map will be lost!");
        	alert.setContentText("Continue?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() != ButtonType.OK)
        		return;
        
			try {
				EditorApp.getInstance().selectButton((Button) event.getSource());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		});
		
		simulusLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                openWebpage("http://github.com/leorohr/simulus");
            }
        });
		
		simulateButton.setOnAction((event) -> {
			
			//validate map then save and open simulator
			Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.initOwner(EditorApp.getInstance().getPrimaryStage());
        	alert.setTitle("Opening Simulator");
        	alert.setHeaderText("Opening the Simulator will close the Editor.");
        	alert.setContentText("Save current map and continue?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() != ButtonType.OK)
        		return;
            
        	//Save map to file
        	File mapFile = EditorApp.getInstance().saveMapDialog();
        	if(mapFile == null)
        		return;
        	
        	//Open map in simulator
            MainApp app = MainApp.getInstance(); 
    		if(app == null)
    			app = new MainApp();
    		app.start(new Stage(), false);
    		SimulationController.getInstance().getMap().loadMap(mapFile);

            try {
            	EditorApp.getInstance().getPrimaryStage().close();
				EditorApp.getInstance().stop();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		
		populateMapList();

		mapListCB.setOnAction((event) -> {
		     
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.initOwner(EditorApp.getInstance().getPrimaryStage());
        	alert.setTitle("Change Map");
        	alert.setHeaderText("The current map will be lost!");
        	alert.setContentText("Continue?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() == ButtonType.OK && mapListCB.getSelectionModel().getSelectedIndex() < matchingFiles.length
        			&& mapListCB.getSelectionModel().getSelectedIndex() >= 0)
        		
        		try{
        			String filePath = matchingFiles[mapListCB.getSelectionModel().getSelectedIndex()].getPath();
        			EditorApp.getInstance().loadMap(filePath); 
        				
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
		});
		
//		gridSizeChoiceBox.getItems().add("40");
//		gridSizeChoiceBox.getItems().add("60");
//		gridSizeChoiceBox.getItems().add("80");
//		gridSizeChoiceBox.getSelectionModel().select(0);

//		gridSizeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//			@Override
//		    public void changed(ObservableValue<? extends Number> arg0, Number num1, Number num2) {
//				final Stage dialog = new Stage();
//		        dialog.initModality(Modality.APPLICATION_MODAL);
//		        dialog.setResizable(false);
//
//		        Label text = new Label("The following action will clear the current map!\n"
//		        		+ "Are you sure?");
//		        Button yesBtn = new Button("Yes");
//		        yesBtn.setOnAction((btnEvent) -> {
//		        	//new map of size grid
//		        	dialog.close();});
//		        Button cancelBtn = new Button("Cancel");
//		        cancelBtn.setOnAction((btnEvent) -> {
//		        	gridSizeChoiceBox.getSelectionModel().select(num1.intValue());
//		        	dialog.close();});
//		 
//		        VBox vbox = new VBox();
//		        vbox.setAlignment(Pos.CENTER);
//		        vbox.setSpacing(5.0d);
//		        vbox.setPadding(new Insets(0.0d, 0.0d, 5.0d, 0.0d));
//		        vbox.getChildren().addAll(text, new HBox(yesBtn, cancelBtn));
//
//		        dialog.setScene(new Scene(vbox));
//		        dialog.show();
//			}
//		});
		
    
	}
	
	private void populateMapList(){
		
		mapListCB.getItems().clear();
		File f = new File(EditorControlsController.class.getResource("/resources/maps").getPath());
		matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("map");
		    }
		});

		for(File files : matchingFiles){
			mapListCB.getItems().add(files.getName().substring(0,files.getName().length() - ".map".length()));
		}

	}
	
	public static void openWebpage(String urlString) {
	    try {
	        Desktop.getDesktop().browse(new URL(urlString).toURI());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	

	public void setMapName(String name) {
	        nameTextField.setText(name);
	    }
	
	public String getMapName() {
        return nameTextField.getText();
    }
	
	public void setMapDate(String date) {
        dateTextField.setText(date);
    }

	public String getMapDate() {
		return dateTextField.getText();
	}
	
	public void setMapDesc(String description) {
        descTextField.setText(description);
    }

	public String getMapDesc() {
		return descTextField.getText();
	}
	
	public void setMapAuthor(String author) {
        authorTextField.setText(author);
    }

	public String getMapAuthor() {
		return authorTextField.getText();
	}
	
//	public void setGridSize(int size) {
//		switch(size){
//			case 40:
//				gridSizeChoiceBox.getSelectionModel().select(0);
//			break;
//			case 60:
//				gridSizeChoiceBox.getSelectionModel().select(1);
//			break;
//			case 80:
//				gridSizeChoiceBox.getSelectionModel().select(2);
//			break;
//		}
//		
//    }
	
//	public int getGridSize(){
//		
//		return Integer.parseInt(gridSizeChoiceBox.getValue().toString());
//		
//	}

}
