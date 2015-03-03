/*
 * @(#) MainAppTest.java 
 * 
 * Copyright (c) 2015 Team Simulus at King's College London. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary of Team Simulus 
 * 7CCSMGPR Group Project(14~15 SEM2 1) at Kings' College London. 
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


package com.simulus;

import javafx.stage.Stage;
import org.scenicview.ScenicView;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;

/**
 * The Class launches the MainApp with ScenicView 
 * ScenicView is a framework to query UI elements 
 *
 */
public class LaunchScenicView extends MainApp {

	@Override   //Override constructor to build ScenicView
	public void start(final Stage primaryStage)  {
	
		this.setPrimaryStage(primaryStage);
		this.getPrimaryStage().setTitle("Simulus");
		this.getPrimaryStage().setResizable(false);
		
		getInitRobotLayout();
		getShowControls();

        SimulationController.init();
    
        ScenicView.show(getRootLayout());   
    
	}


	public static void main(String[] args) {
		
		launch(args);
	}
	
	
}
