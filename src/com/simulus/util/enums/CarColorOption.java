package com.simulus.util.enums;

public enum CarColorOption {
	BEHAVIOR("Behavior"), SPEED("Speed"), USER("User");
	
	private String name;
	private CarColorOption(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
