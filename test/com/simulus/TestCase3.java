package com.simulus;
import static org.junit.Assert.*;

import org.jemmy.fx.SceneDock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simulus.controller.SimulationController;
import com.simulus.view.vehicles.Vehicle;

/**
 * This TestCase tests the UI of spawning a Ambuslance car
 * 
 *
 */

public class TestCase3 extends TestCaseBaseCode {

	private boolean test1Pass = false;
	private boolean test2Pass = false;
	private boolean test3Pass = false;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {


		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();

		writeToFile("test/com/simulus/result/TestCase3.txt", true);
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
		Thread.sleep(2000);
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
	public void test1() throws InterruptedException{
		executor.execute(new Runnable() {

			@Override
			public void run() {

				writeToLog("Initialising Test1...");
				clickButton("Spawn Ambulance");
				test1Pass = true;
				writeToLog("Test 1 completed!");
			}
		});

		Thread.sleep(2000);
		isTestPassed(test1Pass, 1);
	}

	@Test
	public void test2() throws InterruptedException{

		executor.execute(new Runnable() {

			@Override
			public void run() {
				writeToLog("Initialising Test2...");

				if(appThread.AmbulanceIsSpawnedOnMap()){
					test2Pass = true;
				}
				writeToLog("Test 2 completed!");

			}
		});
		Thread.sleep(1000);
		isTestPassed(test2Pass, 2);
	}




	@Test
	public void test3() throws InterruptedException {

		executor.execute(new Runnable() {

			@Override
			public void run() {
				writeToLog("Initialising Test3...");
				int count;

				count = SimulationController.getInstance().getAmbulanceCount();

				if (count == 1){
					appThread.removeEmergencyCar();
					count--;

				}
				if (count == 0){
					test3Pass = true;
				}

				writeToLog("count is " + count);
				writeToLog("Test 3 completed!");
			}
		});
		Thread.sleep(2000);
		isTestPassed(test3Pass, 3);
	}

	
	
}
