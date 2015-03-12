package com.simulus.view.map;

import com.simulus.util.ResourceBuilder;
import com.simulus.util.enums.Direction;
import com.simulus.view.Tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class Lane extends Tile{

    public static final Color COLOR = Color.GREY;

	private Direction dir;
	private int laneNo;

	public Lane(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY, Direction dir, int laneNo, boolean isBlock) {
		super(posX, posY, width, height, gridPosX, gridPosY);
		this.dir = dir;
		this.laneNo = laneNo;
		this.isBlock = isBlock;
        frame.setFill(
                new ImagePattern((dir == Direction.EAST || dir == Direction.WEST ?
                        ResourceBuilder.getEWLaneTexture() : ResourceBuilder.getNSLaneTexture())));
        if (isBlock == true){
        	isOccupied = true;
        	frame.setFill(new ImagePattern(ResourceBuilder.getBlockTexture()));
        }
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

	/**
	 * Sets the Lane's texture to the correct image. Used in debug-mode to redraw tiles.
	 */
	public void redraw() {
		frame.setFill(
                new ImagePattern((dir == Direction.EAST || dir == Direction.WEST ?
                        ResourceBuilder.getEWLaneTexture() : ResourceBuilder.getNSLaneTexture())));
        if (isBlock == true){
        	frame.setFill(new ImagePattern(ResourceBuilder.getBlockTexture()));
        }
	}
}
