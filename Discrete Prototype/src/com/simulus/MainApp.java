package com.simulus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.controller.SimulationController;
import com.simulus.model.Intersection;
import com.simulus.model.Map;
import com.simulus.model.Vehicle;
import com.simulus.view.Road;
import com.simulus.view.TrafficLight;
import com.simulus.view.VCar;
import com.simulus.view.VIntersection;
import com.simulus.view.VVehicle;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Pane canvas;
	private int frameNo = 0;
	private ArrayList<VVehicle> cars;
	private VCar car = null;
	private AnchorPane overview;
	private TrafficLight lights;
	private Rectangle rect;
	private Group tile;
	private int gridSize;
	private int canvasWidth = 900;
	private int canvasHeight = 900;
	private static MainApp instance;
	private SimulationController controller;
	private int tileWidth;

	private ArrayList<Road> roads = new ArrayList<>();
	private ArrayList<VIntersection> intersections = new ArrayList<>();
	private Rectangle[][] tiles = new Rectangle[30][30];

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
		cars = new ArrayList<VVehicle>();

		initRootLayout();
		showControls();

		// Get SimulationController; initialises controller on first call.
		controller = SimulationController.getInstance();

		/**
		 * Ticking loop
		 */
		/*
		 * AnimationTimer timer = new AnimationTimer() { // When the timer is
		 * started, this method loops endlessly
		 * 
		 * @Override public void handle(long now) { //Increment the frame number
		 * frameNo++; //Spawn a car at frame 1 System.out.println(frameNo); if
		 * (frameNo == 1) { cars.add(new VCar(500, 900, Direction.NORTH));
		 * cars.add(new VCar(500, 800+(90/3), Direction.NORTH)); cars.add(new
		 * VCar(500, 800+(90/3)+(90/3), Direction.NORTH));
		 * System.out.println("Start X Position: "
		 * +cars.get(cars.size()-1).getX()+"\nStart Y Position: "+
		 * cars.get(cars.size()-1).getY()); for(int i = 0; i < cars.size(); i++)
		 * rootLayout.getChildren().add(cars.get(i)); } if(frameNo%8 == 0){
		 * for(int i = 0; i < cars.size(); i++); //cars.get(i).moveVehicle(); }
		 * } }; timer.start();
		 */
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

	public void setGridSize(int size) {
		gridSize = size;
		tileWidth = canvasWidth / gridSize;
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

	public synchronized void redrawCars(List<Vehicle> vehicles) {

		// Clear Screen
		canvas.getChildren().clear();

		// Draw Roads
		for (int i = 0; i < roads.size(); i++)
			canvas.getChildren().add(roads.get(i));
		// Draw Intersections
		for (VIntersection i : intersections)
			canvas.getChildren().add(i);

		// Draw cars
		for (Iterator<Vehicle> it = vehicles.iterator(); it.hasNext();) {
			Vehicle v = it.next();
			VCar car = new VCar(v.getX() * tileWidth, v.getY() * tileWidth,
					v.getDirection());
			switch (car.getDirection()) {
			case NORTH:
				car.setY((v.getY() * tileWidth) + (tileWidth / 2)
						- (car.getHeight() / 2));
				if (v.getLaneID() == 1)
					car.setX((v.getX() * tileWidth) + (tileWidth / 2)
							- (tileWidth / 8) - (car.getWidth() / 2));
				if (v.getLaneID() == 0)
					car.setX((v.getX() * tileWidth) + (tileWidth / 8)
							- (car.getWidth() / 2));
				break;

			case WEST:
				car.setX((v.getX() * tileWidth) + (tileWidth / 2)
						- (car.getWidth() / 2));
				if (v.getLaneID() == 1)
					car.setY((v.getY() * tileWidth) + tileWidth
							- (tileWidth / 8) - (car.getHeight() / 2));
				if (v.getLaneID() == 0)
					car.setY((v.getY() * tileWidth) + ((tileWidth * 3) / 4)
							- (tileWidth / 8) - (car.getHeight() / 2));
				break;

			case SOUTH:
				car.setY((v.getY() * tileWidth) + (tileWidth / 2)
						- (car.getHeight() / 2));
				if (v.getLaneID() == 1)
					car.setX((v.getX() * tileWidth) + tileWidth
							- (tileWidth / 8) - (car.getWidth() / 2));
				if (v.getLaneID() == 0)
					car.setX((v.getX() * tileWidth) + ((tileWidth * 3) / 4)
							- (tileWidth / 8) - (car.getWidth() / 2));
				break;

			case EAST:
				car.setX((v.getX() * tileWidth) + (tileWidth / 2)
						- (car.getWidth() / 2));
				if (v.getLaneID() == 1)
					car.setY((v.getY() * tileWidth) + (tileWidth / 2)
							- (tileWidth / 8) - (car.getHeight() / 2));
				if (v.getLaneID() == 0)
					car.setY((v.getY() * tileWidth) + (tileWidth / 8)
							- (car.getHeight() / 2));
				break;

			default:
				break;
			}

			cars.add(car);
			canvas.getChildren().add(car);
		}
	}

	public void switchLights(Intersection is) {
		Platform.runLater(new Runnable() {
			public void run() {
				for (VIntersection vI : intersections)
					if (is.getId() == vI.getIntersectionId())
						vI.switchLights();
			}
		});

	}

	public void readMap() {
		Map map = Map.getInstance();
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				com.simulus.model.Road r = map.getTile(i, j).content;
				if (r == null)
					continue;
				if (r instanceof Intersection) {
					intersections.add(new VIntersection(i * tileWidth, j
							* tileWidth, tileWidth, tileWidth, r.getId()));
				} else if (r instanceof com.simulus.model.Road) {
					roads.add(new Road(i * tileWidth, j * tileWidth, tileWidth,
							tileWidth, r.getSeed()));
				}
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
