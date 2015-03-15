package com.simulus.view.intersection;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

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
		
		turningPaths = new ArrayList<>();
	}
	
	/**
	 * @return A randomly chosen active path contained by this tile.
	 */
	public CustomPath getRandomPath() {
		Vector<CustomPath> v = new Vector<>();
		for(CustomPath p : turningPaths)
			if(p.getActive())
				v.add(p);
		
		return v.get(new Random().nextInt(v.size()));
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
