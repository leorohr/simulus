package com.simulus.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import com.simulus.EditorApp;
import com.simulus.MainApp;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;

public class Map extends Group {

	private static final int NUM_TILES = 40;
	private static final int CANVAS_SIZE_PX = 800;
	public static final int TILESIZE = CANVAS_SIZE_PX / NUM_TILES;

	public Tile[][] tiles = new Tile[NUM_TILES][NUM_TILES];
	private ArrayList<Intersection> intersections = new ArrayList<Intersection>();
	private ArrayList<Lane> entryPoints = new ArrayList<Lane>();

	public Map() {

		createBasicMap();

		// Add map to canvas
		drawMap();

	}

	public void drawMap() {
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {

				if (MainApp.getInstance() != null) {
					MainApp.getInstance().getCanvas().getChildren()
							.add(tiles[i][p]);
				} else if (EditorApp.getInstance() != null) {
					EditorApp.getInstance().getCanvas().getChildren()
							.add(tiles[i][p]);
				} else {
					System.out.println("No current application instance");
				}
			}
		}
	}

	public void readMap() {
		// TODO Read map from XML
	}

//	// Hardcoded map for testing
	private void createBasicMap() {

		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {
				tiles[i][p] = new Tile(i * TILESIZE, p * TILESIZE, TILESIZE,
						TILESIZE, i, p);
				//tiles[i][p].setOccupied(true);
			}
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
	
	
	public void addGroup(TileGroup g) {

		List<Tile> l = g.getTiles();
		for (Tile t : l) {
			tiles[t.getGridPosX()][t.getGridPosY()] = t;

			if (g instanceof Road) {
				if (t.getGridPosX() == tiles.length - 1
						&& ((Road) g).getOrientation() == Seed.WESTEAST
						&& ((Lane) t).getDirection() == Direction.WEST) {
					entryPoints.add((Lane) t);
				} else if (t.getGridPosX() == 0
						&& ((Road) g).getOrientation() == Seed.WESTEAST
						&& ((Lane) t).getDirection() == Direction.EAST) {
					entryPoints.add((Lane) t);
				} else if (t.getGridPosY() == tiles.length - 1
						&& ((Road) g).getOrientation() == Seed.NORTHSOUTH
						&& ((Lane) t).getDirection() == Direction.NORTH) {
					entryPoints.add((Lane) t);
				} else if (t.getGridPosY() == 0
						&& ((Road) g).getOrientation() == Seed.NORTHSOUTH
						&& ((Lane) t).getDirection() == Direction.SOUTH) {
					entryPoints.add((Lane) t);
				}
			}
		}

		if (g instanceof Intersection)
			intersections.add((Intersection) g);
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
						&& ((Road) g).getOrientation() == Seed.WESTEAST
						&& ((Lane) t).getDirection() == Direction.WEST) {
					entryPoints.remove((Lane) t);
				} else if (t.getGridPosX() == 0
						&& ((Road) g).getOrientation() == Seed.WESTEAST
						&& ((Lane) t).getDirection() == Direction.EAST) {
					entryPoints.remove((Lane) t);
				} else if (t.getGridPosY() == tiles.length - 1
						&& ((Road) g).getOrientation() == Seed.NORTHSOUTH
						&& ((Lane) t).getDirection() == Direction.NORTH) {
					entryPoints.remove((Lane) t);
				} else if (t.getGridPosY() == 0
						&& ((Road) g).getOrientation() == Seed.NORTHSOUTH
						&& ((Lane) t).getDirection() == Direction.SOUTH) {
					entryPoints.remove((Lane) t);
				}
			}
		}

		if (g instanceof Intersection)
			intersections.remove((Intersection) g);
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

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}
}
