package com.simulus;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.jemmy.fx.SceneDock;
import org.jemmy.fx.control.LabeledDock;
import org.jemmy.resources.StringComparePolicy;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;



public class TestCase1 {

	private static SceneDock scene;
	
	private final  Executor executor = Executors.newSingleThreadExecutor();
	private static Thread appThread;

	static WriteLog writeLog = new WriteLog("test/com/simulus/result/TestCase1.txt", true);

		
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//	AppExecutor.executeNoBlock(MainApp.class);
		
		appThread = new Thread("JavaFX Init Thread") {
			public void run() {
				
			
				Application.launch(MainApp.class, new String[0]);
			}
		};
		appThread.setDaemon(true);
		appThread.start();
		System.out.printf("FX App thread started\n");
		
		
		Thread.sleep(1000);
		scene = new SceneDock();
		writeLog.flush();

	}

	@After
	public void tearDown() throws Exception {

		Thread.sleep(5000);

	}


	@Rule
	public ExpectedException exception = ExpectedException.none();


	private void runClickButtonThread(String button) {
		executor.execute(new Runnable() {


			@Override
			public void run() {

				boolean buttonClick = false;

				assertEquals(Button.class, new LabeledDock(scene.asParent(), button, 
						StringComparePolicy.EXACT).wrap().getControl().getClass());

				try {
					writeLog.WriteToLog(button + " Button Detected") ;
				} catch (IOException e) {

				}

				

				LabeledDock b = new LabeledDock(scene.asParent(), button, StringComparePolicy.EXACT);

				if(!b.isPressed()){
					b.mouse().click(1);
					buttonClick=true;
				}	

				
				
				try {
					writeLog.WriteToLog(button + " Button Clicked: " + buttonClick );
					writeLog.WriteToLog(" ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				//new LabeledDock(scene.asParent(), button, StringComparePolicy.EXACT).mouse().click();				
				//				System.out.println("button pushed");
				//				  LabeledDock button = new LabeledDock(new SceneDock().asParent(), Button.class);
				//				  Labeled theButtonItself = button.control();

			}
		});


	}



	@Test 
	public void test1() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				runClickButtonThread("Start"); 


				try {
					writeLog.WriteToLog("Test 1 completed!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	@Test
	public void test2(){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {


				runClickButtonThread("Stop"); 
				try {
					writeLog.WriteToLog("Test 2 completed!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		});

	}

	@Test
	public void test3(){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
			
				runClickButtonThread("Reset"); 
				try {
					writeLog.WriteToLog("Test 3 completed!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

}




