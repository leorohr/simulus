package com.simulus.view;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Group;

import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;

public class Road extends Group implements TileGroup{

	private Lane[] lanes = new Lane[4];
	private Seed orientation;
	
	/**
	 * @param gridPosX The mostleft/top x-coordinate of the road in the grid.
	 * @param gridPosY The mostleft/top y-coordinate of the road in the grid.
	 * @param orientation
	 */
	public Road(int gridPosX, int gridPosY, Seed orientation){
		
		int tileSize = Map.TILESIZE;
		this.orientation = orientation;
		
		switch(orientation){
		case NORTHSOUTH:
			for (int i = 0; i < lanes.length; i++) {
				if(i<2) 
					lanes[i] = new Lane((gridPosX + i)*tileSize, gridPosY*tileSize,
										tileSize, tileSize, gridPosX+i, gridPosY, Direction.NORTH);
				else
					lanes[i] = new Lane((gridPosX + i)*tileSize, gridPosY * tileSize,
										tileSize, tileSize, gridPosX+i, gridPosY, Direction.SOUTH);
			}
			break;
		case WESTEAST:
			for (int i = 0; i < lanes.length; i++) {
				if(i<2) 
					lanes[i] = new Lane(gridPosX*tileSize, (gridPosY + i)*tileSize,
										tileSize, tileSize, gridPosX, gridPosY+i, Direction.WEST);
				else
					lanes[i] = new Lane(gridPosX*tileSize, (gridPosY + i)*tileSize,
										tileSize, tileSize, gridPosX, gridPosY+i, Direction.EAST);
			}
			break;
		default:
			break;
		}
		
		this.getChildren().addAll(lanes);
	}
	
	@Override
	public List<Tile> getTiles() {
		
		return Arrays.asList(lanes);
	}
	
	public Seed getOrientation() {
		return this.orientation;
	}
}
