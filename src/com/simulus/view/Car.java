package com.simulus.view;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.PathTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.Random;

/**
 * Describes a car object on the GUI
 */
@SuppressWarnings("deprecation")
public class Car extends Vehicle {

	public static final int CARWIDTH = 10;
	public static final int CARLENGTH = 15;	

	private static final int ARCHEIGHT = 10;
	private static final int ARCWIDTH = 10;
	
	private PathTransition pathTransition;

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
		super(posX, posY, CARWIDTH, CARLENGTH, dir);
		
		behavior = Behavior.getRandomBehavior();
		
		switch (dir) {
		case NORTH:
		case SOUTH:
			setWidth(CARWIDTH);
			setHeight(CARLENGTH);
			break;
		case EAST:
		case WEST:
			setWidth(CARLENGTH);
			setHeight(CARWIDTH);
			break;
		default:
			break;
		}
		setArcHeight(ARCHEIGHT);
		setArcWidth(ARCWIDTH);
		setFill(COLOUR);
		Random rand = new Random();
		vehicleSpeed = rand.nextInt(SimulationController.getInstance().getMaxCarSpeed()-3)+3;
		addToCanvas();
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

//		Checks if the tile 2 tiles ahead of the car is taken for overtaking.
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
						if(getCurrentTile() instanceof Lane){
							if(((Lane)getCurrentTile()).getLaneNo() == 0){
								//Overtake DOWN
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()+1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1]);
									isOvertaking = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
								//Overtake UP
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1]);
									isOvertaking = true;	
							}
						}
					}
					}
					break;
				case WEST:
					if (map[getCurrentTile().getGridPosX() - 2][getCurrentTile()
					                    						.getGridPosY()].isOccupied()) {
						if(getCurrentTile() instanceof Lane){
							if(((Lane)getCurrentTile()).getLaneNo() == 2){
								//Overtake DOWN
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()+1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1]);
									isOvertaking = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
								//Overtake UP
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
									isOvertaking = true;	
							}
						}
					}
					}
					break;
				default:break;
				}
			}catch(ArrayIndexOutOfBoundsException e){
				SimulationController.getInstance().removeVehicle(this);
			}
		}
		try {
            Tile nextTile = null;
			switch (getDirection()) {
			case NORTH:
                if(currentTile.getGridPosY()-1 < 0) {
                    SimulationController.getInstance().removeVehicle(this);
                    break;
                }

                nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY() - 1];
                if (nextTile.isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				break;

			case SOUTH:
                if(currentTile.getGridPosY()+1 >= map.length) {
                    SimulationController.getInstance().removeVehicle(this);
                    break;
                }

                nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY() + 1];
                if (nextTile.isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				break;

			case EAST:
                if(currentTile.getGridPosX()+1 >= map.length) {
                    SimulationController.getInstance().removeVehicle(this);
                    break;
                }

                nextTile = map[getCurrentTile().getGridPosX() + 1][getCurrentTile().getGridPosY()];
				if (nextTile.isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				break;

			case WEST:
                if(currentTile.getGridPosX()-1 < 0) {
                    SimulationController.getInstance().removeVehicle(this);
                    break;
                }

                nextTile = map[getCurrentTile().getGridPosX() - 1][getCurrentTile().getGridPosY()];
				if (nextTile.isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				break;

			default:
				break;
			}

            //Slow the car down if cautious and slow car in front
            if(tempBehavior == Behavior.CAUTIOUS)
                if(nextTile != null && nextTile.getOccupier()!=null)
                    setVehicleSpeed(nextTile.getOccupier().getVehicleSpeed());

		} catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
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
