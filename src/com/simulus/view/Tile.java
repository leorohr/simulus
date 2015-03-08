package com.simulus.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.simulus.controller.SimulationController;
import com.simulus.view.map.Lane;
import com.simulus.view.vehicles.Vehicle;

public class Tile extends Group {

	private int gridPosX;
	private int gridPosY;
	private Vehicle occupier;
    protected Rectangle frame;
    protected boolean isOccupied;
    protected boolean isBlock;
    protected boolean isRedLight = false;

	public Tile(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY) {
		//super(posX, posY, width, height);
		frame = new Rectangle(posX, posY, width, height);
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		occupier = null;
		isOccupied = false;
		//frame.setFill(new ImagePattern(ResourceBuilder.getLandTexture()));
		frame.setFill(Color.TRANSPARENT);
		frame.setStroke(Color.BLACK);
		frame.setStrokeWidth(0.1);
		this.getChildren().add(frame);
	}

    /**
     * Sets the tile to occupied or free. Passing in the vehicle makes sure that only the current
     * occupier can set the tile's occupation status.
     * @param isOccupied
     * @param occupier
     */
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
	
	/**
	 * Sets the tile to occupied or free without checking whether the caller is the currently
	 * occupying car.
	 * @param isOccupied
	 */
	public void setOccupied(boolean isOccupied){
		this.isOccupied = isOccupied;
		if(isOccupied)
			isRedLight = true;
		else isRedLight = false;
		redrawTile();
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
            if(isRedLight)
            	frame.setFill(Color.RED);
		} else {
            if(this instanceof Lane)
			    ((Lane)this).redraw();
            else frame.setFill(Color.TRANSPARENT); //keep intersections transparent 
            if(isRedLight)
            	frame.setFill(Color.RED); //redraw trafficlights
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
	
	public boolean isRedLight(){
		return isRedLight;
	}
	
	public boolean isBlock(){
		return isBlock;
	}
	
	@Override
	public String toString(){
		return "X: " + gridPosX + " Y: " + gridPosY + " Is Occupied: " + isOccupied + " By: " + occupier;
	}
}
