package com.simulus.view;

import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.PathTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import com.simulus.MainApp;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;

/**
 * Describes a car object on the GUI
 */
@SuppressWarnings("deprecation")
public class Car extends Vehicle {

	public static final int CARWIDTH = 10;
	public static final int CARHEIGHT = 15;	

	private static final int ARCHEIGHT = 10;
	private static final int ARCWIDTH = 10;
	
	private PathTransition pathTransition;
	private boolean isOvertaking = false;
	
	private Behavior behavior = Behavior.getRandomBehavior();

	/**
	 * Amount of pixel movements per tick
	 */
	private double speed = 2;

	private static final Color COLOUR = Color.LIGHTSEAGREEN;

	/**
	 * Sets the appearance, position and direction of the car.
	 * 
	 * @param posX
	 *            the X position of the car on the scene
	 * @param posY
	 *            the Y position of the car on the scene
	 * @param dir
	 *            the direction the car should start moving in
	 */
	public Car(double posX, double posY, Direction dir) {
		super(posX, posY, CARWIDTH, CARHEIGHT, dir);
		switch (dir) {
		case NORTH:
		case SOUTH:
			setWidth(CARWIDTH);
			setHeight(CARHEIGHT);
			break;
		case EAST:
		case WEST:
			setWidth(CARHEIGHT);
			setHeight(CARWIDTH);
			break;
		default:
			break;
		}
		setArcHeight(ARCHEIGHT);
		setArcWidth(ARCWIDTH);
		setFill(COLOUR);
		Random rand = new Random();
		vehicleSpeed = rand.nextInt(3)+2;
		addToCanvas();
		
		MainApp.getInstance().getVehicles().add(this);
	}
	
	

	/**
	 * Translates the vehicle according to the current direction
	 */
	public void moveVehicle() {
		
		if(isOvertaking){
			return;
		}
		
		Translate trans = new Translate();
		
		
		final double dx;
		final double dy;

		Direction temp = getDirection();
		Behavior tempBehavior = behavior;
		
		if(tempBehavior == Behavior.SEMI)
			if(Math.random()>0.5)
				tempBehavior = Behavior.RECKLESS;
			else tempBehavior = Behavior.CAUTIOUS;

		//Checks if the tile 2 tiles ahead of the car is taken for overtaking.
		if(tempBehavior == Behavior.RECKLESS){
			try {
				switch(getDirection()){
				case NORTH:
					if (map[getCurrentTile().getGridPosX()][getCurrentTile()
					                						.getGridPosY() - 2].isOccupied()) {
						if(getCurrentTile() instanceof Lane){
							if(((Lane)getCurrentTile()).getLaneNo() == 0){
								//Overtake RIGHT
								if(!map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1]);
									isOvertaking = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
								//Overtake LEFT
								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
									isOvertaking = true;
								}
							}
						}
					}
					break;
				case SOUTH:
					if (map[getCurrentTile().getGridPosX()][getCurrentTile()
					                						.getGridPosY() + 2].isOccupied()) {
						if(getCurrentTile() instanceof Lane){
							if(((Lane)getCurrentTile()).getLaneNo() == 2){
								//Overtake RIGHT
								if(!map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1]);
									isOvertaking = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
								//Overtake LEFT
								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1]);
									isOvertaking = true;
								}
							}
						}
					}
					break;
				case EAST:
					if (map[getCurrentTile().getGridPosX() + 2][getCurrentTile()
					                    						.getGridPosY()].isOccupied()) {
						
					}
					break;
				case WEST:
					if (map[getCurrentTile().getGridPosX() - 2][getCurrentTile()
					                    						.getGridPosY()].isOccupied()) {
						
					}
					break;
				default:break;
				}
			}catch(ArrayIndexOutOfBoundsException e){
				
			}
		}
		try {
			switch (getDirection()) {
			case NORTH:
				if (map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() - 1].isOccupied()) {
					temp = Direction.NONE;
					if(tempBehavior == Behavior.CAUTIOUS)
						if(map[getCurrentTile().getGridPosX()][getCurrentTile()
						                         						.getGridPosY() - 1].getOccupier()!=null)
						setVehicleSpeed(map[getCurrentTile().getGridPosX()][getCurrentTile()
						                         						.getGridPosY() - 1].getOccupier().getVehicleSpeed());
				} else
					temp = getDirection();
				setOnScreen(true);
				break;
			case SOUTH:
				if (map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() + 1].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				setOnScreen(true);
				break;
			case EAST:
				if (map[getCurrentTile().getGridPosX() + 1][getCurrentTile()
						.getGridPosY()].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				setOnScreen(true);
				break;
			case WEST:
				if (map[getCurrentTile().getGridPosX() - 1][getCurrentTile()
						.getGridPosY()].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				setOnScreen(true);
				break;
			default:
				break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
//			System.out.println("out of screen");
			setOnScreen(false);
		}

		//Moves the car in the direction it should go.
		switch (temp) {
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

	public void removeFromCanvas() {
		switch (getDirection()) {
		case NORTH:
			if (parent.getCanvas().getChildren().contains(this)) {
				parent.getCanvas().getChildren().remove(this);
			}
			break;
		case SOUTH:
			if (parent.getCanvas().getChildren().contains(this)) {
				parent.getCanvas().getChildren().remove(this);
			}
			break;
		case EAST:
			if (parent.getCanvas().getChildren().contains(this)) {
				parent.getCanvas().getChildren().remove(this);
			}
			break;
		case WEST:
			if (parent.getCanvas().getChildren().contains(this)) {
				parent.getCanvas().getChildren().remove(this);
			}
			break;
		default:
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
        double carSpeed = (getVehicleSpeed()/MainApp.getInstance().getTickTime());
        		
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
        
        pathTransition.play();
	}
	
	public void setBehavior(Behavior b){
		behavior = b;
	}
	
	public Behavior getBehavior(){
		return behavior;
	}

	// TODO Curve the car to the northwest
	public PathTransition curveNorthWest() {
		PathTransition pathTransition;
		switch (getDirection()) {
		case NORTH:
			Path path = PathBuilder
					.create()
					.elements(
							// from
							new MoveTo(getX() - 50, getY()),
							new CubicCurveTo(getX(), getY(), getX(),
									getY() - 100, getX() - 100, getY() - 95))
					.build();
			path.setStroke(Color.DODGERBLUE);
			path.getStrokeDashArray().setAll(5d, 5d);

			pathTransition = PathTransitionBuilder.create()
					.duration(Duration.seconds(2)).path(path).node(this)
					.orientation(OrientationType.NONE)
					.interpolator(Interpolator.LINEAR)
					// .cycleCount(Timeline.INDEFINITE)
					.build();
			setDirection(Direction.WEST);
			return pathTransition;
		default:
			return null;
		}
	}
}
