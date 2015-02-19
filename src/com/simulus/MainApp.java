package com.simulus;

import java.util.ArrayList;

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

import com.simulus.view.Tile;
import com.simulus.view.VVehicle;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Pane canvas;
	private int frameNo = 0;
	private ArrayList<VVehicle> cars;
	private AnchorPane overview;
	private Rectangle rect;
	private Group tile;
	private int gridSize;
	private int canvasWidth = 900;
	private int canvasHeight = 900;
	private static MainApp instance;
	private int tileWidth;
	VVehicle car;

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
		cars = new ArrayList<VVehicle>();

		initRootLayout();
		showControls();

		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles.length; p++) {
				tiles[i][p] = new Tile(i * tileWidth, p * tileWidth, tileWidth,
						tileWidth, i, p);
				canvas.getChildren().add(tiles[i][p]);
			}
		}

		/**
		 * Ticking loop
		 */
		AnimationTimer timer = new AnimationTimer() {
			
			public void handle(long now) { // Increment the frame number
				
			}
		};
		timer.start();
	}

	/**
	 * Initialise the root layout
	 */
	private void initRootLayout() {

		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(MainApp.class
			 * .getResource("view/RootLayout.fxml")); rootLayout = (BorderPane)
			 * loader.load();
			 */

			rootLayout = new BorderPane();
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

	public void setGridSize(int size) {
		gridSize = size;
		tileWidth = canvasWidth / gridSize;
	}

	public int getGridSize() {
		return gridSize;
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

	public Tile[][] getMap() {
		return tiles;
	}


	public static void main(String[] args) {
		launch(args);
	}
}
