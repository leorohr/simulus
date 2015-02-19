package com.simulus.view;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.simulus.util.enums.Seed;

public class Road extends Group implements TileGroup{

	Lane[] lanes = new Lane[4];
	
	public Road(int posX, int posY, int width, int height, Seed orientation){
		
		
		switch(orientation){
		case NORTHSOUTH:
		
			break;
		case WESTEAST:
		
			break;
		default:
			break;
		}
		
		this.getChildren().addAll(frame, laneSeperator1, laneSeperator2, laneSeperator3);
	}
	public Road(int posX, int posY, int width, int height){
		frame =  new Rectangle(posX, posY, width, height);
		frame.setFill(Color.BLACK);
		this.getChildren().addAll(frame);
	}
	@Override
	public List<Tile> getTiles() {
		
		return null;
	}
}
