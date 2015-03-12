package com.simulus.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import org.jemmy.fx.SceneDock;
import org.jemmy.fx.control.LabeledDock;
import org.jemmy.resources.StringComparePolicy;

import com.simulus.test.WriteLog;


public class TestCaseBaseCode {

	public static SceneDock scene;

	public final static Executor executor = Executors.newSingleThreadExecutor();

	public static LaunchApp appThread = new LaunchApp();

	public static  WriteLog writeLog = new WriteLog("test/com/simulus/result/TestCase1.txt", true);



	public static void writeToFile(String fileLocation, boolean append){
		writeLog = new WriteLog(fileLocation, append);
	}
	
	
	public void isTestPassed(boolean status, Integer testScenario){

		if(status == false){
			writeToLog("Test scenario " + testScenario + " passed: " + status);
			writeToLog("Test scenario " + testScenario + " Failed.");
			fail("Test Scenario: " + testScenario + " Failed! ");
		}
		else{
			writeToLog("Test scenario " + testScenario + " passed: " + status);
		}
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


	public static void clickButton(String buttonName) {
		executor.execute(new Runnable() {
			@Override
			public void run() {

				boolean buttonClick = false;

				//Identifying the input button 
				assertEquals(Button.class, new LabeledDock(scene.asParent(), buttonName, 
						StringComparePolicy.EXACT).wrap().getControl().getClass());


				//Find the label of the button 
				LabeledDock b = new LabeledDock(scene.asParent(), buttonName, StringComparePolicy.EXACT);

				
				if(!b.isPressed()){
					b.mouse().click(1);
					buttonClick=true;
				}	

				try {
					writeLog.WriteToLog(buttonName + " Button Detected \n") ;
					writeLog.WriteToLog(buttonName + " Button Clicked: " + buttonClick + "\n");
				} catch (IOException e) {}

			}
		});

	}


	public void checkThreadStatus(String threadName){
		appThread.threadStatus(threadName);  //Query the status of the thread
		writeToLog(threadName + " found: " + appThread.threadFound);

	}


	
	
	public static void selectCheckBox(String checkBoxName) {
		executor.execute(new Runnable() {
			@Override
			public void run() {

				boolean buttonClick = false;

				//Identifying the input checkBox 
				assertEquals(CheckBox.class, new LabeledDock(scene.asParent(), checkBoxName, 
						StringComparePolicy.EXACT).wrap().getControl().getClass());


				//Find the label of the checkBox 
				LabeledDock cb = new LabeledDock(scene.asParent(), checkBoxName, StringComparePolicy.EXACT);

				
				if(!cb.isPressed()){
					cb.mouse().click(1);
					buttonClick=true;
				}	

				try {
					writeLog.WriteToLog(checkBoxName + " CheckBox Detected \n") ;
					writeLog.WriteToLog(checkBoxName + " CheckBox Clicked: " + buttonClick + "\n");
				} catch (IOException e) {}

			}
		});

	}
	
	
	
	
	
	
	


}




