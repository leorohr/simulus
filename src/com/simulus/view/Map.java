package com.simulus.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.Group;

import com.simulus.MainApp;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;

public class Map extends Group{
	
	private static final int NUM_ROWS = 40;
	private static final int NUM_COLUMNS = 40;
	public static final int TILESIZE = (int) (MainApp.getInstance().getCanvas().getWidth()/NUM_ROWS);
	
	private Tile[][] tiles = new Tile[NUM_COLUMNS][NUM_ROWS];
	private ArrayList<Intersection> intersections = new ArrayList<Intersection>();
	private ArrayList<Lane> entryPoints = new ArrayList<Lane>();
	
	public Map() {		
		
//		createBasicMap();
		createHashStyleMap();
		
		
		//Add map to canvas
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
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
			for (int p = 0; p < tiles[0].length; p++) {
				tiles[i][p] = new Tile(	i * TILESIZE, p * TILESIZE,
										TILESIZE, TILESIZE,
										i, p);
			}
		}
		
		for(int i=0; i<tiles.length; i++) {
			addGroup(new Road(18, i, Seed.NORTHSOUTH));
			addGroup(new Road(i, 18, Seed.WESTEAST));
		}
		addGroup(new Intersection(18, 18));
	}
	
	private void createHashStyleMap() {
		//init all tiles
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
				tiles[i][p] = new Tile(	i * TILESIZE, p * TILESIZE,
										TILESIZE, TILESIZE,
										i, p);
			}
		}
		
		for(int i=0; i<tiles.length; i++) {
			addGroup(new Road(i, 7, Seed.WESTEAST));
			addGroup(new Road(7, i, Seed.NORTHSOUTH));
			
			addGroup(new Road(i, 18, Seed.WESTEAST));
			addGroup(new Road(18, i, Seed.NORTHSOUTH));
			
			addGroup(new Road(i, 29, Seed.WESTEAST));
			addGroup(new Road(29, i, Seed.NORTHSOUTH));
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
	 */
	public void spawnRandomCar() {
		Random rand = new Random();
		Lane l = entryPoints.get(rand.nextInt(entryPoints.size()));
		
		//Car adds itself to the canvas
		//TODO move to car constructor; car constructor should then just take Lane l
		if(l.getDirection() == Direction.NORTH || l.getDirection() == Direction.SOUTH)			
			new Car(l.getGridPosX() * Map.TILESIZE + Map.TILESIZE/2 - Car.CARWIDTH/2,
					l.getGridPosY() * Map.TILESIZE + Map.TILESIZE - Car.CARHEIGHT,
					l.getDirection());
		if(l.getDirection() == Direction.WEST || l.getDirection() == Direction.EAST)			
			new Car(l.getGridPosX() * Map.TILESIZE,
					l.getGridPosY() * Map.TILESIZE + Map.TILESIZE/2 - Car.CARWIDTH/2,
					l.getDirection());	
	}
	
	public void spawnTesterCar() {
		
		Lane l = entryPoints.get(4);
		
		//Car adds itself to the canvas
		new Car(l.getGridPosX() * Map.TILESIZE + Car.CARWIDTH / 4,
				l.getGridPosY() * Map.TILESIZE + Car.CARHEIGHT / 8,
				l.getDirection());	
		
		Lane a = entryPoints.get(0);
		
		//Car adds itself to the canvas
		new Car(a.getGridPosX() * Map.TILESIZE + Car.CARWIDTH / 4,
				a.getGridPosY() * Map.TILESIZE + Car.CARHEIGHT / 8,
				a.getDirection());	
	}
	
	
	
	private void addGroup(TileGroup g) {
		
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
	public void createBlockage(int tileX, int tileY) {
			tiles[tileX][tileY].setOccupied(true);
		
	}

	/**
	 * Updates the map according to the vehicle passed in. Gives the vehicle a
	 * copy of the updated map
	 * 
	 * @param c Vehicle
	 */
	public void updateMap(Vehicle c) {

		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
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
