package com.simulus;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.util.enums.Seed;
import com.simulus.view.Intersection;
import com.simulus.view.Map;
import com.simulus.view.Road;
import com.simulus.view.Tile;
import com.simulus.view.Vehicle;
import com.simulus.io.MapXML;

public class EditorApp extends Application {

	private Stage editorStage;
	private BorderPane rootLayout;
	private Pane canvas;
	private Map editorMap;
	private int gridSize;
	//fixed size canvas
	private int canvasWidth = 800;
	private int canvasHeight = 800;
	private int tileWidth;
	private Scene scene;
	
	private Image csrRoadEW = new Image("com/simulus/util/images/csr_eastwest.png");
	private Image csrRoadNS = new Image("com/simulus/util/images/csr_northsouth.png");
	private Image csrLand = new Image("com/simulus/util/images/csr_land.png");
	private Image csrIntersection = new Image("com/simulus/util/images/csr_boxjunction.png");
	

	private boolean landSelected = false;
	private boolean roadVerticalSelected = false;
	private boolean roadHorizontalSelected = false;
	private boolean interSelected = false;
	

	private static EditorApp instance;

	public static EditorApp getInstance() {
		return instance;
	}

	public EditorApp() {
		super();

		// Synchronized call to access the application's instance
		synchronized (EditorApp.class) {
			if (instance != null)
				throw new UnsupportedOperationException(getClass() + " ");
			instance = this;
		}
	}

	@Override
	public void start(final Stage editorStage) {

		this.editorStage = editorStage;
		this.editorStage.setTitle("Simulus - Map Editor");

		initRootLayout();
		showControls();

		this.editorMap = new Map();

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for (int i = 0; i < editorMap.getTiles().length; i++)
					for (int p = 0; p < editorMap.getTiles()[0].length; p++) {
						if (event.getSceneX() > editorMap.getTiles()[i][p]
								.getBoundsInParent().getMinX()
								&& event.getSceneX() < editorMap.getTiles()[i][p]
										.getBoundsInParent().getMaxX()
								&& event.getSceneY() > editorMap.getTiles()[i][p]
										.getBoundsInParent().getMinY()
								&& event.getSceneY() < editorMap.getTiles()[i][p]
										.getBoundsInParent().getMaxY())
							if (landSelected) {
								System.out.println("Adding land at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.getTiles()[i][p].setOccupied(true);
							} else if (interSelected) {
								System.out.println("Adding intersection at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addGroup(new Intersection(editorMap
										.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p]
												.getGridPosY()));
							} else if (roadVerticalSelected) {
								System.out.println("Adding road at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addGroup(new Road(editorMap
										.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p]
												.getGridPosY(), Seed.NORTHSOUTH));
							} else if (roadHorizontalSelected) {
								System.out.println("Adding road at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addGroup(new Road(editorMap
										.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p]
												.getGridPosY(), Seed.WESTEAST));
							}
					}
			}
		});

		/*
		 * Drag to draw for Land and road tiles
		 */
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				for (int i = 0; i < editorMap.getTiles().length; i++) {
					for (int p = 0; p < editorMap.getTiles()[0].length; p++) {
						if (event.getSceneX() > editorMap.getTiles()[i][p]
								.getBoundsInParent().getMinX()
								&& event.getSceneX() < editorMap.getTiles()[i][p]
										.getBoundsInParent().getMaxX()
								&& event.getSceneY() > editorMap.getTiles()[i][p]
										.getBoundsInParent().getMinY()
								&& event.getSceneY() < editorMap.getTiles()[i][p]
										.getBoundsInParent().getMaxY()) {

							if (landSelected) {
								System.out.println("Adding land at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.getTiles()[i][p].setOccupied(true);
							} else if (roadVerticalSelected) {
								System.out.println("Adding road at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addGroup(new Road(editorMap
										.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p]
												.getGridPosY(), Seed.NORTHSOUTH));
							} else if (roadHorizontalSelected) {
								System.out.println("Adding road at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addGroup(new Road(editorMap
										.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p]
												.getGridPosY(), Seed.WESTEAST));
							}
						}
					}
				}
			}

		});
			

		/**
		 * Ticking loop
		 */
		AnimationTimer timer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			int frameNo = 0;
			Random rand = new Random();

			public void handle(long now) { // Increment the frame number
				getCanvas().getChildren().clear();
				editorMap.drawMap();
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
			loader.setLocation(EditorApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			canvas = new Pane();
			canvas.setMinSize(canvasWidth, canvasHeight);
			canvas.setPrefSize(canvasWidth, canvasHeight);
			canvas.setMaxSize(canvasWidth, canvasHeight);
			rootLayout.setCenter(canvas);

			setGridSize(30);

			scene = new Scene(rootLayout);
			editorStage.setScene(scene);
			editorStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});
			editorStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showControls() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(EditorApp.class
					.getResource("view/EditorControls.fxml"));
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
		return (int) canvasWidth / gridSize;
	}

	public Pane getCanvas() {
		return canvas;
	}

	public Map getMap() {
		return editorMap;
	}

	public void selectButton(Button b) {
		switch (b.getId()) {
		case "landButton":
			System.out.println("Clicked Land Button");
			landSelected = true;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrLand));
			break;
		case "roadVerticalButton":
			System.out.println("Clicked Vertical Road Button");
			landSelected = false;
			roadVerticalSelected = true;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrRoadNS));
			break;
		case "roadHorizontalButton":
			System.out.println("Clicked Horizontal Road Button");
			landSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = true;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrRoadEW));
			break;
		case "interButton":
			System.out.println("Clicked Intersection Button");
			landSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = true;
			canvas.setCursor(new ImageCursor(csrIntersection));
			break;
		case "openMapButton":
			System.out.println("Clicked Open Map Button");
			 FileChooser fileChooser = new FileChooser();
			 fileChooser.setTitle("Open Resource File");
             FileChooser.ExtensionFilter extFilter = 
                     new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
             fileChooser.getExtensionFilters().add(extFilter);
			 File selectedFile = fileChooser.showOpenDialog(editorStage);
			 if (selectedFile != null) {
			    loadMap(selectedFile.getPath());
			 }
			break;
		case "saveMapButton":
			System.out.println("Clicked Save Map Button");
			
			System.out.println(editorMap.getTiles()[0][0].isOccupied());
			System.out.println(editorMap.getTiles()[0][0].getGridPosX());
			System.out.println(editorMap.getTiles()[0][0].getGridPosY());
			System.out.println(editorMap.getTiles()[0][0].getX());
			System.out.println(editorMap.getTiles()[0][0].getY());
			
			break;
		default:
			break;
		}
	}
	
	
	public void loadMap(String fileLocation){
		MapXML mxml = new MapXML();
		mxml.readXML(fileLocation);
		System.out.println(mxml.toString());
		
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
