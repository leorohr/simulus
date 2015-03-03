/*
 * @(#) Launcher.java 
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

import org.jemmy.fx.AppExecutor;


public class LaunchApp {     //Use JemmyFX test framework to launch the application
		
	public static void main (String[] args) {
		
		AppExecutor.execute(MainApp.class);
	
	}
	
}
