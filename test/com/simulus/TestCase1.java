package com.simulus;

import static org.junit.Assert.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;

import org.jemmy.fx.AppExecutor;
import org.jemmy.fx.SceneDock;
import org.jemmy.fx.control.LabeledDock;
import org.jemmy.resources.StringComparePolicy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sun.launcher.resources.launcher;



public class TestCase1 {

	private static SceneDock scene;
	private final  Executor executor = Executors.newSingleThreadExecutor();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AppExecutor.executeNoBlock(MainApp.class);
		Thread.sleep(1000);
		scene = new SceneDock();
	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		
		Thread.sleep(10000);
		
	}

	private void runClickStartButtonThread(){
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				new LabeledDock(scene.asParent(), "Start", StringComparePolicy.EXACT).mouse().click();
			}
		});
	}
	
	@Test
	public void test() {
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
			
				runClickStartButtonThread();
			}
		});
		
	}

}
