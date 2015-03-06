package com.simulus;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.scene.control.Button;

import org.jemmy.fx.SceneDock;
import org.jemmy.fx.control.LabeledDock;
import org.jemmy.resources.StringComparePolicy;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simulus.controller.SimulationController;
import com.simulus.view.vehicles.Truck;
import com.simulus.view.vehicles.Vehicle;



public class TestCaseBase {

	public static SceneDock scene;

	public final Executor executor = Executors.newSingleThreadExecutor();

	public static LaunchApp appThread = new LaunchApp();

	public static  WriteLog writeLog = new WriteLog("test/com/simulus/result/TestCase1.txt", true);



	public static void writeToFile(String fileLocation, boolean append){
		writeLog = new WriteLog(fileLocation, append);
	}


	public void runClickButtonThread(String button) {
		executor.execute(new Runnable() {
			@Override
			public void run() {

				boolean buttonClick = false;

				//Identifying the input button 
				assertEquals(Button.class, new LabeledDock(scene.asParent(), button, 
						StringComparePolicy.EXACT).wrap().getControl().getClass());


				//Find the label of the button 
				LabeledDock b = new LabeledDock(scene.asParent(), button, StringComparePolicy.EXACT);

				if(!b.isPressed()){
					b.mouse().click(1);
					buttonClick=true;
				}	

				try {
					writeLog.WriteToLog(button + " Button Detected \n") ;
					writeLog.WriteToLog(button + " Button Clicked: " + buttonClick + "\n");
				} catch (IOException e) {}

			}
		});

	}


	public void checkThreadStatus(String threadName){
		appThread.threadStatus(threadName);  //Query the status of the thread
		writeToLog(threadName + " found: " + appThread.threadFound);

	}

	public void writeToLog(String text){
		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					writeLog.WriteToLog(text);
					writeLog.WriteToLog(" ");
				} catch (IOException e) {}
			}

		});
	}

	



}




