package com.simulus.model;

import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;

/**
 * Models and intersection of either three or four roads.
 */
public class Intersection extends Road {

	private static int TICKRATE = 5000;
	private boolean northGreen, southGreen, westGreen, eastGreen;
	
	public Intersection() {
		super(Seed.INTERSECTION);
		
		northGreen = true;
		southGreen = true;
		westGreen = false;
		eastGreen = false;
		
		Thread lightThread = new Thread() {
			
			@Override
			public void run() {
				
				try {
					while(!isInterrupted()) {
						northGreen = !northGreen;
						southGreen = !southGreen;
						westGreen = !westGreen;
						eastGreen = !eastGreen;

						Map.getInstance().notifyLightSwitched();
						
						Thread.sleep(TICKRATE);
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
