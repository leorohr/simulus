package com.simulus.view;

import javafx.scene.paint.Color;

import com.simulus.util.enums.Direction;

public class Lane extends Tile{
	
	private Direction dir;
	private int laneNo;

	public Lane(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY, Direction dir, int laneNo) {
		super(posX, posY, width, height, gridPosX, gridPosY);
		this.dir = dir;
		this.laneNo = laneNo;
		getFrame().setFill(Color.GREY);
	}
	
	
	public Direction getDirection() {
		return this.dir;
	}
	
	public int getLaneNo(){
		return laneNo;
	}
}
