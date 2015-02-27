package com.simulus.view;

import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;
import javafx.scene.Group;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a roadblock. Roadblock refers to four adjacent lanes, hence one roadblock takes up a 1x4 space
 * in the grid of tiles.
 */
public class Road extends Group implements TileGroup{

	private Lane[] lanes = new Lane[4];
	private Seed orientation;
	
	/**
	 * @param gridPosX The mostleft/top x-coordinate of the road in the grid.
	 * @param gridPosY The mostleft/top y-coordinate of the road in the grid.
	 * @param orientation The orientation of the Road.
	 */
	public Road(int gridPosX, int gridPosY, Seed orientation){
		
		int tileSize = Map.TILESIZE;
		this.orientation = orientation;
		
		switch(orientation){
		case NORTHSOUTH:
			for (int i = 0; i < lanes.length; i++) {
				if(i<2) 
					lanes[i] = new Lane((gridPosX + i)*tileSize, gridPosY*tileSize,
										tileSize, tileSize, gridPosX+i, gridPosY, Direction.NORTH, i);
				else
					lanes[i] = new Lane((gridPosX + i)*tileSize, gridPosY * tileSize,
										tileSize, tileSize, gridPosX+i, gridPosY, Direction.SOUTH, i);
			}
			break;
		case WESTEAST:
			for (int i = 0; i < lanes.length; i++) {
				if(i<2) 
					lanes[i] = new Lane(gridPosX*tileSize, (gridPosY + i)*tileSize,
										tileSize, tileSize, gridPosX, gridPosY+i, Direction.EAST, i);
				else
					lanes[i] = new Lane(gridPosX*tileSize, (gridPosY + i)*tileSize,
										tileSize, tileSize, gridPosX, gridPosY+i, Direction.WEST, i);
			}
			break;
		default:
			break;
		}
		
		this.getChildren().addAll(lanes);
	}

    /**
     * @return A list containing the 4 tiles that this roadblock is comprised of.
     */
	@Override
	public List<Tile> getTiles() {
		
		return Arrays.asList(lanes);
	}

    /**
     * @return The orientation of this roadblock.
     */
	public Seed getOrientation() {
		return this.orientation;
	}
}
