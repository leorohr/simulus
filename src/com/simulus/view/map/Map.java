package com.simulus.view.map;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import com.simulus.EditorApp;
import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.io.MapXML;
import com.simulus.util.Configuration;
import com.simulus.util.ResourceBuilder;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Orientation;
import com.simulus.util.enums.VehicleColorOption;
import com.simulus.view.Tile;
import com.simulus.view.TileGroup;
import com.simulus.view.intersection.CustomPath;
import com.simulus.view.intersection.Intersection;
import com.simulus.view.intersection.IntersectionTile;
import com.simulus.view.vehicles.Ambulance;
import com.simulus.view.vehicles.Car;
import com.simulus.view.vehicles.EmergencyCar;
import com.simulus.view.vehicles.Truck;
import com.simulus.view.vehicles.Vehicle;

public class Map extends Group {

	private int tileSize = Configuration.tileSize;
	private Tile[][] tiles = new Tile[Configuration.gridSize][Configuration.gridSize];
	private ArrayList<Intersection> intersections = new ArrayList<>();
	private ArrayList<Thread> trafficLightThreads = new ArrayList<>(); //the threads that switch the lights of trafficlights
	private ArrayList<Lane> entryPoints = new ArrayList<>();	//stores all those lanes, that are suitable to spawn a car on
	private ArrayList<Vehicle> vehicles = new ArrayList<>();
	private ArrayList<Vehicle> toBeRemoved = new ArrayList<>(); //temporarily stores vehicles that are off the canvas and should be removed with the next update 
	
	private VehicleColorOption carColorOption = VehicleColorOption.SPEED;
	private VehicleColorOption truckColorOption = VehicleColorOption.SPEED;

