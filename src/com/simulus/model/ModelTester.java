package com.simulus.model;

/*
 * For development purposes only.
 */
public class ModelTester {
	
	public static int MAXCARS = 20;
	public static int TICKRATE = 500; //tickrate in ms
	
	public static void main(String[] args) {
		
		final Map map = new Map(10);
		
		map.printGrid();
		
		Runnable spinner = new Runnable() {
			
			@Override
			public void run() {
				
				if(map.getVehicleCount() < MAXCARS) {
					map.spawnRandomCar();
				}
				
				try {
					Thread.sleep(TICKRATE);
				} catch (InterruptedException e) {e.printStackTrace();}
				
				map.update();
			}
		};
		
		spinner.run();
		
	}
	
	
}
