package com.simulus.view.vehicles;

import javafx.scene.paint.Color;

import com.simulus.controller.SimulationController;
import com.simulus.util.Configuration;
import com.simulus.util.enums.Direction;
import com.simulus.view.Tile;

public class EmergencyCar extends Car {

	private static final Color COLOUR = Color.YELLOW;

	public EmergencyCar(double posX, double posY, Direction dir) {
		super(posX, posY, dir);

		setFill(COLOUR);
		
		//4.64m/s^2; 1m=tilesize/5; 1 tick = 1/10s
		acceleration = (4.64 * Configuration.tileSize/5)/10;
		double speedInMps = ((double)SimulationController.getInstance().getMaxCarSpeed()*1000)/3600;
		maxSpeed = speedInMps * (Configuration.tileSize/5)/10;
		vehicleSpeed = 0;
		setArcHeight(0);
		setArcWidth(0);
	}

	@Override
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
				
				break;

			case SOUTH:
				if (currentTile.getGridPosY() + 1 >= map.length) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() + 1];
				break;

			case EAST:
				if (currentTile.getGridPosX() + 1 >= map.length) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX() + 1][getCurrentTile()
						.getGridPosY()];
				break;

			case WEST:
				if (currentTile.getGridPosX() - 1 < 0) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}
				nextTile = map[getCurrentTile().getGridPosX() - 1][getCurrentTile()
						.getGridPosY()];
				break;

			default:
				break;
			}
			
			if (nextTile != null && nextTile.isOccupied() && nextTile.getOccupier() != null) {
				tempDir = Direction.NONE;
			} else
				tempDir = getDirection();
			
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
