package com.simulus.test;
import org.jemmy.fx.SceneDock;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simulus.controller.SimulationController;

/**
 * This TestCase tests the Controller settings on Tick Rate and Spawn Delay
 *
 */

public class TestCase6 extends TestCaseBaseCode {

	private boolean test1Pass = false;
	private boolean test2Pass = false;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {


		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();

		writeToFile("test/com/simulus/result/TestCase6.txt", true);
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

				double time = SimulationController.getInstance().getTickTime();

				double spawnRate = SimulationController.getInstance().getSpawnRate();

				writeToLog("The current Tick time is: "+ time );

				writeToLog("The current Spawn rate is: "+ spawnRate );

				if(time == 50.0 && spawnRate == 5.0){
					test1Pass = true;
				}
				else{
					test1Pass = false;
					writeToLog("The default Tick time is not equal to 25. OR");
					writeToLog("The default Spawn rate is not equal to 5. Test Fail!");
				}
				writeToLog("Test 1 completed!");

			}
		});
		
		Thread.sleep(3000);
		isTestPassed(test1Pass, 1);
	}


	/**
	 * Run comparison of tick rate and spawn rate against the Controller
	 * @throws InterruptedException
	 */
	@Test
	public void test2() throws InterruptedException{

		executor.execute(new Runnable() {

			@Override
			public void run() {
				writeToLog("Initialising Test2...");

				SimulationController.getInstance().setTickTime(10);
				SimulationController.getInstance().setSpawnRate(1);

				double time = SimulationController.getInstance().getTickTime();

				double spawnRate = SimulationController.getInstance().getSpawnRate();

				if (time != 10 || spawnRate !=1){
					writeToLog(" Tick time has not been changed correctly to 10. OR");
					writeToLog("Spawn rate has not been changed correctly to 1. Test Fail!");
				}
				else{
					test2Pass = true;
				}

				writeToLog("Test 2 completed!");
			}
		});
		Thread.sleep(4000);
		isTestPassed(test2Pass, 2);
	}


}
