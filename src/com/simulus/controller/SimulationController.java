package com.simulus.controller;

import javafx.application.Platform;

import com.simulus.MainApp;
import com.simulus.model.Map;
import com.simulus.model.listeners.MapUpdateListener;

public class SimulationController implements MapUpdateListener {
	
	public static int MAXCARS = 20;
	public static int TICKRATE = 1000; //tickrate in ms
	
	private Thread spinner;
	private Map map = Map.getInstance();
	private MainApp app = MainApp.getInstance();
	
	// Singleton pattern
	private static SimulationController instance;
	public static SimulationController getInstance() {
		
		if(instance == null){
			instance = new SimulationController();
		}
		
		return instance;
	}
	
	
	private SimulationController() {
		
		//Subscribe to map updates
		map.addMapUpdateListener(this);
		
		app.setGridSize(map.getMapSize());
		
		
		//Define the simulation
		spinner = new Thread() {
			
			@Override
			public void run() {
				
				while(!isInterrupted()) {
					
					if(map.getVehicleCount() < MAXCARS) {
						map.spawnRandomCar();
					}
				
					map.update();
					System.out.println("Tick");
					
					try {
						Thread.sleep(TICKRATE);
					} catch (InterruptedException e) {e.printStackTrace();}
					
				}
			}
		};	
		spinner.start();
	}

	@Override
	public void mapUpdated() {
		//TODO call the ui to execute all the changes.		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				app.redrawCars(map.getVehicleList());
			}
		});
		
	}

	@Override
	public void carSpawned(int x, int y, int laneId, int carId) {
		//TODO spawn car in ui
	}
	
	public void startSimulation() {
			
		spinner.start();

	}
	
	public void stopSimulation() {
		
		spinner.interrupt();
	}
	


}
