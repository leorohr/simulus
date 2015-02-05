package com.simulus.controller.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;

import com.simulus.controller.SimulationController;

public class SceneController implements Initializable {
	
	@FXML
	private Group group;
	@FXML
	private Button startButton;
	@FXML
	private Button stopButton;
	
	private SimulationController simController = SimulationController.getInstance();
	
	public Group getGroup() {
		return this.group;
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		
		
	}
	
	@FXML
	private void handleStartButtonAction(ActionEvent event) {
		simController.startSimulation();
	}
	
	@FXML
	private void handleStopButtonAction(ActionEvent event) {
		simController.stopSimulation();
	}

}
