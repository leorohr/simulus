package com.simulus.test;
import org.jemmy.fx.SceneDock;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simulus.controller.SimulationController;

/**
 * This TestCase tests the Controller settings on Max.no. of cars spawned on map
 * 
 *
 */

public class TestCase5 extends TestCaseBaseCode {

	private boolean test1Pass = false;
	private boolean test2Pass = false;
	private boolean test3Pass = false;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {


		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();

		writeToFile("test/com/simulus/result/TestCase5.txt", true);
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
		Thread.sleep(5000);
	}


	@Test 
	public void test0() {

		executor.execute(new Runnable() {

			@Override
			public void run() {

				scene = new SceneDock();

				SimulationController.getInstance().setTickTime(20);
				SimulationController.getInstance().setMaxCarSpeed(120);

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

				int carCount; 
				int maxCar; 
				carCount = SimulationController.getInstance().getMap().getVehicleCount();
				maxCar = SimulationController.getInstance().getMaxCars();
				
				writeToLog("Current no of cars on the map: " + carCount);
				writeToLog("Max no of car on the map: " + maxCar);
				
				if(carCount < maxCar){
					test1Pass = true;
				}
				
				writeToLog("Test 1 completed!");

			}
		});

		Thread.sleep(3000);
		isTestPassed(test1Pass, 1);
	}

	
	/**
	 * Adjust the no of cars on the map and then compare
	 * @throws InterruptedException
	 */
	@Test
	public void test2() throws InterruptedException{

		executor.execute(new Runnable() {

			@Override
			public void run() {
				writeToLog("Initialising Test2...");

				int newMax = 40;
				SimulationController.getInstance().setMaxCars(newMax);
				writeToLog("Increased a number of cars on the map to: " + newMax);
				
				int carCount; 
				int maxCar; 
				carCount = SimulationController.getInstance().getMap().getVehicleCount();
				maxCar = SimulationController.getInstance().getMaxCars();
				
				writeToLog("Current no of cars on the map: " + carCount);
				writeToLog("Max no of car on the map: " + maxCar);
				
				if(carCount < maxCar){
					test2Pass= true;
				}
				
				writeToLog("Test 2 completed!");
			}
		});
		Thread.sleep(4000);
		isTestPassed(test2Pass, 2);
	}


}