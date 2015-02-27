package com.simulus.view;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Tile extends Group {

	public int gridPosX;
	public int gridPosY;
	public boolean isOccupied;
	private Vehicle occupier;
	private Rectangle frame;
	private Image imgLand = new Image("com/simulus/util/images/land.png");

	public Tile(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY) {
		// super(posX, posY, width, height);
		frame = new Rectangle(posX, posY, width, height);
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		occupier = null;
		isOccupied = false;
		frame.setFill(Color.TRANSPARENT);
		frame.setStroke(Color.BLACK);
		frame.setStrokeWidth(0.1);
		this.getChildren().add(frame);
	}

	public void setOccupied(boolean isOccupied, Vehicle occupier) {
		if (isOccupied) {
			this.occupier = occupier;
			this.isOccupied = true;
			redrawTile();
		} else if (occupier.equals(getOccupier())) {
			this.occupier = null;
			this.isOccupied = false;
			redrawTile();
		}
	}

	public void setOccupied(boolean isOccupied) {
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

	public void setGridPosX(int x) {
		gridPosX = x;
	}

	public void setGridPosY(int y) {
		gridPosY = y;
	}

	public void redrawTile() {
		if (isOccupied()) {
			frame.setFill(new ImagePattern(imgLand));
		} else {
			frame.setFill(Color.TRANSPARENT);
		}
	}

	public double getX() {
		return frame.getX();
	}

	public double getY() {
		return frame.getY();
	}

	public double getWidth() {
		return frame.getWidth();
	}

	public double getHeight() {
		return frame.getHeight();
	}

	public Rectangle getFrame() {
		return frame;
	}

	public double getCenterX() {
		return getX() + (getWidth() / 2);
	}

	public double getCenterY() {
		return getY() + (getHeight() / 2);
	}

	public String toString() {
		return "X: " + getX() + " Y:" + getY();
	}

}
