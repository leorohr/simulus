package com.simulus.view;

import com.simulus.util.ResourceBuilder;
import com.simulus.util.enums.Direction;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class Lane extends Tile{

    public static final Color COLOR = Color.GREY;

	private Direction dir;
	private int laneNo;

	public Lane(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY, Direction dir, int laneNo) {
		super(posX, posY, width, height, gridPosX, gridPosY);
		this.dir = dir;
		this.laneNo = laneNo;
		frame.setFill(COLOR);

        frame.setFill(
                new ImagePattern((dir == Direction.EAST || dir == Direction.WEST ?
                        ResourceBuilder.getEWLaneTexture() : ResourceBuilder.getNSLaneTexture())));

	}

    public Direction getDirection() {
		return this.dir;
	}
	
	public int getLaneNo(){
		return laneNo;
	}
}
