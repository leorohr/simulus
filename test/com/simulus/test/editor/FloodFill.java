package com.simulus.test.editor;

import javafx.application.Platform;

import org.jemmy.fx.SceneDock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simulus.EditorApp;
import com.simulus.test.TestCaseBaseCode;
import com.simulus.util.Configuration;
import com.simulus.view.Tile;
import com.simulus.view.map.Land;

public class FloodFill extends TestCaseBaseCode {

	private boolean test1Pass;

	private Tile[][] tiles = new Tile[Configuration.getGridSize()][Configuration.getGridSize()];



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

				EditorApp.getInstance().getFloodFill("grass", 1, 1);

				EditorApp.getInstance().getMap().drawMap(EditorApp.getInstance().getCanvas());

				for (int i = 0; i < tiles.length; i++) {
					for (int p = 0; p < tiles[0].length; p++) {
						Tile tile = EditorApp.getInstance().getMap().getTiles()[i][p];
						if( tile instanceof Land){
							test1Pass = true;
						}
						else{
							test1Pass=false;
						}
					}
				}
				

				writeToLog("Test 1 Completed");
			}
		});


		Thread.sleep(3000);
		isTestPassed(test1Pass, 1);


	}

}
