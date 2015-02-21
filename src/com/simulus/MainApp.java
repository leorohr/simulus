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

import com.simulus.util.enums.Direction;
import com.simulus.view.Map;
import com.simulus.view.Tile;
import com.simulus.view.Car;
import com.simulus.view.Vehicle;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Pane canvas;
	private ArrayList<Vehicle> cars;
	private Map map;
	private int canvasWidth = 900;
	private int canvasHeight = 900;
	public double tickTime = 20;
	private int spawnTimer = 30;
	
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
		
		initRootLayout();
		showControls();
		
		this.cars = new ArrayList<Vehicle>();
		this.map = new Map();

		/**
		 * Ticking loop
		 */
		AnimationTimer timer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			int frameNo = 0;
			

			public void handle(long now) { // Increment the frame number
				frameNo++;
				
				long startTime = System.currentTimeMillis();

				
				if (frameNo % spawnTimer == 0) {
					map.spawnTesterCar();
				}
				
				for (Vehicle c : cars) {
					map.updateMap(c);
					
					if (c.getOnScreen())
						c.moveVehicle();
					else {
						removeVehicle(c);
					}
					
				}
				//Ensures a fixed Tickrate
				long end = System.nanoTime();
				while(System.nanoTime() - now < ((tickTime*1000000)- (end-now))){
					//sleep
				}
			}
		};
		timer.start();
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

	public Pane getCanvas() {
		return canvas;
	}

	public Map getMap() {
		return map;
	}

	public ArrayList<Vehicle> getVehicles() {
		return cars;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
