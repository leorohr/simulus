package com.simulus.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import com.simulus.MainApp;

public class ControlsController implements Initializable {

	@FXML
	Button startButton;
	@FXML
	Button stopButton;
	@FXML
	Slider numCarSlider;
	@FXML
	Slider tickrateSlider;
	@FXML
	Slider maxcarspeedSlider;
	@FXML
	Slider spawnrateSlider;
	@FXML
	Slider cartruckratioSlider;
	@FXML 
	Label numCarLabel;
	@FXML
	Label tickrateLabel;
	@FXML
	Label maxcarspeedLabel;
	@FXML
	Label spawnrateLabel;
	@FXML
	Label cartruckratioLabel;
	@FXML
	CheckBox debugCheckbox;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		MainApp mainApp = MainApp.getInstance();
	
		tickrateSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				
				tickrateLabel.setText(String.valueOf(newValue.intValue()));
				mainApp.setTickTime(newValue.intValue());
			}
		});
		
		numCarSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			numCarLabel.setText(String.valueOf(newValue.intValue()));
			mainApp.setMaxCars(newValue.intValue());
		});
		
		spawnrateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			spawnrateLabel.setText(String.valueOf(newValue.intValue()));
			mainApp.setSpawnrate(newValue.intValue());
			System.out.println(newValue);
		});
		
		maxcarspeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			maxcarspeedLabel.setText(String.valueOf(newValue.intValue()));
			mainApp.setMaxCarSpeed(newValue.intValue());
		});
		
		cartruckratioSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			cartruckratioLabel.setText(String.valueOf((double) ((int)(newValue.doubleValue()*10))/10));
			//TODO make something happen
		});
	
		startButton.setOnAction((event) -> {
			mainApp.startSimulation();
		});
		
		stopButton.setOnAction((event) -> {
			mainApp.stopSimulation();
		});
		
		debugCheckbox.setOnAction((event) -> {
			mainApp.setDebug(debugCheckbox.isSelected());
		});
		
	}

}
