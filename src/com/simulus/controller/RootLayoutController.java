package com.simulus.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;

public class RootLayoutController implements Initializable {

	@FXML
	private MenuBar menuBar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//for mac menubar
		menuBar.setUseSystemMenuBar(true); 
	}

}