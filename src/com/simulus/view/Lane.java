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
        frame.setFill(
                new ImagePattern((dir == Direction.EAST || dir == Direction.WEST ?
                        ResourceBuilder.getEWLaneTexture() : ResourceBuilder.getNSLaneTexture())));
	}

	/**
	 * @return The direction the lane is bound towards.
	 */
    public Direction getDirection() {
		return this.dir;
	}
	
    /**
     * A lane number is used to identify lanes available for overtaking cars.
     * @return The number of the lane.
     */
	public int getLaneNo(){
		return laneNo;
	}
	
	/*
	 * Sets the Lane's texture to the correct image. Used in debug-mode to redraw tiles.
	 */
	public void redraw() {
		frame.setFill(
                new ImagePattern((dir == Direction.EAST || dir == Direction.WEST ?
                        ResourceBuilder.getEWLaneTexture() : ResourceBuilder.getNSLaneTexture())));
	}
}
