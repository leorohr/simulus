package com.simulus.view;

import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

import com.simulus.controller.SimulationController;
import com.simulus.util.ResourceBuilder;
import com.simulus.util.enums.Direction;
import com.simulus.view.map.Lane;
import com.simulus.view.vehicles.Vehicle;

import javafx.scene.paint.RadialGradient;

public class Tile extends Group {

	private int gridPosX;
	private int gridPosY;
	private boolean isOccupied;
	private Vehicle occupier;
	protected Rectangle frame;
	protected boolean isRedLight = false;

	public Tile(double posX, double posY, double width, double height,
			int gridPosX, int gridPosY) {
		// super(posX, posY, width, height);
		frame = new Rectangle(posX, posY, width, height);
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		occupier = null;
		isOccupied = false;
		// frame.setFill(new ImagePattern(ResourceBuilder.getLandTexture()));
		frame.setFill(Color.TRANSPARENT);
		frame.setStroke(Color.BLACK);
		frame.setStrokeWidth(0.1);
		this.getChildren().add(frame);
	}

	/**
	 * Sets the tile to occupied or free. Passing in the vehicle makes sure that
	 * only the current occupier can set the tile's occupation status.
	 * 
	 * @param isOccupied
	 * @param occupier
	 */
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

	/**
	 * Sets the tile to occupied or free without checking whether the caller is
	 * the currently occupying car.
	 * 
	 * @param isOccupied
	 */
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
		redrawTile();
	}

	/**
	 * If the app is currently in debug mode, this method draws the tiles
	 * occupied by vehicles in green.
	 */
	public void redrawTile() {
		if (!SimulationController.getInstance().isDebug())
			return;

		if (isOccupied()) {
			frame.setFill(Color.GREEN);
			if (isRedLight){
				//redraw red tile
				RadialGradient gradient = new RadialGradient(0d, 0d, 0.5d, 0.5d, 1d,
						true, CycleMethod.REFLECT, new Stop[] {
								new Stop(0d, Color.RED), new Stop(1d, Color.BLACK) });
				GaussianBlur blur = new GaussianBlur(5d);

				this.getFrame().setEffect(blur);
				this.getFrame().setFill(gradient);
			}else{
				//redraw green tile
			}
		} else {
			if (this instanceof Lane)
				((Lane) this).redraw();
			else
				frame.setFill(Color.TRANSPARENT); // keep intersections
													// transparent
			if (isRedLight)
				;
		}
	}

	/**
	 * @return The x-coordinate of the top left corner of the tile.
	 */
	public double getX() {
		return frame.getX();
	}

	/**
	 * @return The y-coordinate of the top left corner of the tile.
	 */
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

	public boolean isRedLight() {
		return isRedLight;
	}

	@Override
	public String toString() {
		return "X: " + gridPosX + " Y: " + gridPosY + " Is Occupied: "
				+ isOccupied + " By: " + occupier;
	}

	public void setRedLight(boolean b) {
		isRedLight = b;
	}
}
