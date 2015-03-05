package com.simulus.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.simulus.EditorApp;

public class EditorControlsController implements Initializable {

	@FXML
	Button grassButton;
	@FXML
	Button dirtButton;
	@FXML
	Button cityButton;
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
	TextField nameTextField;
	@FXML
	TextField dateTextField;
	@FXML
	TextField descTextField;
	@FXML
	TextField authorTextField;
//	@FXML
//	ChoiceBox<KeyValuePair> gridSizeChoiceBox;
	@FXML
	ChoiceBox<String> gridSizeChoiceBox = new ChoiceBox<String>();
	


	// TODO
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		grassButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		
		dirtButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		
		cityButton.setOnAction((event) -> {
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

		saveMapButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		clearMapButton.setOnAction((event) -> {
			EditorApp.getInstance().selectButton((Button) event.getSource());
		});
		
		gridSizeChoiceBox.getItems().add("40");
		gridSizeChoiceBox.getItems().add("60");
		gridSizeChoiceBox.getItems().add("80");
		gridSizeChoiceBox.getSelectionModel().select(0);

		gridSizeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
		    public void changed(ObservableValue<? extends Number> arg0, Number num1, Number num2) {
				final Stage dialog = new Stage();
	            dialog.initModality(Modality.APPLICATION_MODAL);
	            dialog.setResizable(false);

	            Label text = new Label("Are you sure you want to change the GridSize?\n"
	            		+ "The current map will be lost.");
	            Button okayBtn = new Button("Okay");
	            okayBtn.setOnAction((btnEvent) -> dialog.close());
	            Button cancelBtn = new Button("Cancel");
	            cancelBtn.setOnAction((btnEvent) -> dialog.close());
	            
	            VBox vbox = new VBox();
	            vbox.setAlignment(Pos.CENTER);
	            vbox.setSpacing(5.0d);
	            vbox.setPadding(new Insets(0.0d, 0.0d, 5.0d, 0.0d));
	            vbox.getChildren().addAll(text, new HBox(okayBtn, cancelBtn));

	            dialog.setScene(new Scene(vbox));
	            dialog.show();
			}
		});
    
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
	
	public void setGridSize(int size) {
		switch(size){
			case 40:
				gridSizeChoiceBox.getSelectionModel().select(0);
			break;
			case 60:
				gridSizeChoiceBox.getSelectionModel().select(1);
			break;
			case 80:
				gridSizeChoiceBox.getSelectionModel().select(2);
			break;
		}
		
    }
	
	public int getGridSize(){
		
		return Integer.parseInt(gridSizeChoiceBox.getValue().toString());
		
	}

	

}
