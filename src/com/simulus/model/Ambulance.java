package com.simulus.model;

import com.simulus.MainApp;
import com.simulus.util.enums.Direction;
import com.simulus.view.VVehicle;

//TODO maybe rename VVehicle to Vehicle etc, since there is no model and view class anymore?
//TODO move other view classes here?
//TODO deprecated classes imported in VVehicle?
public class Ambulance extends VVehicle{

	public Ambulance(double posX, double posY, double width, double height,
			Direction dir, MainApp gui) {
		super(posX, posY, width, height, dir, gui);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void moveVehicle() {
		// TODO Auto-generated method stub
		
	}

}
