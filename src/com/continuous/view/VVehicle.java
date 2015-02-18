package com.continuous.view;

import javafx.scene.shape.Rectangle;

import com.continous.util.enums.Direction;
import com.continuous.MainApp;

/**
 * Describes a vehicle on the GUI
 */
public abstract class VVehicle extends Rectangle {

	protected Direction dir;
	protected int mapSize;
	protected Tile[][] map;
	protected MainApp parent;
	protected Tile currentTile;
	protected boolean onScreen;

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
	public VVehicle(double posX, double posY, double width, double height,
			Direction dir, MainApp gui) {
		super(posX, posY, width, height);
		parent = gui;
		this.dir = dir;
		mapSize = gui.getGridSize();
		map = gui.getMap();
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

	public Direction getDirection() {
		return dir;
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public void setMap(Tile[][] map) {
		this.map = map;
	}

	public void setCurentTile(Tile t) {
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

}