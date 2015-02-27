package com.simulus.view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import com.simulus.util.enums.Direction;

public class Lane extends Tile {

	private Direction dir;
	private Image imgRoadEW = new Image("com/simulus/util/images/roadtile_eastwest.png");
	private Image imgRoadNS = new Image("com/simulus/util/images/roadtile_northsouth.png");

	public Lane(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY, Direction dir) {
		super(posX, posY, width, height, gridPosX, gridPosY);
		this.dir = dir;
		
		if(this.dir == Direction.EAST || this.dir == Direction.WEST) {
			getFrame().setFill(new ImagePattern(imgRoadEW));
		} else if(this.dir == Direction.NORTH || this.dir == Direction.SOUTH) {
			getFrame().setFill(new ImagePattern(imgRoadNS));
		} else {
			getFrame().setFill(Color.BLACK);
		}
	}

	public Direction getDirection() {
		return this.dir;
	}
}
