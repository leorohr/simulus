package com.simulus;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.util.enums.Direction;
import com.simulus.view.Intersection;
import com.simulus.view.Map;
import com.simulus.view.Tile;
import com.simulus.view.TrafficLight;
import com.simulus.view.VCar;
import com.simulus.view.VVehicle;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Pane canvas;
	private int frameNo = 0;
	private ArrayList<VVehicle> cars;
	private AnchorPane overview;
	private TrafficLight lights;
	private Rectangle rect;
	private Group tile;
	private Map map;
	private int gridSize;
	private int canvasWidth = 900;
	private int canvasHeight = 900;
	private int tileWidth;
	
	private static MainApp instance;

	private ArrayList<Intersection> intersections = new ArrayList<>();
	private Tile[][] tiles = new Tile[30][30];

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
		this.cars = new ArrayList<VVehicle>();
		this.map = new Map();

		initRootLayout();
		showControls();

		/**
		 * Ticking loop
		 */
		AnimationTimer timer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			int frameNo = 0;
			Random rand = new Random();

			public void handle(long now) { // Increment the frame number
				frameNo++;

				if (frameNo % 60 == 0) {
					if (Math.random() > 0.5)
						cars.add(new VCar(15 * tileWidth + VCar.CARWIDTH / 4,
								29 * tileWidth + VCar.CARHEIGHT / 8,
								Direction.NORTH, instance));
					else
						cars.add(new VCar(29 * tileWidth + VCar.CARWIDTH / 4,
								15 * tileWidth + VCar.CARHEIGHT / 8,
								Direction.WEST, instance));
				}

				map.createBlockage(frameNo, 300, 15, 15);
				map.createBlockage(frameNo, 300, 16, 15);
				map.createBlockage(frameNo, 300, 14, 15);

				for (VVehicle c : cars) {
					map.updateMap(c);

					if (c.getOnScreen())
						c.moveVehicle();
					else {
						removeVehicle(c);
					}

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
	public void removeVehicle(VVehicle v) {
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

			setGridSize(30);

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
	
	public void setGridSize(int size) {
		gridSize = size;
		tileWidth = canvasWidth / gridSize;
	}

	public int getGridSize() {
		return gridSize;
	}
	
	public int getTileSize() {
		return (int)canvasWidth / gridSize;
	}

	public Pane getCanvas() {
		return canvas;
	}

	public Map getMap() {
		return map;
	}

	public ArrayList<VVehicle> getVehicles() {
		return cars;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
