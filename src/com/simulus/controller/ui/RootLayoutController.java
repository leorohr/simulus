package com.simulus.controller.ui;

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
		
		//native menubar
		menuBar.setUseSystemMenuBar(true); 
	}

}
