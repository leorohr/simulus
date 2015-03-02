package com.simulus.view;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Direction;

public class Ambulance extends Group {
	
	private boolean isColoured = false;
	private AreaOfEffect AoE;
	private Car frame;
	
	public static double AoERadius = 100;
	
	
	private static final Color COLOUR = Color.YELLOW;

	public Ambulance(double posX, double posY, Direction dir) {
		frame = new Car(posX, posY, dir, this);
		frame.setFill(COLOUR);
		frame.vehicleSpeed = SimulationController.getInstance().getMaxCarSpeed();
		frame.setArcHeight(0);
		frame.setArcWidth(0);
		
		AoE = new AreaOfEffect(frame.getX() + frame.getWidth()/2, frame.getY() + frame.getHeight()/2, AoERadius, this);
		AoE.setFill(Color.ORANGERED);
		AoE.setOpacity(0.25);
		this.getChildren().add(frame);
		getChildren().add(AoE);
		MainApp.getInstance().getCanvas().getChildren().add(this);
		
			
		
		
		Thread t = new Thread(){
			public void run(){
				while(!isInterrupted())
					try{
						sleep(600);
						changeColourTo();
						sleep(600);
						changeColourTo();
					}catch(Exception e){
						e.printStackTrace();
					}
			}
		};
		t.start();
		
	}
	
	public void changeColourTo(){
		FillTransition fill = new FillTransition(Duration.millis(300));
		ParallelTransition transition;
		if(!isColoured){
			fill.setToValue(Color.BLUE);
			transition = new ParallelTransition(frame, fill);
			transition.play();
			isColoured = true;
		}else{
			fill.setToValue(Color.YELLOW);
			transition = new ParallelTransition(frame, fill);
			transition.play();
			isColoured = false;
		}
	}
	
	public void updateAoE(){
		AoE.updatePosition();
	}
	
	public Car getCar(){
		return frame;
	}
	
	public Circle getAoE(){
		return AoE;
	}
}
