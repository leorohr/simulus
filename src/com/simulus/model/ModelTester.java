package com.simulus.model;


/*
 * For development purposes only.
 */
public class ModelTester {
	
	public static int MAXCARS = 20;
	public static int TICKRATE = 1000; //tickrate in ms
	
	public static void main(String[] args) {
		
		final Map map = new Map(10);
		
		Thread spinner = new Thread() {
			
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
		
		spinner.start();
		
	}
	
	
}
