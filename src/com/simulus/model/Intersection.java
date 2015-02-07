package com.simulus.model;

import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;

/**
 * Models and intersection of either three or four roads.
 */
public class Intersection extends Road {

	private int tickrate = 5000;
	private boolean northGreen, southGreen, westGreen, eastGreen;
	private static int count = 0;
	private int id;
	
	public Intersection() {
		super(Seed.INTERSECTION);
		this.id = count++;
	
		//Randomize tickrate [1000,10000)
		tickrate = (int) ((Math.random()*9000) + 1000);
		
		northGreen = true;
		southGreen = true;
		westGreen = false;
		eastGreen = false;
		
		Thread lightThread = new Thread("Intersection-"+id) {
			
			@Override
			public void run() {
				
				try {
					while(!isInterrupted()) {
						northGreen = !northGreen;
						southGreen = !southGreen;
						westGreen = !westGreen;
						eastGreen = !eastGreen;

						Map.getInstance().notifyLightSwitched();
						
						Thread.sleep(tickrate);
					}
					
				} catch(InterruptedException e) {
					e.printStackTrace();
				}				
			}
		};
		lightThread.start();
		
	}

	/**
	 * @param dir The direction to check for green light
	 * @return Whether the intersection allows a car to move in direction <code>dir</code>
	 */
	public boolean isGreen(Direction dir) {
		switch(dir) {
		case NORTH:
			return northGreen;
		case SOUTH:
			return southGreen;
		case WEST:
			return westGreen;
		case EAST:
			return eastGreen;	
		default:
			return true;
		}
	}

}
