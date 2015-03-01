package com.simulus.view;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Direction;

public class Ambulance extends Car {
	
	private boolean isRed = false;
	private Circle AoE;
	
	public static double AoERadius = 100;
	
	
	private static final Color COLOUR = Color.WHITE;

	public Ambulance(double posX, double posY, Direction dir) {
		super(posX, posY, dir);
		frame.setFill(COLOUR);
		vehicleSpeed = SimulationController.getInstance().getMaxCarSpeed();
		frame.setArcHeight(0);
		frame.setArcWidth(0);
		
		AoE = new Circle(frame.getX() + frame.getWidth()/2, frame.getY() + frame.getHeight()/2, AoERadius);
		AoE.setFill(Color.ORANGERED);
		AoE.setOpacity(0.25);
		this.getChildren().add(AoE);
		
		Thread t = new Thread(){
			public void run(){
				while(!isInterrupted())
					try{
						sleep(600);
						changeColourTo(Color.RED);
						sleep(600);
						changeColourTo(Color.WHITE);
					}catch(Exception e){
						e.printStackTrace();
					}
			}
		};
		t.start();
	}
	
	public void changeColourTo(Color c){
		FillTransition fill = new FillTransition(Duration.millis(300));
		ParallelTransition transition;
		if(!isRed){
			fill.setToValue(Color.RED);
			transition = new ParallelTransition(frame, fill);
			transition.play();
			isRed = true;
		}else{
			fill.setToValue(Color.WHITE);
			transition = new ParallelTransition(frame, fill);
			transition.play();
			isRed = false;
		}
	}
}
