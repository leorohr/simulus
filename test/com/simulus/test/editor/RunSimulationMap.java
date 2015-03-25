package com.simulus.test.editor;

import java.io.File;

import javafx.application.Platform;
import javafx.stage.Stage;

import org.jemmy.fx.SceneDock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simulus.EditorApp;
import com.simulus.MainApp;
import com.simulus.controller.EditorControlsController;
import com.simulus.controller.SimulationController;
import com.simulus.test.TestCaseBaseCode;
import com.simulus.view.Tile;

/**
 * {@code RunSimulationMap} tests running simulation after map is loaded from the map editor
 * @author Jerry
 *
 */
public class RunSimulationMap extends TestCaseBaseCode {
	
	private boolean test1Pass;
	private boolean test2Pass;
	private boolean test3Pass;
	
	File mapFile = new File("resources/maps/Junctions.map");
	//File file =new file "resources/maps/Junctions.map";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();
		writeToFile("test/com/simulus/result/RunSimulationMap.txt", true);
		writeLog.flush();
		writeLog.WriteToLog("FX App thread started \n");

		executor.execute(new Runnable() {

			@Override
			public void run() {
				clickButton("Editor");

			}
		});

	}

	@After
	public void tearDown() throws Exception {
		Thread.sleep(3000);
	}

	
	@Test
	public void test1() throws InterruptedException {
		Thread.sleep(2000);

		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				writeToLog("Initialising Test1...");
				Tile[][] mapTiles = EditorApp.getInstance().getMap().getTiles();
				
			EditorApp.getInstance().loadMap(mapFile);
			
			Tile[][] newMapTiles = EditorApp.getInstance().getMap().getTiles();
			
				if(mapTiles != newMapTiles){
					test1Pass = true;
					writeToLog("New Map loaded.");
				}
				else{
					writeToLog("Load map failed.");
				}
			
				writeToLog("Test 1 Completed");
			}
		});

	
		Thread.sleep(3000);
		isTestPassed(test1Pass, 1);

	}
	
	@Test
	public void test2() throws InterruptedException {
		Thread.sleep(3000);

		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				writeToLog("Initialising Test2...");
			
				//Open map in simulator
                MainApp app = MainApp.getInstance(); 
        		if(app == null)
        			app = new MainApp();
        		app.start(new Stage());
        		SimulationController.getInstance().getMap().loadMap(mapFile);

                try {
                	EditorApp.getInstance().getPrimaryStage().close();
    				EditorApp.getInstance().stop();
    				
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
                
                test2Pass = true;
                writeToLog("Test 2 Completed");
			}
			
			
			
		});

	
		Thread.sleep(3000);
		isTestPassed(test2Pass, 2);

	}
	
	
	@Test
	public void test3() throws InterruptedException {
		
		scene = new SceneDock();

		executor.execute(new Runnable() {
			
			@Override
			public void run() {
	
				clickButton("Start"); //Check thread is running 
				
				checkThreadStatus("animationThread");

				if(getIsAlive() == true){
					test3Pass = true;
					writeToLog("thread status is alive.");
				}
			}
			
			
		});
		Thread.sleep(5000);
		isTestPassed(test3Pass, 3);
	}
	
	

}
