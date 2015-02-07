package com.simulus.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.simulus.model.listeners.MapUpdateListener;
import com.simulus.util.enums.Seed;

/**
 * Represents the street map through a grid of tiles. 
 * The grid is always a square. The point of origin (0,0) is in the top left corner,
 * values growing to the right and the bottom.
 */
public class Map {
	
	private static final int DEFAULT_GRIDSIZE = 30;
	
	private Tile[][] grid = null;
	
	/** Store all vehicles currently on the map */
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	private Object listMutex = new Object();

	/** A list of tiles that serve as spawnpoints for the vehicles. */
	private ArrayList<Tile> entryPoints = new ArrayList<Tile>();
	
	/** A list of all intersections in the current map. */
	private ArrayList<Intersection> intersections = new ArrayList<Intersection>(); 
	
	/** A list of all listener that want to be notified when the map is updated */
	private ArrayList<MapUpdateListener> listeners = new ArrayList<MapUpdateListener>();
	
	//Singleton pattern
	private static Map instance = null;
	/**
	 * @return 	The Map instance. If the map has not been initialised earlier, it return a map with size
	 * 			<code>DEFAULT_GRIDSIZE</code>. If a different mapsize is desired, it can be adjusted by 
	 * 			<code>setMapSize(int)</code>. 
	 */
	public static synchronized Map getInstance() {
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
	}
	
	/**
	 * Initialises the map to a basic intersection; roads crossing in the center of the map.
	 */
	private void initBasicIntersection() {
		
		for(int i=0; i<grid.length; i++) {
			
			if(i==6 || i==14 || i==23) {
				grid[i][6].content = new Intersection();
				grid[i][14].content = new Intersection();
				grid[i][23].content = new Intersection();
				
				intersections.add((Intersection) grid[i][6].content);
				intersections.add((Intersection) grid[i][14].content);
				intersections.add((Intersection) grid[i][23].content);
				continue;
			}
			
			grid[6][i].content = new Road(Seed.NORTHSOUTH);
			grid[i][6].content = new Road(Seed.WESTEAST);
			
			grid[14][i].content = new Road(Seed.NORTHSOUTH);
			grid[i][14].content = new Road(Seed.WESTEAST);
			
			grid[23][i].content = new Road(Seed.NORTHSOUTH);
			grid[i][23].content = new Road(Seed.WESTEAST);
		}
		
		//TODO Should entrypoints also specify the direction a car enters with, e.g. as property in Tile?
		entryPoints.add(grid[6][0]);	//Top
		entryPoints.add(grid[14][0]);
		entryPoints.add(grid[23][0]);
		
		entryPoints.add(grid[6][29]);	//Bottom
		entryPoints.add(grid[14][29]);
		entryPoints.add(grid[23][29]);

		entryPoints.add(grid[0][6]);	//Left
		entryPoints.add(grid[0][14]);
		entryPoints.add(grid[0][23]);
		
		entryPoints.add(grid[29][6]);	//Right
		entryPoints.add(grid[29][14]);
		entryPoints.add(grid[29][23]);
	}
	
	/**
	 * Is called on simulation tick. Updates all car positions and traffic light settings.
	 */
	public void update() {

		synchronized(listMutex) {
			for (Iterator<Vehicle> it = vehicles.iterator(); it.hasNext(); ) {
			    Vehicle v = it.next();
			    //If the car moves out of the grid, remove it from the map.
			    if (!v.moveForward()) {
			        it.remove();
			        v.getLane().setVehicle(null); //remove the vehicle from its current lane
			    }
			}
		}
		notifyMapUpdateListeners();
	}

	/**
	 * <code>Synchronized</code> because it is accessed by multiple threads, including the UI.
	 * @param x
	 * @param y
	 * @return the <code>Tile</code> at position (x,y), <code>null</code> if the grid does
	 * not contain a road or intersection at that position.
	 */
	public synchronized Tile getTile(int x, int y) {
		return grid[x][y];
	}
	
	public int getVehicleCount() {
		return this.vehicles.size();
	}
	
	public List<Vehicle> getVehicleList() {
		synchronized (listMutex) {
			return this.vehicles;
		}
	}
	
	public int getMapSize() {
		return this.grid.length;
	}

	/**
	 * Notifies all listeners that the map has been updated, i.e. the cars have been moved.
	 */
	private void notifyMapUpdateListeners() {
		
		for(MapUpdateListener l : listeners) {
			l.mapUpdated();
		}
	}
	
	/**
	 * Notifies all listeners that a traffic light has changed.
	 * <code>Synchronized</code> because every traffic light thread calls this method.
	 */
	public synchronized void notifyLightSwitched() {
		
		for(MapUpdateListener l : listeners) {
			l.lightSwitched();
		}
	}
	
	public void addMapUpdateListener(MapUpdateListener listener) {
		listeners.add(listener);
	}
}
