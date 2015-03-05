package com.simulus.view;

import java.util.ArrayList;

public class IntersectionTile extends Tile {
	
	private ArrayList<CustomPath> turningPaths;

	public IntersectionTile(double posX, double posY, double width,
			double height, int gridPosX, int gridPosY) {
		
		super(posX, posY, width, height, gridPosX, gridPosY);
		
		turningPaths = new ArrayList<>();
	}
	
	/**
	 * @return The list of available paths for cars to take when they face this tile in an intersection.
	 */
	public ArrayList<CustomPath> getTurningPaths() {
		return turningPaths;			
	}

	
	
}
