package com.simulus.model;

import java.util.ArrayList;

import com.simulus.model.enums.Orientation;

/**
 * Represents the street map through a grid of tiles. 
 * The grid is always a square. The point of origin (0,0) is in the top left corner,
 * values growing to the right and the bottom.
 */
public class Map {
	
	private Tile[][] grid = null;
	
	/** Store all vehicles currently on the map */
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	/** A list of tiles that serve as spawnpoints for the vehicles. */
	private ArrayList<Road> entryPoints = new ArrayList<Road>();
	
	/**
	 * Spawns a car at a random entrypoint, facing a random direction (based on the tile's orientation).
	 */
	public void spawnRandomCar() {
		
		Road ep;
		do {
			int epIdx = (int) Math.round(Math.random()*entryPoints.size());
			ep = entryPoints.get(epIdx);
		} while(ep.getVehicle() != null);
		
		Car c = new Car(ep, ep.getOrientation().randomDirection());
		ep.setVehicle(c);
		vehicles.add(c);
	}

	/**
	 * @param gridSize The map's edge length. 
	 */
	public Map(int gridSize) {
		this.grid = new Tile[gridSize][gridSize];
		
		initBasicIntersection();
	}
	
	/**
	 * Initialises the map to a basic intersection; roads crossing in the center of the map.
	 */
	private void initBasicIntersection() {
		int mid = grid.length/2;
		
		for(int i=0; i<grid.length; i++) {
			
			if(i==mid) {
				grid[i][i] = new Intersection();
			} else {
				grid[i][mid] = new Road(Orientation.NORTHSOUTH);
				grid[mid][i] = new Road(Orientation.EASTWEST);
			}
		}
		
		//Add entrypoints at each side.
		//TODO Should entrypoints also specify the direction a car enters with, e.g. as property in Tile?
		entryPoints.add((Road) grid[0][mid]);
		entryPoints.add((Road) grid[mid][0]);
		entryPoints.add((Road) grid[grid.length][mid]);
		entryPoints.add((Road) grid[mid][grid.length]);		
	}
	
	/**
	 * Is called on simulation tick. Updates all car positions and traffic light settings.
	 */
	public void update() {

		for(Vehicle v : vehicles) {
			//TODO move all vehicles in the list.
		}
		
	}
	
	/**
	 * @param x
	 * @param y
	 * @return the <code>Tile</code> at position (x,y), <code>null</code> if the grid does
	 * not contain a road or intersection at that position.
	 */
	public Tile getTile(int x, int y) {
		return (grid[x][y] instanceof Tile ? grid[x][y] : null);
	}
	
	public int getVehicleCount() {
		return this.vehicles.size();
	}
}