package com.simulus.util.enums;

public enum VehicleColorOption {
	BEHAVIOR("Behavior"), SPEED("Speed"), USER("User");
	
	private String name;
	private VehicleColorOption(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
