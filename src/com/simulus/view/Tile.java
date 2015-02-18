package com.simulus.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

	private int gridPosX;
	private int gridPosY;
	private boolean isOccupied;
	private VVehicle occupier;

	public Tile(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY) {
		super(posX, posY, width, height);
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		occupier = null;
		isOccupied = false;
		setFill(Color.TRANSPARENT);
		setStroke(Color.BLACK);
	}

	public void setOccupied(boolean isOccupied, VVehicle occupier) {
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

	public VVehicle getOccupier() {
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

	public void setGridPosX(int x) {
		gridPosX = x;
	}

	public void setGridPosY(int y) {
		gridPosY = y;
	}

	public void redrawTile() {
		if (isOccupied()) {
			setFill(Color.GREEN);
		} else {
			setFill(Color.TRANSPARENT);
		}
	}

	public double getCenterX() {
		return getX() + (getWidth() / 2);
	}

	public double getCenterY() {
		return getY() + (getHeight() / 2);
	}
}
