package com.simulus.view;

import java.util.ArrayList;

import javafx.animation.PathTransition;
import javafx.scene.shape.Rectangle;

import com.simulus.MainApp;
import com.simulus.util.enums.Direction;

/**
 * Describes a vehicle on the GUI
 */
public abstract class Vehicle extends Rectangle {

	protected Direction dir;
	protected int mapSize;
	protected Tile[][] map;
	protected MainApp parent;
	protected Tile currentTile;
	protected boolean onScreen;
	protected ArrayList<Tile> occupiedTiles;
	
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
		super(posX, posY, width, height);
		this.parent = MainApp.getInstance();
		this.dir = dir;
		map = parent.getMap().getTiles();
		mapSize = map.length;
		occupiedTiles = new ArrayList<Tile>();
		try {
			currentTile = map[(int) (posX / mapSize)][(int) (posY / mapSize)];
			onScreen = true;
		} catch (ArrayIndexOutOfBoundsException e) {
			currentTile = null;
			onScreen = false;
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
		switch (getDirection()) {
		case NORTH:
			if (getCurrentTile() != null) {
				Tile oldTile = getCurrentTile();
				if (oldTile.getGridPosY() > t.getGridPosY())
					currentTile = t;
				else
					return;
			} else
				currentTile = t;
			break;
		case SOUTH:
			if (getCurrentTile() != null) {
				Tile oldTile = getCurrentTile();
				if (oldTile.getGridPosY() < t.getGridPosY())
					currentTile = t;
				else
					return;
			} else
				currentTile = t;
			break;
		case EAST:
			if (getCurrentTile() != null) {
				Tile oldTile = getCurrentTile();
				if (oldTile.getGridPosX() < t.getGridPosX())
					currentTile = t;
				else
					return;
			} else
				currentTile = t;
			break;
		case WEST:
			if (getCurrentTile() != null) {
				Tile oldTile = getCurrentTile();
				if (oldTile.getGridPosX() > t.getGridPosX())
					currentTile = t;
				else
					return;
			} else
				currentTile = t;
			break;
		default:
			break;
		}

	}

	public Tile getCurrentTile() {
		return currentTile;
	}

	public void addToCanvas() {
		if (!parent.getCanvas().getChildren().contains(this))
			parent.getCanvas().getChildren().add(this);
	}

	public void removeFromCanvas() {
		if (parent.getCanvas().getChildren().contains(this))
			parent.getCanvas().getChildren().remove(this);
	}

	public boolean getOnScreen() {
		return onScreen;
	}

	public void setOnScreen(boolean b) {
		onScreen = b;
	}
	
	public void addTile(Tile t){
		if(!occupiedTiles.contains(t))
			occupiedTiles.add(t);
	}
	
	public void removeTile(Tile t){
		if(occupiedTiles.contains(t))
			occupiedTiles.remove(t);
	}

	public ArrayList<Tile> getOccupiedTiles(){
		return occupiedTiles;
	}
	
	public double getVehicleSpeed(){
		return vehicleSpeed;
	}
	
	public void setVehicleSpeed(double d){
		vehicleSpeed = d;
	}
}
