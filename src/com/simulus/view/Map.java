package com.simulus.view;


import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;

import com.simulus.MainApp;

public class Map extends Group{
	
	private static final int NUM_TILES = 30;
	private static final int CANVAS_SIZE_PX = 900;
	
	private int tileWidth = CANVAS_SIZE_PX/NUM_TILES;
	private Tile[][] tiles = new Tile[NUM_TILES][NUM_TILES];
	
	public Map() {
		
		createBasicMap();
		
		
	}
	
	public void readMap(){
		//Read map from XML
	}
	
	public void addMap(){
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {
				MainApp.getInstance().getCanvas().getChildren().add(tiles[i][p]);
			}
		}
	}
	
	private void createBasicMap() {
			
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {
				tiles[i][p] = new Tile(	i * tileWidth, p * tileWidth,
										tileWidth, tileWidth,
										i, p);
			}
		}

	}
	
	private void addGroup(TileGroup g) {
		Tile[][] t = g.getTiles();
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
	 * @param c
	 *            Vehicle
	 */
	public void updateMap(VVehicle c) {

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
	
	public int getTileWidth() {
		return CANVAS_SIZE_PX/NUM_TILES;
	}
}
