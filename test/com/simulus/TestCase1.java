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



public class TestCase1 {

	private static SceneDock scene;

	private final Executor executor = Executors.newSingleThreadExecutor();

	private static LaunchApp appThread = new LaunchApp();

	private static WriteLog writeLog = new WriteLog("test/com/simulus/result/TestCase1.txt", true);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		appThread.launchApp();

		Thread.sleep(1000);
		scene = new SceneDock();
		writeLog.flush();
		writeLog.WriteToLog("FX App thread started \n");

	}

	@After
	public void tearDown() throws Exception {

		Thread.sleep(1000);

	}


	private void runClickButtonThread(String button) {
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


	private void checkThreadStatus(String threadName){
		appThread.threadStatus(threadName);  //Query the status of the thread
		writeToLog(threadName + " found: " + appThread.threadFound);

	}

	private void writeToLog(String text){
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



	@Test 
	public void test1() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				writeToLog("Initialising Test1...");

				runClickButtonThread("Start"); 
				checkThreadStatus("animationThread");

				if(appThread.isAlive = true){

					writeToLog("thread status is alive.");
				}

				writeToLog("Test 1 completed!");

			}
		});

	}

	@Test
	public void test2(){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				writeToLog("Initialising Test2...");

				runClickButtonThread("Stop"); 
				checkThreadStatus("animationThread");

				if(appThread.isInterrupted = true){
					writeToLog("thread status is interrupted.");
				}

				writeToLog("Test 2 completed!");
			}
		});

	}

	@Test
	public void test3(){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {


				writeToLog("Initialising Test3...");

				runClickButtonThread("Reset"); 
				checkThreadStatus("animationThread");

				if(appThread.isAlive = true){

					writeToLog("thread status is reseted.");
				}

				writeToLog("Test 3 completed!");

			}
		});

	}

}




