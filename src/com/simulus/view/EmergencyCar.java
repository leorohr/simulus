package com.simulus.view;

import javafx.scene.paint.Color;

import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Direction;

public class EmergencyCar extends Car {

	private static final Color COLOUR = Color.YELLOW;

	public EmergencyCar(double posX, double posY, Direction dir) {
		super(posX, posY, dir);

		setFill(COLOUR);
		vehicleSpeed = SimulationController.getInstance().getMaxCarSpeed();
		setArcHeight(0);
		setArcWidth(0);
	}

	public void moveVehicle() {
		
		tempDir = getDirection();

		try {
			Tile nextTile = null;
			switch (getDirection()) {
			case NORTH:
				if (currentTile.getGridPosY() - 1 < 0) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() - 1];
				if (nextTile.isOccupied() && nextTile.getOccupier() != null) {
					tempDir = Direction.NONE;
				} else
					tempDir = getDirection();
				break;

			case SOUTH:
				if (currentTile.getGridPosY() + 1 >= map.length) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() + 1];
				if (nextTile.isOccupied() && nextTile.getOccupier() != null) {
					tempDir = Direction.NONE;
				} else
					tempDir = getDirection();
				break;

			case EAST:
				if (currentTile.getGridPosX() + 1 >= map.length) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX() + 1][getCurrentTile()
						.getGridPosY()];
				if (nextTile.isOccupied() && nextTile.getOccupier() != null) {
					tempDir = Direction.NONE;
				} else
					tempDir = getDirection();
				break;

			case WEST:
				if (currentTile.getGridPosX() - 1 < 0) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX() - 1][getCurrentTile()
						.getGridPosY()];
				if (nextTile.isOccupied() && nextTile.getOccupier() != null) {
					tempDir = Direction.NONE;
				} else
					tempDir = getDirection();
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		move(tempDir);
		((Ambulance) getParent()).updateAoE();
	}

	public void removeFromCanvas() {
		((Ambulance) getParent()).remove();
	}

}
