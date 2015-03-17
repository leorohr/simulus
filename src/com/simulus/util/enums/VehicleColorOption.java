package com.simulus.util.enums;

/**
 * Enumeration used to determine the colour of vehicles.
 * @see com.simulus.view.map.Map#updateVehicleColor(Vehicle)
 */
public enum VehicleColorOption {
	SPEED("Speed"), BEHAVIOR("Behavior"), USER("User");
	
	private String name;
	private VehicleColorOption(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
