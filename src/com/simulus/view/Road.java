package com.simulus.view;


import com.simulus.util.enums.Seed;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Road extends Group{

	Rectangle frame = new Rectangle();
	Rectangle laneSeperator1;
	Rectangle laneSeperator2;
	Rectangle laneSeperator3;
	
	public Road(int posX, int posY, int width, int height, Seed orientation){
		frame =  new Rectangle(posX, posY, width, height);
		frame.setFill(Color.BLACK);
		
		switch(orientation){
		case NORTHSOUTH:
		laneSeperator1 =  new Rectangle(posX+width/4-width/40, frame.getY(), frame.getWidth()/20, frame.getHeight()-height/10);
		laneSeperator1.setFill(Color.WHITE);
		laneSeperator1.setStroke(Color.BLACK);
		laneSeperator1.setStrokeDashOffset(laneSeperator1.getHeight()/3);
		
		laneSeperator2 =  new Rectangle(posX+width/2-width/40, frame.getY(), frame.getWidth()/20, frame.getHeight()-height/10);
		laneSeperator2.setFill(Color.WHITE);
		laneSeperator2.setStrokeDashOffset(laneSeperator1.getHeight()/3);
		laneSeperator2.setStroke(Color.BLACK);
		
		laneSeperator3 =  new Rectangle(posX+(width*3)/4-width/40, frame.getY(), frame.getWidth()/20, frame.getHeight()-height/10);
		laneSeperator3.setFill(Color.WHITE);
		laneSeperator3.setStroke(Color.BLACK);
		laneSeperator3.setStrokeDashOffset(laneSeperator1.getHeight()/3);
			break;
		case WESTEAST:
		laneSeperator1 =  new Rectangle(frame.getX(), posY+height/4-height/40, frame.getWidth()-width/10, frame.getHeight()/20);
		laneSeperator1.setFill(Color.WHITE);
		laneSeperator1.setStroke(Color.BLACK);
		laneSeperator1.setStrokeDashOffset(laneSeperator1.getHeight()/3);
		
		laneSeperator2 =  new Rectangle(frame.getX(), posY+height/2-height/40, frame.getWidth()-width/10, frame.getHeight()/20);
		laneSeperator2.setFill(Color.WHITE);
		laneSeperator2.setStroke(Color.BLACK);
		laneSeperator2.setStrokeDashOffset(laneSeperator1.getHeight()/3);
		
		laneSeperator3 =  new Rectangle(frame.getX(), posY+(height*3)/4-height/40, frame.getWidth()-width/10, frame.getHeight()/20);
		laneSeperator3.setFill(Color.WHITE);
		laneSeperator3.setStroke(Color.BLACK);
		laneSeperator3.setStrokeDashOffset(laneSeperator1.getHeight()/3);
			break;
		}
		
		this.getChildren().addAll(frame, laneSeperator1, laneSeperator2, laneSeperator3);
	}
	public Road(int posX, int posY, int width, int height){
		frame =  new Rectangle(posX, posY, width, height);
		frame.setFill(Color.BLACK);
		this.getChildren().addAll(frame);
	}
}
