package com.simulus.view.vehicles;

import javafx.application.Platform;
import javafx.scene.shape.Circle;

import com.simulus.controller.SimulationController;
import com.simulus.view.map.Lane;

public class AreaOfEffect extends Circle{
	
	
	public AreaOfEffect(double centerX, double centerY, double radius, Ambulance a){
		super(centerX, centerY, radius);
	}
	
	public void updatePosition(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setCenterX(getCar().getBoundsInParent().getMaxX() - getCar().getWidth()/2);
				setCenterY(getCar().getBoundsInParent().getMaxY() - getCar().getHeight()/2);
			}
		});	
		
		for(Vehicle v: SimulationController.getInstance().getMap().getVehicles()){
			
			if(this.getBoundsInParent().intersects(v.getBoundsInParent()) && !(v instanceof EmergencyCar)){
				if(v.getDirection() == getCar().getDirection() && (v.getCurrentTile()  instanceof Lane) && getCar().getCurrentTile() instanceof Lane) {						
					
					Lane l = (Lane) v.getCurrentTile();
					if(l.getLaneNo() ==  ((Lane)getCar().getCurrentTile()).getLaneNo()) //if v is in same lane, force it over
						v.changeLane();
					else //if car is in adjacent lane, make it slow down and allow cars to merge
						v.allowMerge();
				}
			}
		}
	}
	
	/**
	 * @return The EmergencyCar object that this AoE is associated to.
	 */
	private EmergencyCar getCar() {
		return ((Ambulance) getParent()).getCar();
	}

}
