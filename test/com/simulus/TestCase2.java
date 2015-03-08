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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.simulus.controller.SimulationController;
import com.simulus.view.vehicles.Truck;
import com.simulus.view.vehicles.Vehicle;


public class TestCase2 extends TestCaseBaseCode  {

	private boolean test1Pass;
	private	boolean test2Pass;
	private	boolean test3Pass;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();

		writeToFile("test/com/simulus/result/TestCase2.txt", true);
		writeLog.flush();
		writeLog.WriteToLog("FX App thread started \n");

		executor.execute(new Runnable() {

			@Override
			public void run() {
				clickButton("Simulator");

			}
		});

	}

	@After
	public void tearDown() throws Exception {

		Thread.sleep(3000);
	}



	@Test 
	public void test0() {

		executor.execute(new Runnable() {

			@Override
			public void run() {

				scene = new SceneDock();
				clickButton("Start"); 

			}
		});

	}

	

	
	
	@Test 
	public void test1() throws InterruptedException {

		executor.execute(new Runnable() {

			@Override
			public void run() {

				writeToLog("Initialising Test1...");

				if (appThread.carIsSpawnedOnMap()){
					test1Pass = true;
					writeToLog(" Cars detected on the map: " + appThread.carIsSpawnedOnMap() );
				}
				else{
					writeToLog(" Cars detected on the Map: " + appThread.truckIsSpawnedOnMap() );
				}
				writeToLog("Test1 completed!");
			}


		});

		Thread.sleep(4000);


		isTestPassed(test1Pass, 1);

	}


	@Test 
	public void test2() throws InterruptedException {


		executor.execute(new Runnable() {

			@Override
			public void run() {

				writeToLog("Initialising Test2...");

				if (appThread.truckIsSpawnedOnMap()){
					test2Pass = true;
					writeToLog(" Trucks detected on Map: " + appThread.truckIsSpawnedOnMap() );

				}
				else{
					writeToLog(" Trucks detected on map: " + appThread.truckIsSpawnedOnMap() );
					test2Pass = false;

				}

				writeToLog("Test2 completed!");

			}
		});


		Thread.sleep(2000);

		isTestPassed(test2Pass, 2);

	}

	@Test
	public void test3() throws InterruptedException{

		executor.execute(new Runnable() {


			@Override
			public void run() {

				writeToLog("Initialising Test3...");

				selectCheckBox("Debug");

				if (appThread.debugBoxSelected()){
					test3Pass = true;
					
				}
				writeToLog("Test3 completed!");
			}
		});

		Thread.sleep(2000);
		isTestPassed(test3Pass, 3);
	}



}




