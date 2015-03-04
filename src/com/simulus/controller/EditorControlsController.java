package com.simulus.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
	}
	

	public void setMapName(String text) {
	        nameTextField.setText(text);
	    }

}
