package com.simulus.test;

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

public class MapEditor_loadMap extends TestCaseBaseCode {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		appThread.launchApp();
		Thread.sleep(1000);
		scene = new SceneDock();
		writeToFile("test/com/simulus/result/MapEditor.txt", true);
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
		Thread.sleep(5000);

		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {

				EditorApp.getInstance().loadMap(EditorControlsController.class.getResource("/resources/maps/Road_Works.map").getPath());

				
			}
		});

	

		Thread.sleep(5000);


	}

}
