package com.simulus;

import org.jemmy.fx.SceneDock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simulus.controller.SimulationController;


/**
 * This testcase tests on car truck ratio
 * 
 * 
 */
public class TestCase8 extends TestCaseBaseCode{

	private boolean test1Pass = false;

	private double car;
	private double truck;
	private double vihecleCount;

	private double ratio ;
	private double myRatio;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {


		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();

		writeToFile("test/com/simulus/result/TestCase8.txt", true);
		writeLog.flush();
		writeLog.WriteToLog("FX App thread started \n");

		executor.execute(new Runnable() {

			@Override
			public void run() {
				clickButton("Simulator");

			}
		});
	}
	@Before
	public void setUp() throws Exception {
		Thread.sleep(5000);

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

				ratio = SimulationController.getInstance().getCarTruckRatio();
				writeToLog("Map car Ratio " + ratio);
				//	test1Pass = true;

				truck = SimulationController.getInstance().getTruckCount();

				vihecleCount = SimulationController.getInstance().getMap().getVehicleCount();

				car = vihecleCount - truck;

				myRatio = car/vihecleCount;

				writeToLog("Trucks on map: " + truck);
				writeToLog("Cars on map: " + car);
				writeToLog("Current Ratio " + myRatio);
				writeToLog("Map car Ratio " + ratio);


				if(myRatio< ratio){
					test1Pass = true;
				}


				writeToLog("Test 1 completed!");

			}
		});

		Thread.sleep(3000);
		isTestPassed(test1Pass, 1);
	}



}
