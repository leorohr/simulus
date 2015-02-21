package com.simulus.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.Group;

import com.simulus.MainApp;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;

public class Map extends Group{
	
	private static final int NUM_TILES = 30;
	private static final int CANVAS_SIZE_PX = 900;
	public static final int TILESIZE = CANVAS_SIZE_PX/NUM_TILES;
	
	private Tile[][] tiles = new Tile[NUM_TILES][NUM_TILES];
	private ArrayList<Intersection> intersections = new ArrayList<Intersection>();
	private ArrayList<Lane> entryPoints = new ArrayList<Lane>();
	
	public Map() {		
		
		createBasicMap();
		
		//Add map to canvas
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {
				MainApp.getInstance().getCanvas().getChildren().add(tiles[i][p]);
			}
		}
		
	}
	
	public void readMap(){
		//TODO Read map from XML
	}
	
	//Hardcoded map for testing
	private void createBasicMap() {
			
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {
				tiles[i][p] = new Tile(	i * TILESIZE, p * TILESIZE,
										TILESIZE, TILESIZE,
										i, p);
			}
		}
		
		for(int i=0; i<30; i++) {
			addGroup(new Road(13, i, Seed.NORTHSOUTH));
			addGroup(new Road(i, 13, Seed.WESTEAST));
		}
		addGroup(new Intersection(13, 13));
	}
	
	/**
	 * Spawns a car at a randomly selected entrypoint of the map.
	 */
	public void spawnRandomCar() {
		Random rand = new Random();
		Lane l = entryPoints.get(rand.nextInt(entryPoints.size()));
		
		//Car adds itself to the canvas
		new Car(l.getGridPosX() * Map.TILESIZE + Car.CARWIDTH / 4,
				l.getGridPosY() * Map.TILESIZE + Car.CARHEIGHT / 8,
				l.getDirection());	
	}
	
	public void addGroup(TileGroup g) {
		
		List<Tile> l = g.getTiles();
		for(Tile t : l) {
			tiles[t.getGridPosX()][t.getGridPosY()] = t;
			
			if(g instanceof Road) {
				if(t.getGridPosX() == tiles.length-1 && ((Road) g).getOrientation() == Seed.WESTEAST && ((Lane) t).getDirection() == Direction.WEST) {
					entryPoints.add((Lane)t);
				}
				else if(t.getGridPosX() == 0 && ((Road) g).getOrientation() == Seed.WESTEAST && ((Lane) t).getDirection() == Direction.EAST) {
					entryPoints.add((Lane)t);
				}
				else if(t.getGridPosY() == tiles.length-1 && ((Road) g).getOrientation() == Seed.NORTHSOUTH && ((Lane) t).getDirection() == Direction.NORTH) {
					entryPoints.add((Lane)t);
				}
				else if(t.getGridPosY() == 0 && ((Road) g).getOrientation() == Seed.NORTHSOUTH && ((Lane) t).getDirection() == Direction.SOUTH) {
					entryPoints.add((Lane)t);
				}
			}	
		}
		
		
		if(g instanceof Intersection)
			intersections.add((Intersection)g);
	}
	
	/**
	 * Used only for testing purposes Simulates the effect of traffic lights on
	 * cars
	 */
	public void createBlockage(int frameNo, int length, int tileX, int tileY) {
		if (frameNo < length)
			tiles[tileX][tileY].setOccupied(true);
		else
			tiles[tileX][tileY].setOccupied(false);
	}

	/**
	 * Updates the map according to the vehicle passed in. Gives the vehicle a
	 * copy of the updated map
	 * 
	 * @param c Vehicle
	 */
	public void updateMap(Vehicle c) {

		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {
				if (tiles[i][p].intersects(c.getBoundsInParent())) {
					tiles[i][p].setOccupied(true, c);
					c.setCurrentTile(tiles[i][p]);
					c.addTile(tiles[i][p]);
				} else {
					tiles[i][p].setOccupied(false, c);
				}
			}
			c.setMap(tiles);
		}
	}
	

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}
}
