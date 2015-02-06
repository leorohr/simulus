package com.simulus.view;


import com.simulus.model.enums.Light;
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
	private Circle light;
	private Light state;
	
	private int width;
	private int height;
	
	private long greenTime = 4000;
	private long redTime = 4000;
	private long amberTime = 1250;
	private long offTime = 500;
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
		light = new Circle(width/3, Color.GRAY);
		state = Light.OFF;
		
		frame.setX(posX);
		frame.setY(posY);
		light.setCenterX(frame.getX()+frame.getWidth()/2);
		light.setCenterY(frame.getY()+frame.getHeight()/2);
		
		Thread t = new Thread(){
			public void run(){
				Light oldState = Light.AMBER;
				try{
					while(true){
						if(state == Light.OFF){
							sleep(offTime);
							if(oldState == Light.AMBER)
								changeLight(Light.GREEN);
							else if(oldState == Light.GREEN)
								changeLight(Light.RED);
							else changeLight(Light.AMBER);
						}else if(state == Light.GREEN){
							sleep(greenTime);
							changeLight(Light.OFF);
							oldState = Light.GREEN;
						}else if(state == Light.AMBER){
							sleep(amberTime);
							changeLight(Light.OFF);
							oldState = Light.AMBER;
						}else if(state == Light.RED){
							sleep(redTime);
							changeLight(Light.OFF);
							oldState = Light.RED;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		this.getChildren().addAll(frame, light);
		t.start();
	}
	/**
	 * Transitions state of the light.
	 * Animates the light to the selected state.
	 * @param state the final state after transition.
	 */
	public void changeLight(Light state){
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
