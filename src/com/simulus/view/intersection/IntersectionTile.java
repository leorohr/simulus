package com.simulus.view.intersection;

import java.util.ArrayList;

import javafx.scene.paint.ImagePattern;

import com.simulus.util.ResourceBuilder;
import com.simulus.view.Tile;

public class IntersectionTile extends Tile {
	
	private ArrayList<CustomPath> turningPaths;
	private Intersection intersection;

	public IntersectionTile(double posX, double posY, double width,
			double height, int gridPosX, int gridPosY) {
		
		super(posX, posY, width, height, gridPosX, gridPosY);
		frame.setFill(new ImagePattern((ResourceBuilder.getBoxjunctionTexture())));
		
		//frame.setFill(Color.BLACK);
		turningPaths = new ArrayList<>();
	}
	
	/**
	 * @return The list of available paths for cars to take when they face this tile in an intersection.
	 */
	public ArrayList<CustomPath> getTurningPaths() {
		return turningPaths;			
	}

	public void setIntersection(Intersection i) {
		this.intersection = i;
	}
	
	public Intersection getIntersection() {
		return this.intersection;
	}	
}
