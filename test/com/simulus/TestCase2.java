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


public class TestCase2 extends TestCaseBase  {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();

		writeToFile("test/com/simulus/result/TestCase2.txt", true);
		writeLog.flush();
		writeLog.WriteToLog("FX App thread started \n");

	}

	@After
	public void tearDown() throws Exception {

		Thread.sleep(3000);

	}


	@Test 
	public void test1() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				writeToLog("Initialising Test1...");

				runClickButtonThread("Start"); 

				writeToLog("Test 1 completed!");

			}
		});

	}

	@Test 
	public void test2() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				writeToLog("Initialising Test2...");


				if (appThread.truckIsSpawnedOnMap()){
					writeToLog(" Truck is on Map: " + appThread.truckIsSpawnedOnMap() );
				}
				else{
					writeToLog(" Truck is on Map: " + appThread.truckIsSpawnedOnMap() );
				}


				writeToLog("Test 2 completed!");

			}
		});

	}

	@Test 
	public void test3() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				writeToLog("Initialising Test3...");



				if (appThread.carIsSpawnedOnMap()){
					writeToLog(" Car is on Map: " + appThread.carIsSpawnedOnMap() );
				}

				else{
					writeToLog(" Car is on Map: " + appThread.truckIsSpawnedOnMap() );
				}


				writeToLog("Test 3 completed!");

			}
		});

	}



}




