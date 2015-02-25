package com.simulus.view;

import com.simulus.controller.SimulationController;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Tile extends Group {

	private int gridPosX;
	private int gridPosY;
	private boolean isOccupied;
	private Vehicle occupier;
	private Rectangle frame;
	private ArrayList<Path> turningPaths;

	public Tile(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY) {
		//super(posX, posY, width, height);
		frame = new Rectangle(posX, posY, width, height);
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		occupier = null;
		isOccupied = false;
		turningPaths = new ArrayList<>();
		frame.setFill(Color.LIGHTGOLDENRODYELLOW);
		frame.setStroke(Color.BLACK);
		frame.setStrokeWidth(0.2d);
		this.getChildren().add(frame);
	}

    //Makes sure that only the current occupier can set the tile's occupation status
	public void setOccupied(boolean isOccupied, Vehicle occupier) {
		if (isOccupied) {
			this.occupier = occupier;
			this.isOccupied = true;
			redrawTile();
		}else if(occupier.equals(getOccupier())){
			this.occupier = null;
			this.isOccupied = false;
			redrawTile();
		}
	}
	
	public void setOccupied(boolean isOccupied){
		this.isOccupied = isOccupied;
		redrawTile();
	}

	public Vehicle getOccupier() {
		return occupier;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public int getGridPosX() {
		return gridPosX;
	}

	public int getGridPosY() {
		return gridPosY;
	}

	/**
	 * If the app is currently in debug mode, this method draws
	 * the tiles occupied by vehicles in green.
	 */
	public void redrawTile() {
		if(!SimulationController.getInstance().isDebug())
			return;
		
		if (isOccupied()) {
            frame.setFill(Color.GREEN);
		} else {
            if(this instanceof Lane)
			    frame.setFill(Lane.COLOR);
            else frame.setFill(Color.TRANSPARENT);
		}
	}
	
	/**
	 * @return The x-coordinate of the top left corner of the tile.
	 */
	public double getX(){
		return frame.getX();
	}
	
	/**
	 * @return The y-coordinate of the top left corner of the tile.
	 */
	public double getY(){
		return frame.getY();
	}
	
	public double getWidth(){
		return frame.getWidth();
	}
	
	public double getHeight(){
		return frame.getHeight();
	}
	
	public Rectangle getFrame(){
		return frame;
	}

	public double getCenterX() {
		return getX() + (getWidth() / 2);
	}

	public double getCenterY() {
		return getY() + (getHeight() / 2);
	}
	
	/**
	 * @return The list of available paths for cars to take when they face this tile in an intersection.
	 */
	public ArrayList<Path> getTurningPaths() {
		return turningPaths;
				
	}
}
