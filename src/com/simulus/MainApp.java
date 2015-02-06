package com.simulus;

import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.simulus.util.enums.Direction;
import com.simulus.view.Car;
import com.simulus.view.TrafficLight;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private int frameNo = 0;
	private ArrayList<Car> cars;
	private Car car = null;
	private AnchorPane overview;
	private TrafficLight lights;
	private Rectangle rect;
	private int gridSize;
	private int windowWidth;
	private int windowHeight;
	private static MainApp instance;
	
	
	
	public static MainApp getInstance(){
		return instance;
	}
	
	public MainApp(){
		super();
		synchronized(MainApp.class){
			if(instance != null) throw new UnsupportedOperationException(
					getClass()+" ");
			instance = this;
		}
	}
	@Override
	public void start(final Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Simulus");
		lights = new TrafficLight(100, 100, 20, 60);
		rect = new Rectangle(600, 600, Color.BLACK);
		rect.setFill(Color.TRANSPARENT);
		rect.setStrokeWidth(10);
		rect.setStroke(Color.BLACK);
		
		
		initRootLayout();
		showMainView();

		cars = new ArrayList<Car>();

		/**
		 * Ticking loop
		 */
		AnimationTimer timer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			@Override
			public void handle(long now) {
				//Increment the frame number
				frameNo++;
				//Spawn a car at frame 1
				if (frameNo == 1) {
					cars.add(new Car(primaryStage.getWidth() / 2 - 50, primaryStage.getHeight() / 2, Direction.NORTH));
					System.out.println("Start X Position: "+cars.get(cars.size()-1).getX()+"\nStart Y Position: "+ cars.get(cars.size()-1).getY());
					rootLayout.getChildren().add(cars.get(cars.size() - 1));
					
				}
				
				for (int i = 0; i < cars.size(); i++){
					
					//Move the vehicle at every frame, see Car->moveVehicle()
					cars.get(i).moveVehicle();
					
					//Change the car's direction at specified frame numbers
					if(frameNo == 100){
						cars.get(i).setDirection(Direction.SOUTH);
					}
					if(frameNo == 200){
						cars.get(i).setDirection(Direction.EAST);
					}
					if(frameNo == 350){
						cars.get(i).setDirection(Direction.WEST);
					}
					if(frameNo == 400){
						cars.get(i).setDirection(Direction.NORTH);
					}
					
					//Stop the car ie. at a traffic light
					if(frameNo == 500){
						cars.get(i).setDirection(Direction.NONE);
					}	
					
					//TO DO simulate a car curving across a junction
					if(frameNo == 510){
						//rootLayout.getChildren().add(cars.get(i).curveNorthWest().getPath());
						//cars.get(i).curveNorthWest().play();
					}	
				}
				
				
				
			}
		};
		timer.start();
	}

	/**
	 * Initialise the root layout
	 */
	private void initRootLayout() {

		try {

			/*FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			*/
			rootLayout = new BorderPane();
			//rootLayout.setBorder(new Border());
			Scene scene = new Scene(rootLayout, 600, 600);

	        rootLayout.prefHeightProperty().bind(scene.heightProperty());
	        rootLayout.prefWidthProperty().bind(scene.widthProperty());
			Screen screen = Screen.getPrimary();
	        Rectangle2D bounds = screen.getVisualBounds();
			primaryStage.setScene(scene);
			//primaryStage.setWidth(bounds.getHeight()/2);
			//primaryStage.setHeight(bounds.getWidth()/2);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void setGridSize(int size){
		gridSize = size;
	}

	/**
	 * Shows the cashflow overview interface
	 */
	private void showMainView() {
		try {
			//FXMLLoader loader = new FXMLLoader();
			//loader.setLocation(MainApp.class.getResource("view/MainView.fxml"));
			//overview = (AnchorPane) loader.load();
			// Set overview as center widget of the pane
			//rootLayout.setCenter(overview);
			rootLayout.getChildren().add(lights);
			rootLayout.getChildren().add(rect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
