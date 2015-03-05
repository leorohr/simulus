package com.simulus.util.enums;

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
