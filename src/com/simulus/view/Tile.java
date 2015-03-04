package com.simulus.view;

import com.simulus.controller.SimulationController;
import com.simulus.util.ResourceBuilder;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Tile extends Group {

	private int gridPosX;
	private int gridPosY;
	private boolean isOccupied;
	private Vehicle occupier;
	private ArrayList<CustomPath> turningPaths;
    protected Rectangle frame;
    protected boolean isRedLight = false;
    
    private Intersection parent = null;

	public Tile(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY) {
		//super(posX, posY, width, height);
		frame = new Rectangle(posX, posY, width, height);
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		occupier = null;
		isOccupied = false;
		turningPaths = new ArrayList<>();
		frame.setFill(new ImagePattern(ResourceBuilder.getLandTexture()));
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
	
	/**
	 * @return The list of available paths for cars to take when they face this tile in an intersection.
	 */
	public ArrayList<CustomPath> getTurningPaths() {
		return turningPaths;			
	}
	
	public void setIntersection(Intersection i){
		parent = i;
	}
	public Intersection getIntersection(){
		return parent;
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
	
	@Override
	public String toString(){
		return "X: " + gridPosX + " Y: " + gridPosY + " Is Occupied: " + isOccupied + " By: " + occupier;
	}
}
