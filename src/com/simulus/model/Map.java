package com.simulus.model;

import java.util.ArrayList;

import com.simulus.model.listeners.MapUpdateListener;
import com.simulus.util.enums.Seed;

/**
 * Represents the street map through a grid of tiles. 
 * The grid is always a square. The point of origin (0,0) is in the top left corner,
 * values growing to the right and the bottom.
 */
public class Map {
	
	private static final int DEFAULT_GRIDSIZE = 10;
	
	private Tile[][] grid = null;
	
	/** Store all vehicles currently on the map */
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	/** A list of tiles that serve as spawnpoints for the vehicles. */
	private ArrayList<Tile> entryPoints = new ArrayList<Tile>();
	
	/** A list of all listener that want to be notified when the map is updated */
	private ArrayList<MapUpdateListener> listeners = new ArrayList<MapUpdateListener>();
	
	//Singleton pattern
	private static Map instance;
	/**
	 * @return 	The Map instance. If the map has not been initialised earlier, it return a map with size
	 * 			<code>DEFAULT_GRIDSIZE</code>. If a different mapsize is desired, it can be adjusted by 
	 * 			<code>setMapSize(int)</code>. 
	 */
	public static Map getInstance(){
		if(instance == null)
			instance = new Map(DEFAULT_GRIDSIZE);
		
		return instance;
	}
	

	/**
	 * @param gridSize The map's edge length. 
	 */
	private Map(int gridSize) {
		this.grid = new Tile[gridSize][gridSize];
		for(int i=0; i<gridSize; i++) {
			for(int j=0; j<gridSize; j++) {
			grid[i][j] = new Tile(i,j);
			}
		}
		
		initBasicIntersection();
	}
	
	/**
	 * Resizes the map to <code>size</code>. The map is reallocated, i.e. calling this will reset the map
	 * to its initial state!
	 * @param size The size of the new map
	 */
	public void setMapSize(int size) {
		grid = new Tile[size][size];
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
			grid[i][j] = new Tile(i,j);
			}
		}
		
		initBasicIntersection();
	}
	
	/**
	 * Spawns a car at a random entrypoint, facing a random direction (based on the tile's orientation).
	 */
	public void spawnRandomCar() {
		
		Tile ep;
		Lane lane = null;
		do {
			//Select random entrypoint
			int epIdx = (int)Math.round((Math.random() * (entryPoints.size()-1)));
			ep = entryPoints.get(epIdx);
			
			lane = ep.content.getEmptyLane();
			
		} while(lane == null);
			
		Car c = new Car(grid, ep.getxPos(), ep.getyPos(), lane);
		lane.setVehicle(c);
		vehicles.add(c);
		
		notifyCarSpawned(ep.getxPos(), ep.getyPos(), lane.getLaneId(), c.getId());
	}
	
	/**
	 * Initialises the map to a basic intersection; roads crossing in the center of the map.
	 */
	private void initBasicIntersection() {
		int mid = grid.length/2-1;
		
		for(int i=0; i<grid.length; i++) {
			
			if(i==mid) {
				grid[i][i].content = new Intersection();
			} else {
				grid[i][mid].content = new Road(Seed.NORTHSOUTH);
				grid[mid][i].content = new Road(Seed.WESTEAST);
			}
		}
		
		//Add entrypoints at each side.
		//TODO Should entrypoints also specify the direction a car enters with, e.g. as property in Tile?
		entryPoints.add(grid[0][mid]);  //North entry
		entryPoints.add(grid[mid][0]);  //West entry 
		entryPoints.add(grid[grid.length-1][mid]); //South entry
		entryPoints.add(grid[mid][grid.length-1]);	//East entry 
	}
	
	/**
	 * Is called on simulation tick. Updates all car positions and traffic light settings.
	 */
	public void update() {

		for(Vehicle v : vehicles) {
			v.moveForward(); // Need to implement a move method
			notifyMapUpdateListeners();
		}
		
	}

	/**
	 * @param x
	 * @param y
	 * @return the <code>Tile</code> at position (x,y), <code>null</code> if the grid does
	 * not contain a road or intersection at that position.
	 */
	public Tile getTile(int x, int y) {
		return grid[x][y];
	}
	
	public int getVehicleCount() {
		return this.vehicles.size();
	}
	

	/**
	 * Passes the current state of the grid to all listeners.
	 */
	private void notifyMapUpdateListeners() {
		
		for(MapUpdateListener l : listeners) {
			l.mapUpdated();
		}
		
	}
	
	/**
	 * Notify listeners of spawned car
	 */
	private void notifyCarSpawned(int x, int y, int laneId, int carId){
		
		for(MapUpdateListener l : listeners) {
			l.carSpawned(x, y, laneId, carId);
		}
			
			
	}
	
	public void addMapUpdateListener(MapUpdateListener listener) {
		listeners.add(listener);
	}
}
