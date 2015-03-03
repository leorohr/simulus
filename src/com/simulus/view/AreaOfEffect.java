package com.simulus.view;

import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Direction;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class AreaOfEffect extends Circle{
	
	
	public AreaOfEffect(double centerX, double centerY, double radius, Ambulance a){
		super(centerX, centerY, radius);
	}
	
	public void updatePosition(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setCenterX(((Ambulance) getParent()).getCar().getBoundsInParent().getMaxX() - ((Ambulance) getParent()).getCar().getWidth()/2);
				setCenterY(((Ambulance) getParent()).getCar().getBoundsInParent().getMaxY() - ((Ambulance) getParent()).getCar().getHeight()/2);
			}
		});	
		
		for(Vehicle v: SimulationController.getInstance().getMap().getVehicles()){
			if(this.getBoundsInParent().intersects(v.getBoundsInParent()) && !(v instanceof EmergencyCar)){
					//v.setAmbulanceMode(true);
					if(v.getDirection() == ((Ambulance) getParent()).getCar().getDirection()
							&& v instanceof Car)
						((Car) v).attemptOvertake();
			}
			else; //v.setAmbulanceMode(false);
		}
	}
}
