package com.simulus;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.view.Map;
import com.simulus.view.Tile;
import com.simulus.view.Vehicle;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Pane canvas;
	private ArrayList<Vehicle> vehicles;
	private Map map;
	private AnimationTimer animationTimer;
	private boolean debugFlag = false;
	private int canvasWidth = 800;
	private int canvasHeight = 800;
	private int tickTime = 50; //in ms
	private int spawnRate = 5; //a new car spawns every spawnRate'th frame
	private int maxCars = 50;
	private int maxCarSpeed = 10;
	private double carTruckRatio = 0.7d;
	private int truckCount = 0;
	private static MainApp instance;

	public static MainApp getInstance() {
		return instance;
	}

	public MainApp() {
		super();

		// Synchronized call to access the application's instance
		synchronized (MainApp.class) {
			if (instance != null)
				throw new UnsupportedOperationException(getClass() + " ");
			instance = this;
		}
	}
	
	@Override
	public void start(final Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Simulus");
		this.primaryStage.setResizable(false);
		
		initRootLayout();
		showControls();
		
		this.vehicles = new ArrayList<Vehicle>();
		this.map = new Map();

		/**
		 * Ticking loop
		 */
		animationTimer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			int frameNo = 0;
			

			public void handle(long now) { // Increment the frame number
				frameNo++;

				if (frameNo % spawnRate == 0 && vehicles.size() < maxCars) {
					//If the car-truck ratio is not correct, spawn a truck, otherwise a car.
					if(truckCount < (1-carTruckRatio) * vehicles.size()) {
						map.spawnRandomTruck();
						truckCount++;
					}
					else {
						map.spawnRandomCar(); 
					}
				}
				
				for (Vehicle v : vehicles) {
					map.updateMap(v);

					if (v.getOnScreen())
						v.moveVehicle();
					else {
						removeVehicle(v);
					}
					
				}
				
				//Ensures a fixed tickrate
				long end = System.nanoTime();
				while(System.nanoTime() - now < (tickTime * 1000000 - (end - now))){
					
				}
			}
		};
		animationTimer.start();
	}
	
	/**
	 * Removes a vehicle from the screen
	 * 
	 * @param v
	 *            Vehicle to be removed
	 */
	public void removeVehicle(Vehicle v) {
		v.removeFromCanvas();
		v.getCurrentTile().setOccupied(false, v);

		for (Tile t : v.getOccupiedTiles())
			t.setOccupied(false, v);
	}

	/**
	 * Initialise the root layout
	 */
	private void initRootLayout() {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			canvas = new Pane();
			canvas.setMinSize(canvasWidth, canvasHeight);
			canvas.setPrefSize(canvasWidth, canvasHeight);
			canvas.setMaxSize(canvasWidth, canvasHeight);
			rootLayout.setCenter(canvas);

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void showControls() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Controls.fxml"));
			AnchorPane controls = (AnchorPane) loader.load();
			rootLayout.setRight(controls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startSimulation() {
		animationTimer.start();
	}
	
	public void stopSimulation() {
		animationTimer.stop();
	}

	public Pane getCanvas() {
		return canvas;
	}

	public Map getMap() {
		return map;
	}

	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	public int getTickTime() {
		return tickTime;
	}
	
	public void setTickTime(int tickTime) {
		this.tickTime = tickTime;
	}
	
	public void setSpawnrate(int spawnRate) {
		this.spawnRate = spawnRate;
	}
	
	public boolean isDebug() {
		return debugFlag;
	}
	
	public void setDebug(boolean debugFlag) {
		this.debugFlag = debugFlag;
	}
	
	public int getMaxCarSpeed() {
		return maxCarSpeed;
	}
	
	public void setMaxCarSpeed(int maxSpeed) {
		this.maxCarSpeed = maxSpeed;
	}
	
	public void setMaxCars(int maxCars) {
		this.maxCars = maxCars;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