	public Map() {
		
		// init all tiles
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
				tiles[i][p] = new Tile(i * tileSize, p * tileSize, tileSize,
						tileSize, i, p);
			}
		}

		// Add map to canvas
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
				if(MainApp.getInstance() != null)
					MainApp.getInstance().getCanvas().getChildren().add(tiles[i][p]);
				else EditorApp.getInstance().getCanvas().getChildren().add(tiles[i][p]);
			}
		}
		
		
		for(Intersection i: intersections){
			i.addTurningPaths(tiles);
			
			//If the path has a lane at the end of it, set it to active
			//This allows the creation intersections without 4 connected roads
			for(CustomPath p: i.getTurningPaths()){
				if(p.getEndTile() instanceof Lane)
					p.setActive(true);
			}
			Thread t = new Thread(i, "Intersection <" 	+ i.getTiles().get(0).getGridPosX() + ", "
														+ i.getTiles().get(0).getGridPosY() + ">");
			trafficLightThreads.add(t);
			t.start();
		}
	}

	public Map(Tile[][] tiles) {
		this();
		
		this.tiles = tiles;
	}
	

	/**
	 * Draws the current state of the map on either the editor's or the mainApp's canvas.
	 */
	public void drawMap() {
		if(MainApp.getInstance() != null)
			MainApp.getInstance().getCanvas().getChildren().clear();
		 if(EditorApp.getInstance() != null)
			EditorApp.getInstance().getCanvas().getChildren().clear();
		
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {

				if (MainApp.getInstance() != null) {
					MainApp.getInstance().getCanvas().getChildren()
							.add(tiles[i][p]);
				}
				if (EditorApp.getInstance() != null) {
					EditorApp.getInstance().getCanvas().getChildren()
							.add(tiles[i][p]);
				}
			}
		}
	}
	
	private void createHashStyleMap() {
		// init all tiles
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
				tiles[i][p] = new Tile(i * tileSize, p * tileSize, tileSize,
						tileSize, i, p);
			}
		}

		for (int i = 0; i < tiles.length; i++) {
			addGroup(new Road(i, 7, Orientation.WESTEAST));
			addGroup(new Road(7, i, Orientation.NORTHSOUTH));

			addGroup(new Road(i, 18, Orientation.WESTEAST));
			addGroup(new Road(18, i, Orientation.NORTHSOUTH));

			addGroup(new Road(i, 29, Orientation.WESTEAST));
			addGroup(new Road(29, i, Orientation.NORTHSOUTH));
		}

		addGroup(new Intersection(7, 7));
		addGroup(new Intersection(7, 18));
		addGroup(new Intersection(7, 29));

		addGroup(new Intersection(18, 7));
		addGroup(new Intersection(18, 18));
		addGroup(new Intersection(18, 29));

		addGroup(new Intersection(29, 7));
		addGroup(new Intersection(29, 18));
		addGroup(new Intersection(29, 29));
	}

	/**
	 * Spawns a car at a randomly selected entrypoint of the map.
	 * @param b The desired behavior of the spawning car.
	 */
	public void spawnRandomCar(Behavior b) {
		Lane l;
		if ((l = selectRandomEntryPoint()) == null)
			return;

		// Car adds itself to the canvas
		Car c = null;
		if (l.getDirection() == Direction.NORTH
				|| l.getDirection() == Direction.SOUTH) {
			c = new Car(l.getGridPosX() * tileSize + tileSize / 2
					- Car.CARWIDTH / 2, l.getGridPosY() * tileSize
					+ tileSize - Car.CARLENGTH, l.getDirection());
		} else if (l.getDirection() == Direction.WEST
				|| l.getDirection() == Direction.EAST) {
			c = new Car(l.getGridPosX() * tileSize, l.getGridPosY()
					* tileSize + tileSize / 2 - Car.CARWIDTH / 2,
					l.getDirection());
		}

		c.setCurrentTile(l);
		c.setMap(tiles);
		c.setBehavior(b);
		l.setOccupied(true, c);
		synchronized (vehicles) {
			vehicles.add(c);
		}
	}

	/**
	 * Spawns a truck at a randomly selected entrypoint of the map.
	 */
	public void spawnRandomTruck() {
		Lane l;
		if ((l = selectRandomEntryPoint()) == null)
			return;

		// Truck adds itself to the canvas
		Truck t = null;
		if (l.getDirection() == Direction.NORTH
				|| l.getDirection() == Direction.SOUTH) {
			t = new Truck(l.getGridPosX() * tileSize + tileSize / 2
					- Truck.TRUCKWIDTH / 2, l.getGridPosY() * tileSize
					+ tileSize - Truck.TRUCKLENGTH, l.getDirection());
		} else if (l.getDirection() == Direction.WEST
				|| l.getDirection() == Direction.EAST) {
			t = new Truck(l.getGridPosX() * tileSize, l.getGridPosY()
					* tileSize + tileSize / 2 - Truck.TRUCKWIDTH / 2,
					l.getDirection());
		}

		t.setCurrentTile(l);
		t.setMap(tiles);
		l.setOccupied(true, t);
		synchronized (vehicles) {
			vehicles.add(t);
		}
	}

	/**
	 * Adds the passed tile to the tiles[][] array.
	 * 
	 * @param tile - The tile to be added
	 */
	public void addSingle(Tile tile) {
		tiles[tile.getGridPosX()][tile.getGridPosY()] = tile;
	}
	
	/**
	 * Removes the passed tile from the tiles[][] array.
	 * 
	 * @param tile - The tile to be removed
	 */
	public void removeSingle(Tile tile) {
		tiles[tile.getGridPosX()][tile.getGridPosY()] = new Tile(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), tile.getGridPosX(), tile.getGridPosY());
	}

	/**
	 * @return A random entrypoint (Lane) or <code>null</code> if no free
	 *         entrypoint has been found after a certain number of tries.
	 */
	public Lane selectRandomEntryPoint() {
		Random rand = new Random();
		Lane l;
		int tries = 0;
		do {
			if (++tries > entryPoints.size()) // break if 70% of the entrypoints
												// were tried unsuccessfully
				return null;

			l = entryPoints.get(rand.nextInt(entryPoints.size()));
		} while (l.isOccupied());

		return l;
	}

	//for testing
	public void spawnTesterCar(double speed) {

		Lane a = entryPoints.get(0);

		Car c = null;
		// Car adds itself to the canvas
		c = new Car(a.getGridPosX() * tileSize + Car.CARWIDTH / 4,
				a.getGridPosY() * tileSize + Car.CARLENGTH / 8,
				a.getDirection());

		c.setCurrentTile(a);
		c.setMap(tiles);
		c.setVehicleSpeed(speed);
		a.setOccupied(true, c);
		synchronized (vehicles) {
			vehicles.add(c);
		}

	}

	/**
	 * Spawns an ambulance at a random, available entrypoint.
	 */
	public void spawnAmbulance(){
		
		Lane l;
		do{
			l = selectRandomEntryPoint();
		}
		while( l != null && l.getLaneNo() != 1 && l.getLaneNo() != 2);
		
		Ambulance a = null;
		
		if (l.getDirection() == Direction.NORTH
				|| l.getDirection() == Direction.SOUTH) {
			a = new Ambulance(l.getGridPosX() * tileSize + tileSize / 2
					- Car.CARWIDTH / 2, l.getGridPosY() * tileSize
					+ tileSize - Car.CARLENGTH, l.getDirection());
		} else if (l.getDirection() == Direction.WEST
				|| l.getDirection() == Direction.EAST) {
			a = new Ambulance(l.getGridPosX() * tileSize, l.getGridPosY()
					* tileSize + tileSize / 2 - Car.CARWIDTH / 2,
					l.getDirection());
		}
		a.getCar().setCurrentTile(l); 
		a.getCar().setMap(tiles);
		l.setOccupied(true, a.getCar());
		
		synchronized (vehicles){
			vehicles.add(a.getCar());
		}
	}

	/**
	 * Add the tiles contained by the passed tilegroup to the current instance of the map.
	 * If a North/Southbound lane is at the top/bottom-edge of the map, it becomes an entrypoint,
	 * equivalently do East/Westbound  lanes at the left/right-edge of the map.
	 * @param g The TileGroup to add to the map's tiles.
	 */
	public void addGroup(TileGroup g) {

		List<Tile> l = g.getTiles();
		for (Tile t : l) {
			tiles[t.getGridPosX()][t.getGridPosY()] = t;
			
			if(g instanceof Intersection) {
				((IntersectionTile)tiles[t.getGridPosX()][t.getGridPosY()]).setIntersection((Intersection)g);
			}

			if (g instanceof Road) {
				if (t.getGridPosX() == tiles.length - 1
						&& ((Road) g).getOrientation() == Orientation.WESTEAST
						&& ((Lane) t).getDirection() == Direction.WEST) {
					entryPoints.add((Lane) t);
				} else if (t.getGridPosX() == 0
						&& ((Road) g).getOrientation() == Orientation.WESTEAST
						&& ((Lane) t).getDirection() == Direction.EAST) {
					entryPoints.add((Lane) t);
				} else if (t.getGridPosY() == tiles.length - 1
						&& ((Road) g).getOrientation() == Orientation.NORTHSOUTH
						&& ((Lane) t).getDirection() == Direction.NORTH) {
					entryPoints.add((Lane) t);
				} else if (t.getGridPosY() == 0
						&& ((Road) g).getOrientation() == Orientation.NORTHSOUTH
						&& ((Lane) t).getDirection() == Direction.SOUTH) {
					entryPoints.add((Lane) t);
				}
			}
		}
		
		if(g instanceof Intersection)
			intersections.add((Intersection) g);
	}


	public void setRedTrafficLight(int tileX, int tileY) {
		tiles[tileX][tileY].setOccupied(true);
        tiles[tileX][tileY].getFrame().setFill(Color.RED);
	}
	
	public void setGreenTrafficLight(int tileX, int tileY){
		tiles[tileX][tileY].setOccupied(false);
        tiles[tileX][tileY].getFrame().setFill(
                new ImagePattern((((Lane) tiles[tileX][tileY]).getDirection() == Direction.EAST || ((Lane) tiles[tileX][tileY]).getDirection() == Direction.WEST ?
                ResourceBuilder.getEWLaneTexture() : ResourceBuilder.getNSLaneTexture())));
	}

	/**
	 * Computes one simulation step. Is executed with each tick.
	 */
	public void updateMap() {

		Vehicle v;
		Iterator<Vehicle> iter = vehicles.iterator();
		while (iter.hasNext()) {

			v = iter.next();
			int vX = v.getCurrentTile().getGridPosX();
			int vY = v.getCurrentTile().getGridPosY();
			
			
			Tile nextTile = null;

			// In order to prevent ConcurrentModificationExceptions
			// vehicles have to be removed in this loop.
			if (toBeRemoved.contains(v)) {
				iter.remove();
                toBeRemoved.remove(v);
				continue;
			}

			switch (v.getDirection()) {
			case NORTH:
				if (tiles[vX][vY - 1].getFrame().intersects(
						v.getBoundsInParent())) {
					nextTile = tiles[vX][vY - 1];
				}
				break;
			case EAST:
				if (tiles[vX + 1][vY].getFrame().intersects(
						v.getBoundsInParent())) {
					nextTile = tiles[vX + 1][vY];
				}
				break;
			case SOUTH:
				if (tiles[vX][vY + 1].getFrame().intersects(
						v.getBoundsInParent())) {
					nextTile = tiles[vX][vY + 1];
				}
				break;
			case WEST:
				if (tiles[vX - 1][vY].getFrame().intersects(
						v.getBoundsInParent())) {
					nextTile = tiles[vX - 1][vY];
				}
				break;
			case NONE:
				break;
			}

			if (nextTile != null) { // if the car is intersecting its front tile
				// Free tiles
				Iterator<Tile> i = v.getOccupiedTiles().iterator();
				while (i.hasNext()) {
					Tile t = (Tile) i.next();
					if (!v.getBoundsInParent().intersects(
							t.getFrame().getBoundsInParent())) {
						if(!t.isRedLight()){
							t.setOccupied(false, v);
							i.remove();
						}
					}
				}

				nextTile.setOccupied(true, v );
				v.setCurrentTile(nextTile);
				v.setMap(tiles);

			}
			
			updateVehicleColor(v);
			v.moveVehicle();
		}
	}

	/*
	 *  TODO: shrink method, remove redundant/repeat code
	 *  	  have a remove method that handles single square and groups
	 */
	public void removeGroup(TileGroup g) {

		List<Tile> l = g.getTiles();
		for (Tile t : l) {
			tiles[t.getGridPosX()][t.getGridPosY()] = new Tile(t.getX(), t.getY(), t.getWidth(), t.getHeight(), t.getGridPosX(), t.getGridPosY());

			if (g instanceof Road) {
				if (t.getGridPosX() == tiles.length - 1
						&& ((Road) g).getOrientation() == Orientation.WESTEAST
						&& ((Lane) t).getDirection() == Direction.WEST) {
					entryPoints.remove((Lane) t);
				} else if (t.getGridPosX() == 0
						&& ((Road) g).getOrientation() == Orientation.WESTEAST
						&& ((Lane) t).getDirection() == Direction.EAST) {
					entryPoints.remove((Lane) t);
				} else if (t.getGridPosY() == tiles.length - 1
						&& ((Road) g).getOrientation() == Orientation.NORTHSOUTH
						&& ((Lane) t).getDirection() == Direction.NORTH) {
					entryPoints.remove((Lane) t);
				} else if (t.getGridPosY() == 0
						&& ((Road) g).getOrientation() == Orientation.NORTHSOUTH
						&& ((Lane) t).getDirection() == Direction.SOUTH) {
					entryPoints.remove((Lane) t);
				}
			}
		}

		if (g instanceof Intersection)
			intersections.remove((Intersection) g);
	}
	
    /**
     * Loads a Map from an XML file.
     * @param mapFile The XML file containing the map-data.
     */
    public void loadMap(File mapFile) {
    	SimulationController.getInstance().resetSimulation(false);
    	SimulationController.getInstance().setLastLoadedMap(mapFile);
        tiles = new Tile[Configuration.gridSize][Configuration.gridSize];
        entryPoints = new ArrayList<Lane>();
        intersections = new ArrayList<Intersection>();
        stopChildThreads();
        trafficLightThreads = new ArrayList<Thread>();
        vehicles = new ArrayList<Vehicle>();
        toBeRemoved = new ArrayList<Vehicle>();
    	
    	MapXML loader = new MapXML();
        loader.readXML(mapFile.toPath().toString());
        tiles = loader.getTileGrid();
        
        boolean[][] checked = new boolean[tiles.length][tiles[0].length];
        for(int i=0; i<tiles.length; i++) {
        	for(int j=0; j<tiles[0].length; j++) {
        		//Dont double-check tiles -- mainly for correct detection of intersections.
        		if(checked[i][j])
        			continue;
        		
        		//Check for entrypoints
        		if(i==0) {
        			if(tiles[i][j] instanceof Lane && ((Lane)tiles[i][j]).getDirection() == Direction.EAST)
        				entryPoints.add((Lane) tiles[i][j]);
        		} else if(i == tiles.length-1) {
        			if(tiles[i][j] instanceof Lane && ((Lane)tiles[i][j]).getDirection() == Direction.WEST)
        				entryPoints.add((Lane) tiles[i][j]);
        		} else if(j == 0) {
        			if(tiles[i][j] instanceof Lane && ((Lane)tiles[i][j]).getDirection() == Direction.SOUTH)
        				entryPoints.add((Lane) tiles[i][j]);
        		} else if(j == tiles.length-1) {
        			if(tiles[i][j] instanceof Lane && ((Lane)tiles[i][j]).getDirection() == Direction.NORTH)
        				entryPoints.add((Lane) tiles[i][j]);
        		}
        		
        		if(tiles[i][j] instanceof IntersectionTile) {
        			//If an intersection is encountered, create new object and set all related tiles to checked
        			addGroup(new Intersection(i, j));
        			for(int m=i; m<i+4; m++) {
        				for(int n=j; n<j+4; n++) 
        					checked[m][n] = true;
        			}
        		}
        	}
        }
        
        for(Intersection i: intersections){
			i.addTurningPaths(tiles);
			
			//If the path has a lane at the end of it, set it to active
			//This allows the creation intersections without 4 connected roads
			for(CustomPath p: i.getTurningPaths()){
				if(p.getEndTile() instanceof Lane)
					p.setActive(true);
			}
			Thread t = new Thread(i, "Intersection <" 	+ i.getTiles().get(0).getGridPosX() + ", "
														+ i.getTiles().get(0).getGridPosY() + ">");
			trafficLightThreads.add(t);
			t.start();
		}
        
        drawMap();
    }

	/**
	 * Removes a vehicle from the screen. To prevent ConcurrentModification-Exceptions, the 
	 * vehicles that shold be removed are temporarily stored in <code>toBeRemoved</code> and 
	 * then removed in the main simulation loop.
	 * @param v Vehicle to be removed
	 */
	public void removeVehicle(Vehicle v) {

		v.removeFromCanvas();
		v.getCurrentTile().setOccupied(false, v);

		for (Tile t : v.getOccupiedTiles())
			t.setOccupied(false, v);

		toBeRemoved.add(v);
	}

	/**
	 * Changes the color of the vehicle according to the currently chosen coloroption.
	 * @param v The vehicle whose color should be updated.
	 */
	private void updateVehicleColor(Vehicle v) {
		
		if(v instanceof Car && !(v instanceof EmergencyCar)) {
			switch(carColorOption) {
			case BEHAVIOR: 
				if(v.getBehavior() == Behavior.RECKLESS)
					v.setFill(Color.RED);
				else if(v.getBehavior() == Behavior.CAUTIOUS) 
					v.setFill(Color.AQUAMARINE);
				break;
			case SPEED:
				//If a car is standing, color it green, if it is driving with the max. allowed speed, color it red.
				double maxSpeedInMps = ((double)SimulationController.getInstance().getMaxCarSpeed()*1000)/3600;
				double speedfraction = v.getVehicleSpeed()/((maxSpeedInMps * (Configuration.tileSize/5))/10);
				v.setFill(Color.hsb(120.0d * (speedfraction > 1 ? 1 : speedfraction), 1.0d, 1.0d)); //Hue degree 120 is bright green, 0 is red
				break;
			case USER:
	    		v.setFill(MainApp.getInstance().getControlsController().getCarColor());
				break;
			default:
				break;
			}
		} else if(v instanceof Truck) {
			switch(truckColorOption){
			case BEHAVIOR:
				if(v.getBehavior() == Behavior.RECKLESS)
					v.setFill(Color.RED);
				else if(v.getBehavior() == Behavior.CAUTIOUS) 
					v.setFill(Color.AQUAMARINE);
				break;
			case SPEED:
				double maxSpeedInMps = ((double)SimulationController.getInstance().getMaxCarSpeed()*1000)/3600;
				double speedfraction = v.getVehicleSpeed()/((maxSpeedInMps * (Configuration.tileSize/5))/10);
				v.setFill(Color.hsb(120.0d * speedfraction, 1.0d, 1.0d)); //Hue degree 120 is bright green, 0 is red
				break;
			case USER:
				v.setFill(MainApp.getInstance().getControlsController().getTruckColor());
				break;
			default:
				break;
			}
		} 
	}
	
	/**
	 * Give all intersections a new random switchtime between 2 and 5 secs.
	 */
	public void randomiseTrafficLights() {
		for(Intersection i : intersections) {
			i.setSwitchTime((long) (2000 + Math.random()*3000));
		}
	}
	
	public void showAllIntersectionPaths() {
		for (Intersection is : intersections)
			is.showAllPaths();
	}

	public void hideAllIntersectionsPaths() {
		for (Intersection is : intersections) {
			is.hideAllPaths();
		}
	}
	
	/**
	 * @return The average speed of all vehicles currently on the map.
	 */
	public double getAverageSpeed() {
		
		double avg = 0.0d;
		for(Vehicle v : vehicles)
			avg += v.getVehicleSpeed()*(50/Configuration.tileSize)*3.6;
	
		return avg/vehicles.size();
	}
	
	/**
	 * @return The percentage of road-tiles that are currently occupied [0.0;1.0]
	 */
	public double getCongestionValue() {
		double occ = 0.0d;
		double road = 0.0d;
		
		for(Tile[] ts : tiles) {
			for(Tile t : ts) {
				if(t instanceof Lane)
					road++;
				if(t.isOccupied())
					occ++;
			}
		}
		return occ/road;
	}
	
	/**
	 * @return The average number of ticks in which a vehicle did not move on its way through the map.
	 */
	public double getAvgWaitingTime() {
		
		double avg = 0.0d;
		for(Vehicle v : vehicles) {
			avg += v.getWaitedCounter();
		}
		
		return avg/vehicles.size();
	}
	
	/**
	 * @return The average number of ticks in which an emergency vehicle did not move on its way through the map.
	 */
	public double getAvgEmWaitingTime() {
		
		double avg = 0.0d;
		int count = 0;
		for(Vehicle v : vehicles) {
			if(v instanceof EmergencyCar) {
				avg += v.getWaitedCounter();
				count++;
			}
		}
		
		return avg/count;
	}
	
	/**
	 * Interrupts all trafficLightThreads that are spawned by the map.
	 * Is used when the simulation is restart.
	 */
	public void stopChildThreads() {

		for(Thread t : trafficLightThreads) {
			t.interrupt();
		}
	}

	public Tile[][] getTiles() {
		return tiles;
	}
	
	public ArrayList<Vehicle> getVehicles(){
		return vehicles;
	}

	public int getVehicleCount() {
		return vehicles.size();
	}
	
	public void setCarColorOption(VehicleColorOption o) {
    	this.carColorOption = o;
    }
	
	public void setTruckColorOption(VehicleColorOption o) {
		this.truckColorOption = o;
	}	
	public ArrayList<Intersection> getIntersections(){
		return intersections;
	}
	
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}
}
