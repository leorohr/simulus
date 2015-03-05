package com.simulus.view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import com.simulus.util.enums.LandType;

public class Land extends Tile {

	// Default LandType is EMPTY
	private LandType landType = LandType.EMPTY;

	/**
	 * Creates a Land Tile object that contains its size (height, width), 
	 * Map coordinates (posX, posY) and array position (gridPosX, gridPosY), 
	 * along with a LandType to determine the image that will be drawn on the map.
	 * 
	 * @param posX X coordinate position from top-left corner of Map
	 * @param posY Y coordinate position from top-left corner of Map
	 * @param width Width of the Land tile
	 * @param height Height of the Land tile
	 * @param gridPosX X Position in the Map array
	 * @param gridPosY Y Position in the Map array
	 * @param landType Type of Land tile to use
	 */
	public Land(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY, LandType landType) {
		super(posX, posY, width, height, gridPosX, gridPosY);
		this.landType = landType;
		
		if (landType == LandType.EMPTY) {
			getFrame().setFill(Color.TRANSPARENT);
		} else {
			getFrame().setFill(new ImagePattern(getLandImage(this.landType)));
		}
		
	}
	
	/**
	 * Returns the LandType of the Land tile
	 * 
	 * @return the landType set for the Land tile
	 */
	public LandType getLandType() {
		return this.landType;
	}
	
	/**
	 * Returns the appropriate image file for the given LandType
	 * 
	 * @param landtype the LandType of the tile
	 * @return image - the image representation of the LandType
	 */
	private Image getLandImage(LandType landtype) {
		Image image;
		switch(this.landType) {
		case GRASS :
			image = new Image("com/simulus/util/images/grass.png");
			break;
		case DIRT :
			image = new Image("com/simulus/util/images/dirt.png");
			break;
		case CITY :
			// TODO Replace CITY image
			image = new Image("com/simulus/util/images/city.png");
		break;
		case BLOCK :
			image = new Image("com/simulus/util/images/block.png");
		break;
		default :
			image = null;
		}
		
		return image;
	}
	

}
