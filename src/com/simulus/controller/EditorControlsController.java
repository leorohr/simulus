package com.simulus.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.simulus.EditorApp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EditorControlsController implements Initializable {

	@FXML
	Label numCarLabel;
	@FXML
	Label tickrateLabel;
	@FXML
	Button landButton;
	@FXML
	Button roadHorizontalButton;
	@FXML
	Button roadVerticalButton;
	@FXML
	Button interButton;

	// TODO
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		landButton.setOnAction((event) -> {
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

	}

}
