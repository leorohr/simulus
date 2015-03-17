package com.simulus.util.enums;

import java.util.Random;

/**
 * The behaviour types for cars.
 */
public enum Behavior {
	CAUTIOUS, RECKLESS, SEMI;
	
	public static Behavior getRandomBehavior(){
		Random rand = new Random();
		int choice = rand.nextInt(3);
		if(choice == 0)
			return CAUTIOUS;
		if(choice == 1)
			return RECKLESS;
		if(choice == 2)
			return SEMI;
		return CAUTIOUS;
	}
}
