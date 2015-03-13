/*
 * @(#) LanuchScenicView.java 
 * 
 * Copyright (c) 2015 Team Simulus at King's College London. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary of Team Simulus 
 * 7CCSMGPR Group Project(14~15 SEM2 1) at King's College London. 
 * You shall not disclose such confidential information and shall use it only in accordance 
 * with the agreement and regulation set by Simulus and the Department of Informatics at King's College London . 
 * 
 * The software is set to be reviewed by module coordinator of 7CCSMGPR Group Project(14~15 SEM2)
 * 
 */

/* 
 * VERSION HISTORY:
 * 
 * 1.00 01/03/2015 Initial Version  
 */


package com.simulus.test;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.scenicview.ScenicView;

import com.simulus.EditorApp;
import com.simulus.MainApp;
import com.simulus.Startup;
import com.simulus.controller.SimulationController;

/**
 * The Class launches the MainApp with ScenicView 
 * ScenicView is a framework to query UI elements 
 *
 */
public class LaunchScenicView extends Startup {

	
	
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Simulus");
		this.primaryStage.setResizable(false);
		
		ImageView img = new ImageView(Startup.class.getResource("/resources/simulus.png").toString());
		Button simBtn = new Button("Simulator");
		simBtn.setPrefSize(100.0d, 50.0d);
		simBtn.setOnAction((event) -> {
			MainApp app = new MainApp();
			app.start(primaryStage);
			ScenicView.show(app.getInstance().getRootLayout());  
		});
		
		Button editorBtn = new Button("Editor");
		editorBtn.setPrefSize(100.0d, 50.0d);
		editorBtn.setOnAction((event) -> {
			EditorApp app = new EditorApp();
			app.start(primaryStage);
		});
		
		HBox btnBox = new HBox(simBtn, editorBtn);
		btnBox.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.setSpacing(75.0d);
		
		VBox vbox = new VBox(img, btnBox);
		vbox.setSpacing(10.0d);
		vbox.setPadding(new Insets(0.0d, 0.0d, 10.0d, 0.0d));
		
		primaryStage.setScene(new Scene(vbox));
		primaryStage.show();
		
	 
	}


	public static void main(String[] args) {

		launch(args);
		


	}


}