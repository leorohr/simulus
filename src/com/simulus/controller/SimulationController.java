package com.simulus.controller;

import javafx.application.Platform;

import com.simulus.MainApp;
import com.simulus.model.Intersection;
import com.simulus.model.Map;
import com.simulus.model.listeners.MapUpdateListener;

public class SimulationController implements MapUpdateListener {
	
	public static int MAXCARS = 400;
	public static int TICKRATE = 50; //tickrate in ms
	
	private Thread spinner;
	private Map map;
	private MainApp app;
	
	// Singleton pattern
	private static SimulationController instance;
	public static SimulationController getInstance() {
		
		if(instance == null){
			instance = new SimulationController();
		}
		
		return instance;
	}
	
	
	private SimulationController() {
		 map = Map.getInstance();
		 app = MainApp.getInstance();
		
		//Subscribe to map updates
		map.addMapUpdateListener(this);
		
		//Initialise UI
		app.setGridSize(map.getMapSize());
		Platform.runLater(() -> app.readMap());
		
		//Define the simulation
		spinner = new SimulationThread();
	}

	@Override
	public void mapUpdated() {
		Platform.runLater(() -> {
				app.redrawCars(map.getVehicleList());
			});
	}
	
	public void startSimulation() {
		if(!spinner.isInterrupted()) {
			spinner = new SimulationThread();
			spinner.start();
		
		if(!spinner.isAlive())
			spinner.start();
		}
	}
	
	public void stopSimulation() {
		
		spinner.interrupt();
	}
	
	@Override
	public void lightSwitched(Intersection is) {
		app.switchLights(is);
	}
	
	private class SimulationThread extends Thread {
		@Override
		public void run() {
			
			while(!this.isInterrupted()) {
				
				if(map.getVehicleCount() < MAXCARS) {
					map.spawnRandomCar();
				}
			
				map.update();
				
				try {
					Thread.sleep(TICKRATE);
				} catch (InterruptedException e) {
					e.printStackTrace();
					this.interrupt();
				}
				
			}
		}
	}
}
