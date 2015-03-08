package com.simulus.view.map;

import com.simulus.util.enums.Direction;
import com.simulus.util.enums.LandType;

public class Block extends Land {
	
	
	private Direction dir;
	private int laneNo;

	/**
	 * Creates a Land Tile object of type BLOCK that contains its 
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
	public Block(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY, Direction dir, int laneNo) {
		super(posX, posY, width, height, gridPosX, gridPosY, LandType.BLOCK);
		
		this.dir = dir;
		this.laneNo = laneNo;
	}
	
	public Direction getDirection() {
		return dir;
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public int getLaneNo() {
		return laneNo;
	}

	public void setLaneNo(int laneNo) {
		this.laneNo = laneNo;
	}

}