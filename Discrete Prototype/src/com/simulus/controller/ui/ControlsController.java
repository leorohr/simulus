package com.simulus.controller.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import com.simulus.controller.SimulationController;

public class ControlsController implements Initializable {

	@FXML
	Slider numCarSlider;
	@FXML
	Slider tickrateSlider;
	@FXML
	Button startButton;
	@FXML
	Button stopButton;
	@FXML 
	Label numCarLabel;
	@FXML
	Label tickrateLabel;
	
	SimulationController simController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		simController = SimulationController.getInstance();
	
		tickrateSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				
				tickrateLabel.setText(String.valueOf(newValue.intValue()));
				SimulationController.TICKRATE = newValue.intValue();
			}
		});
		
		numCarSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			numCarLabel.setText(String.valueOf(newValue.intValue()));
			SimulationController.MAXCARS = newValue.intValue();
		});

		startButton.setOnAction((event) -> {
			simController.startSimulation();
		});
		
		stopButton.setOnAction((event) -> {
			simController.stopSimulation();
		});
		
		
	}

}
