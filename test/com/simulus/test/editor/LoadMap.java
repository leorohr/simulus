package com.simulus.test.editor;

import static org.junit.Assert.*;

import java.io.File;

import javafx.application.Platform;

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
import com.simulus.view.map.Map;

public class LoadMap extends TestCaseBaseCode {
	
	private boolean test1Pass;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();
		writeToFile("test/com/simulus/result/LoadMap.txt", true);
		writeLog.flush();
		writeLog.WriteToLog("FX App thread started \n");

		executor.execute(new Runnable() {

			@Override
			public void run() {
				clickButton("Editor");

			}
		});

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InterruptedException {
		Thread.sleep(2000);

		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				writeToLog("Initialising Test1...");
				Tile[][] mapTiles = EditorApp.getInstance().getMap().getTiles();
				
				
			EditorApp.getInstance().loadMap(EditorControlsController.class.getResource("/resources/maps/Road_Works.map").getPath());
			
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

}
