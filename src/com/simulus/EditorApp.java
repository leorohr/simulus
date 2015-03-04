package com.simulus;

import java.io.File;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.controller.EditorControlsController;
import com.simulus.io.MapXML;
import com.simulus.io.TileXML;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;
import com.simulus.view.Intersection;
import com.simulus.view.Land;
import com.simulus.view.Lane;
import com.simulus.view.Map;
import com.simulus.view.Road;
import com.simulus.view.Tile;

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
	private Image csrDirt = new Image("com/simulus/util/images/csr_dirt.png");
	private Image csrBlock = new Image("com/simulus/util/images/csr_block.png");
	private Image csrIntersection = new Image("com/simulus/util/images/csr_boxjunction.png");
	private Image csrErase = new Image("com/simulus/util/images/csr_erase.png");

	private boolean landSelected = false;
	private boolean eraserSelected = false;
	private boolean dirtSelected = false;
	private boolean blockSelected = false;
	private boolean roadVerticalSelected = false;
	private boolean roadHorizontalSelected = false;
	private boolean interSelected = false;

	FileChooser fileChooser = new FileChooser();
	FileChooser.ExtensionFilter extFilter;
	File selectedFile;


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
		// TODO: fix resizable issue with mouse position?
		this.editorStage.setResizable(false);

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
														.getBoundsInParent().getMaxY()) {
							if (landSelected) {
								System.out.println("Adding land at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.getTiles()[i][p].setOccupied(true);
								
							} else if (dirtSelected) {
								System.out.println("Adding dirt at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.getTiles()[i][p].setOccupied(true);
							} else if (blockSelected) {
								System.out.println("Adding block at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.getTiles()[i][p].setOccupied(true);
							} else if (eraserSelected) {
								// TODO implement remove properly
								System.out.println("Removing at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.removeGroup(new Road(editorMap.getTiles()[i][p]
										.getGridPosX(), editorMap.getTiles()[i][p]
												.getGridPosY(), Seed.NORTHSOUTH));
							} else if (interSelected) {
								System.out.println("Adding intersection at "
										+ editorMap.getTiles()[i][p].toString());								
								editorMap.addGroup(new Intersection(editorMap
										.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p]
												.getGridPosY()));
							} else if (roadVerticalSelected) {
								System.out.println("Removing road");
								editorMap.removeGroup(new Road(editorMap.getTiles()[i][p]
										.getGridPosX(), editorMap.getTiles()[i][p]
												.getGridPosY(), Seed.NORTHSOUTH));


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
		});
		

		/*
		 * Drag to draw for Land and road tiles
		 */
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
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
							} else if (dirtSelected) {
								System.out.println("Adding dirt at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.getTiles()[i][p].setOccupied(true);
							} else if (blockSelected) {
								System.out.println("Adding block at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.getTiles()[i][p].setOccupied(true);
							}else if (eraserSelected) {
								// TODO implement remove properly
								System.out.println("Removing at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.removeGroup(new Road(editorMap.getTiles()[i][p]
										.getGridPosX(), editorMap.getTiles()[i][p]
												.getGridPosY(), Seed.NORTHSOUTH));
							}else if (roadVerticalSelected) {
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
		 * Mouse hover for each tile in the EditorMap
		 */
		Tile[][] mapTiles = editorMap.getTiles();
		for (int x =0 ;  x < mapTiles.length; x++) {
			for(int y = 0; y < mapTiles[x].length; y++) {
				Tile t = editorMap.getTiles()[x][y];

				t.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						System.out.println("Mouse hovering over X:" + t.gridPosX + " Y:" + t.gridPosY);
					}
				});
				
			}

		}


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
			/*canvas.setMinSize(canvasWidth, canvasHeight);
			canvas.setPrefSize(canvasWidth, canvasHeight);
			canvas.setMaxSize(canvasWidth, canvasHeight);*/
			rootLayout.setCenter(canvas);

			//not sure if needed?
			//setGridSize(30);

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
			landSelected = true;
			dirtSelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrLand));
			break;
		case "dirtButton":
			landSelected = false;
			dirtSelected = true;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrDirt));
			break;
		case "eraserButton":
			landSelected = false;
			dirtSelected = false;
			eraserSelected = true;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrErase));
			break;
		case "roadVerticalButton":
			landSelected = false;
			dirtSelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = true;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrRoadNS));
			break;
		case "roadHorizontalButton":
			landSelected = false;
			dirtSelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = true;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrRoadEW));
			break;
		case "interButton":
			landSelected = false;
			dirtSelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = true;
			canvas.setCursor(new ImageCursor(csrIntersection));
			break;
		case "blockButton":
			landSelected = false;
			dirtSelected = false;
			eraserSelected = false;
			blockSelected = true;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrBlock));
			break;
		case "openMapButton":
			fileChooser = new FileChooser();
			fileChooser.setTitle("Open Map XML...");
			extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
			fileChooser.getExtensionFilters().add(extFilter);
			selectedFile = fileChooser.showOpenDialog(editorStage);
			if (selectedFile != null) {
				loadMap(selectedFile.getPath());
			}
			break;
		case "saveMapButton":
			fileChooser = new FileChooser();
			fileChooser.setTitle("Save Map XML...");
			extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
			fileChooser.getExtensionFilters().add(extFilter);
			selectedFile = fileChooser.showSaveDialog(editorStage);
			if (selectedFile != null) {
				saveMap(selectedFile.getPath());
			}

			// TODO: Delete test block
//			System.out.println("Tile 0,0 is occupied: " + editorMap.getTiles()[0][0].isOccupied());
//			System.out.println(getTileDetails(0,0));
//			System.out.println(getTileDetails(1,0));
//			System.out.println(getTileDetails(2,0));
//			System.out.println(getTileDetails(3,0));
			
			break;
		case "clearMapButton":
			System.out.println("Clicked Clear Map Button");
			this.editorMap = new Map();
			break;
		default:
			break;
		}
	}
	
	
	/*
	 *  TODO: replace return type with something usable by mouse hover and/or XML
	 *  		Perhaps the method should take in a Tile rather than compute the information based on XY 
	 */
	/**
	 * Returns details about the given tile 
	 * 
	 * @param posX X position of tile in Map array
	 * @param posY Y position of tile in Map array
	 * @return details - String listing tile type and attribute
	 */
	public String getTileDetails(int posX, int posY) {
		String attribute = "";
		Tile t = editorMap.getTiles()[posX][posY];
		
		if (t instanceof Lane) {
			attribute = "This tile "+ posX + " " + posY +" is a lane with " +((Lane) t).getDirection() + " Direction";
		} else if (t instanceof Land) {
			attribute = "This tile "+ posX + " " + posY +" is land of type " +((Land) t).getLandType();
		}
		return attribute;
	}


	public void loadMap(String fileLocation){
		MapXML mxml = new MapXML();
		mxml.readXML(fileLocation);
		System.out.println(mxml.toString());
	}

	public void saveMap(String fileLocation){
		MapXML mxml = new MapXML();
		mxml.writeXML(editorMap.tiles, fileLocation, "name", "03-03-2015", "test map by me", "paul", 800, 40);
	}


	public static void main(String[] args) {
		launch(args);
	}
}
