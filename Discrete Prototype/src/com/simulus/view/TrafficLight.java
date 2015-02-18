package com.simulus.view;


import com.simulus.util.enums.Light;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
/**
 * Describes a traffic light made up of a circle within a rectangle
 */
public class TrafficLight extends Group{

	private Rectangle frame;
	private Circle middleLight;
	private Circle upLight;
	private Circle downLight;
	private Light state;
	
	private int width;
	private int height;
	
	private long greenTime = 4000;
	private long redTime = 4000;
	private long amberTime = 1250;
	private long offTime = 200;
	/**
	 * Creates a traffic light at posX/Y and of specified width and height. 
	 * The traffic light will automatically change state.
	 * @param posX the X position of the top left corner of the frame
	 * @param posY the Y position of the top left corner of the frame
	 * @param width the width of the frame
	 * @param height the height of the frame
	 */
	public TrafficLight(int posX, int posY, int width, int height){
		
		this.width = width;
		this.height = height;
		
		frame = new Rectangle(width, height, Color.BLACK);
		frame.setArcHeight(10);
		frame.setArcWidth(10);
		middleLight = new Circle(height/8, Color.GRAY);
		upLight = new Circle(height/8, Color.GRAY);
		downLight = new Circle(height/8, Color.GRAY);
		state = Light.OFF;
		
		frame.setX(posX);
		frame.setY(posY);
		
		middleLight.setCenterX(frame.getX()+frame.getWidth()/2);
		middleLight.setCenterY(frame.getY()+frame.getHeight()/2);
		
		upLight.setCenterX(frame.getX()+frame.getWidth()/2);
		upLight.setCenterY(frame.getY()+frame.getHeight()/4);
	
		
		downLight.setCenterX(frame.getX()+frame.getWidth()/2);
		downLight.setCenterY(frame.getY()+(frame.getHeight()*3)/4);
		
		
		Thread t = new Thread(){
			public void run(){
				Light oldState = Light.AMBER;
				try{
					while(true){
						if(state == Light.OFF){
							sleep(offTime);
							if(oldState == Light.AMBER)
								changeLight(Light.GREEN, upLight);
							else if(oldState == Light.GREEN)
								changeLight(Light.RED, downLight);
							else changeLight(Light.AMBER, middleLight);
						}else if(state == Light.GREEN){
							sleep(greenTime);
							changeLight(Light.OFF, upLight);
							oldState = Light.GREEN;
						}else if(state == Light.AMBER){
							sleep(amberTime);
							changeLight(Light.OFF, middleLight);
							oldState = Light.AMBER;
						}else if(state == Light.RED){
							sleep(redTime);
							changeLight(Light.OFF, downLight);
							oldState = Light.RED;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		this.getChildren().addAll(frame, middleLight, upLight, downLight);
		t.start();
	}
	/**
	 * Transitions state of the light.
	 * Animates the light to the selected state.
	 * @param state the final state after transition.
	 */
	public void changeLight(Light state, Circle light){
		this.state = state;
		
		FillTransition fill = new FillTransition(Duration.millis(300));
		ParallelTransition transition;
		switch(state){
		case OFF:
			fill.setToValue(Color.GRAY);
			transition = new ParallelTransition(light, fill);
			transition.play();
			break;
		case GREEN:
			fill.setToValue(Color.GREEN);
			transition = new ParallelTransition(light, fill);
			transition.play();
			break;
		case RED:
			fill.setToValue(Color.RED);
			transition = new ParallelTransition(light, fill);
			transition.play();
			break;
		case AMBER:
			fill.setToValue(Color.ORANGE);
			transition = new ParallelTransition(light, fill);
			transition.play();
			break;
		}
	}
	/**
	 * Gets the width of the frame
	 * @return frame width
	 */
	public int getWidth(){
		return width;
	}
	/**
	 * gets the height of the frame
	 * @return frame height
	 */
	public int getHeight(){
		return height;
	}
}
