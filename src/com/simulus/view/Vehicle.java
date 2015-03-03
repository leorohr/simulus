package com.simulus.view;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransitionBuilder;
import javafx.animation.PathTransition.OrientationType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a vehicle on the GUI
 */
public abstract class Vehicle extends Rectangle {

	protected Direction dir;
	protected int mapSize;
	protected Tile[][] map;
	protected MainApp parent;
	protected Tile currentTile;
	protected List<Tile> occupiedTiles;
	protected boolean isOvertaking = false;
	protected Behavior behavior;
	protected Rectangle frame;
	protected boolean isPaused = false;
	protected double vehicleSpeed;
	protected boolean skipLights = false;
	protected PathTransition pathTransition;
	
	protected double temporarySpeed;
	
	protected Behavior tempBehavior;
	protected Direction tempDir;

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
	public Vehicle(double posX, double posY, double width, double height,
			Direction dir) {
		super(posX, posY, width, height);
		frame = new Rectangle();
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

	public void move(Direction d) {
		Translate trans = new Translate();

		final double dx;
		final double dy;
		// Moves the car in the direction it should go.
		switch (d) {
		case NORTH:

			dx = 0;
			dy = -getVehicleSpeed();

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);

			break;
		case SOUTH:
			dx = 0;
			dy = getVehicleSpeed();

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);

			break;
		case EAST:
			dx = getVehicleSpeed();
			dy = 0;

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			break;
		case WEST:
			dx = -getVehicleSpeed();
			dy = 0;

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);

			break;
		case NONE:

			dx = 0;
			dy = 0;

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			break;
		}
	}

	public void overtake(Tile moveToTile){
		getTransforms().clear();
		Path path = new Path(
                		//from 
                		new MoveTo(getCurrentTile().getCenterX(), getCurrentTile().getCenterY()),
                        		
                        new LineTo(moveToTile.getCenterX(), moveToTile.getCenterY())
                    
                );
		
               
        path.setStroke(Color.DODGERBLUE);
        path.getStrokeDashArray().setAll(5d,5d);
        MainApp.getInstance().getCanvas().getChildren().add(path);
       
        double pathDistance = Math.sqrt(Math.pow(path.getBoundsInParent().getMaxX()-path.getBoundsInParent().getMinX(), 2)
        		+Math.pow(path.getBoundsInParent().getMinY()-path.getBoundsInParent().getMaxY(), 2));
        double carSpeed = (getVehicleSpeed()/SimulationController.getInstance().getTickTime());
        		
        double pathTime = pathDistance/carSpeed;
        
        
        
        pathTransition = PathTransitionBuilder.create()
                .duration(Duration.millis(pathTime))
                .path(path)
                .node(this)
                .interpolator(Interpolator.LINEAR)
                .orientation(OrientationType.NONE)
                .build();
        
        pathTransition.setOnFinished(new EventHandler<ActionEvent>(){
 
            @Override
            public void handle(ActionEvent arg0) {
            	MainApp.getInstance().getCanvas().getChildren().remove(path);
                isOvertaking = false;
            }
        });
        setCurrentTile(moveToTile);
        pathTransition.play();
	};

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
		addTile(t); // add current tile to list of occupied tiles
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

	private void addTile(Tile t) {
		synchronized (this) {
			if (!occupiedTiles.contains(t))
				occupiedTiles.add(t);
		}
	}

	public void removeTile(Tile t) {
		synchronized (this) {
			if (occupiedTiles.contains(t))
				occupiedTiles.remove(t);
		}
	}

	public List<Tile> getOccupiedTiles() {
		synchronized (this) {
			return occupiedTiles;
		}
	}

	public double getVehicleSpeed() {
		return vehicleSpeed;
	}

	public void setVehicleSpeed(double d) {
		vehicleSpeed = d;
	}

	public void setTemporarySpeed(double d) {
		temporarySpeed = d;
	}

	public void setAmbulanceMode(boolean b) {
		isPaused = b;
	}

	public void setSkipLights(boolean b) {
		skipLights = b;
	}
}
