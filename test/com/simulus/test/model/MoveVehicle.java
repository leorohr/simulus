package com.simulus.test.model;

import javafx.application.Platform;

import org.jemmy.fx.SceneDock;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simulus.controller.SimulationController;
import com.simulus.test.TestCaseBaseCode;
import com.simulus.util.enums.*;
import com.simulus.view.vehicles.Car;
import com.simulus.view.vehicles.Vehicle;

/**
 * Test case: execution of AnimationThread and detect its presence 
 */
public class MoveVehicle extends TestCaseBaseCode {

	private boolean test1Pass;
	private boolean test2Pass;
	Behavior behaviour = Behavior.CAUTIOUS;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();
		writeToFile("test/com/simulus/result/AnimationThread.txt", true);
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

		Thread.sleep(4000);

	}


	@Test
	public void test1() throws InterruptedException {

		Thread.sleep(4000);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				writeToLog("Initialising Test1...");

				SimulationController.getInstance().getMap().spawnRandomCar(behaviour);
				test1Pass = true;

			}
		});

		Thread.sleep(4000);
		isTestPassed(test1Pass, 1);

	}
	@Test
	public void test2() throws InterruptedException {

		Thread.sleep(4000);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				writeToLog("Initialising Test2...");

				for (Vehicle v : SimulationController.getInstance().getMap().getVehicles()){
					if(v instanceof Car){
						v.move(v.getDirection());
						writeToLog(v.getDirection().toString());

					}
				}

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				SimulationController.getInstance().getMap().updateMap();
				//Increase tickCount or reset if overflown

				test2Pass= true;
			}
			
		
	});

		Thread.sleep(4000);
		isTestPassed(test2Pass, 1);

}





}




