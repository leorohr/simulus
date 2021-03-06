package com.simulus.view.map;

import com.simulus.util.enums.LandType;

/**
 * An extension to {@link com.simulus.view.map.Land}.
 * Water tiles are a type of Land.
 */
public class Water extends Land {

	/**
	 * Creates a Land Tile object of type WATER that contains its 
	 * size (height, width), Map coordinates (posX, posY) 
	 * and array position (gridPosX, gridPosY)
	 * 
	 * @param posX X coordinate position from top-left corner of Map
	 * @param posY Y coordinate position from top-left corner of Map
	 * @param width Width of the Land tile
	 * @param height Height of the Land tile
	 * @param gridPosX X Position in the Map array
	 * @param gridPosY Y Position in the Map array
	 */
	public Water(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY) {
		super(posX, posY, width, height, gridPosX, gridPosY, LandType.WATER);
	}
	
}
