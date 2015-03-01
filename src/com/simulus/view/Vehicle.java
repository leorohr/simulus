package com.simulus.view;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a vehicle on the GUI
 */
public abstract class Vehicle extends Group {

	protected Direction dir;
	protected int mapSize;
	protected Tile[][] map;
	protected MainApp parent;
	protected Tile currentTile;
	protected List<Tile> occupiedTiles;
	protected boolean isOvertaking = false;
	protected Behavior behavior;
	protected Rectangle frame;
	
	protected double vehicleSpeed;

	/**
	 * Initialises the position and size of the vehicle
	 * 
	 * @param posX
	 *            The position of the vehicle on X
	 * @param posY
	 *            The position of the vehicle on Y
	 * @param width
	 *            The width of the vehicle
	 * @param height
	 *            The height of the vehicle
	 */	
	public Vehicle(double posX, double posY, double width, double height, Direction dir) {
		frame = new Rectangle(posX, posY, width, height);
		this.parent = MainApp.getInstance();
		this.dir = dir;
		map = SimulationController.getInstance().getMap().getTiles();
		mapSize = map.length;
		occupiedTiles = new ArrayList<>();
		try {
			currentTile = map[(int) (posX / mapSize)][(int) (posY / mapSize)];
		} catch (ArrayIndexOutOfBoundsException e) {
			currentTile = null;
		}
	}

	/**
	 * Describes a vehicle translation
	 */
	public abstract void moveVehicle();
	
	public abstract void overtake(Tile t);

	public Direction getDirection() {
		return dir;
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public void setMap(Tile[][] map) {
		this.map = map;
	}

	public void setCurrentTile(Tile t) {
        currentTile = t;
        addTile(t); //add current tile to list of occupied tiles
    }

	public void removeFromCanvas() {
        if (parent.getCanvas().getChildren().contains(this))
            parent.getCanvas().getChildren().remove(this);
	}

	public Tile getCurrentTile() {
		return currentTile;
	}

	public void addToCanvas() {
		if (!parent.getCanvas().getChildren().contains(this))
			parent.getCanvas().getChildren().add(this);
	}

	private void addTile(Tile t){
        synchronized(this) {
            if (!occupiedTiles.contains(t))
                occupiedTiles.add(t);
        }
	}
	
	public void removeTile(Tile t){
        synchronized (this) {
            if (occupiedTiles.contains(t))
                occupiedTiles.remove(t);
        }
	}

	public List<Tile> getOccupiedTiles(){
        synchronized (this) {
            return occupiedTiles;
        }
    }
	
	public double getVehicleSpeed(){
		return vehicleSpeed;
	}
	
	public void setVehicleSpeed(double d){
		vehicleSpeed = d;
	}
}
