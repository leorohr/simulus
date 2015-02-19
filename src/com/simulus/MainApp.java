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
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.view.Map;
import com.simulus.view.Tile;
import com.simulus.view.VVehicle;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Pane canvas;
	private Map map;
	private int gridSize;
	private int canvasWidth = 900;
	private int canvasHeight = 900;
	private int tileWidth;
	
	private boolean landSelected = false;
	private boolean roadSelected = false;
	private boolean interSelected = false;
	
	private static MainApp instance;

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
		this.map = new Map();

		initRootLayout();
		showControls();
		
		map.addMap();

		/**
		 * Ticking loop
		 */
		AnimationTimer timer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			int frameNo = 0;
			Random rand = new Random();

			public void handle(long now) { // Increment the frame number
				frameNo++;

			}
		};
		timer.start();
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
	
	public void selectButton(Button b){
		switch(b.getId()){
		case "landButton":
			System.out.println("Clicked Land Button");
			landSelected = true;
			roadSelected = false;
			interSelected = false;
			break;
		case "roadButton":
			System.out.println("Clicked Road Button");
			landSelected = false;
			roadSelected = true;
			interSelected = false;
			break;
		case "interButton":
			System.out.println("Clicked Intersection Button");
			landSelected = false;
			roadSelected = false;
			interSelected = true;
			break;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
