package com.simulus.view;

import com.simulus.util.enums.Direction;
import javafx.scene.paint.Color;

public class Lane extends Tile{

    public static final Color COLOR = Color.GREY;

	private Direction dir;
	private int laneNo;

	public Lane(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY, Direction dir, int laneNo) {
		super(posX, posY, width, height, gridPosX, gridPosY);
		this.dir = dir;
		this.laneNo = laneNo;
		getFrame().setFill(COLOR);
	}
	
	
	public Direction getDirection() {
		return this.dir;
	}
	
	public int getLaneNo(){
		return laneNo;
	}
}
