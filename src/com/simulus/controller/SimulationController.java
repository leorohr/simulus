package com.simulus.controller;

import com.simulus.model.Map;
import com.simulus.model.Tile;
import com.simulus.model.listeners.MapUpdateListener;

public class SimulationController implements MapUpdateListener {
	
	public static int MAXCARS = 20;
	public static int TICKRATE = 1000; //tickrate in ms
	
	private Thread spinner;
	private Map map; //TODO maybe Map should be a singleton?
	
	// Singleton pattern
	private static SimulationController instance;
	public static SimulationController getInstance() {
		
		if(instance == null){
			instance = new SimulationController();
		}
		
		return instance;
	}
	
	
	private SimulationController() {
		
		spinner = new Thread() {
			
			@Override
			public void run() {
				
				while(!isInterrupted()) {
					
					if(map.getVehicleCount() < MAXCARS) {
						map.spawnRandomCar();
						map.update();
					}
				
					try {
						Thread.sleep(TICKRATE);
					} catch (InterruptedException e) {e.printStackTrace();}
					
					map.update();
				}
			}
		};
		
		
	}

	@Override
	public void mapUpdated(Tile[][] currentMap) {
		//TODO call the ui to execute all the changes.

	}
	
	public void startSimulation() {
			
		spinner.start();

	}
	
	public void stopSimulation() {
		
		spinner.interrupt();
	}

}
