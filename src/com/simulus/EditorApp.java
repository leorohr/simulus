package com.simulus;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.controller.EditorControlsController;
import com.simulus.io.MapXML;
import com.simulus.util.enums.Orientation;
import com.simulus.view.Block;
import com.simulus.view.City;
import com.simulus.view.Dirt;
import com.simulus.view.Grass;
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
	private boolean mapValidated;
	//fixed size canvas
	private int canvasWidth = 800;
	private int canvasHeight = 800;
	private int tileWidth;
	private Scene scene;

	private Image csrRoadEW = new Image("com/simulus/util/images/csr_eastwest.png");
	private Image csrRoadNS = new Image("com/simulus/util/images/csr_northsouth.png");
	private Image csrGrass = new Image("com/simulus/util/images/csr_grass.png");
	private Image csrDirt = new Image("com/simulus/util/images/csr_dirt.png");
	private Image csrCity = new Image("com/simulus/util/images/csr_city.png");
	private Image csrBlock = new Image("com/simulus/util/images/csr_block.png");
	private Image csrIntersection = new Image("com/simulus/util/images/csr_boxjunction.png");
	private Image csrErase = new Image("com/simulus/util/images/csr_erase.png");

	private boolean grassSelected = false;
	private boolean dirtSelected = false;
	private boolean citySelected = false;
	private boolean blockSelected = false;
	private boolean eraserSelected = false;
	private boolean roadVerticalSelected = false;
	private boolean roadHorizontalSelected = false;
	private boolean interSelected = false;
	
	
	private Tile hoverTile;

	FileChooser fileChooser = new FileChooser();
	FileChooser.ExtensionFilter extFilter;
	File selectedFile;
	File userDirectory = new File(System.getProperty("user.home") + "/Desktop");


	private static EditorApp instance;
	EditorControlsController ECC;

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
							if (grassSelected) {
								editorMap.addSingle(new 
										Grass(i*Map.TILESIZE, p*Map.TILESIZE, Map.TILESIZE, Map.TILESIZE, i, p));
							} else if (dirtSelected) {
								editorMap.addSingle(new 
										Dirt(i*Map.TILESIZE, p*Map.TILESIZE, Map.TILESIZE, Map.TILESIZE, i, p));
							} else if (citySelected) {
								editorMap.addSingle(new 
										City(i*Map.TILESIZE, p*Map.TILESIZE, Map.TILESIZE, Map.TILESIZE, i, p));
							}else if (blockSelected) {
								// TODO: Blockage
								editorMap.addSingle(new Block(i*Map.TILESIZE, p*Map.TILESIZE, Map.TILESIZE, Map.TILESIZE, i, p));
								//editorMap.getTiles()[i][p].setOccupied(true);
							} else if (eraserSelected) {
								// TODO implement remove properly
								// Single Tile
								editorMap.removeSingle(editorMap.getTiles()[i][p]);
								// Group of tiles
//								editorMap.removeGroup(new Road(editorMap.getTiles()[i][p]
//										.getGridPosX(), editorMap.getTiles()[i][p].getGridPosY(), Seed.NORTHSOUTH));
							} else if (interSelected) {
								// TODO : Remove group before add new
								editorMap.addGroup(new Intersection(editorMap.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p].getGridPosY()));
								
							} else if (roadVerticalSelected) {
								// Remove current tiles in space
								editorMap.removeGroup(new 
										Road(editorMap.getTiles()[i][p].getGridPosX(), 
												editorMap.getTiles()[i][p].getGridPosY(), Orientation.NORTHSOUTH));
								// Add new Verticle Road
								editorMap.addGroup(new 
										Road(editorMap.getTiles()[i][p].getGridPosX(),
												editorMap.getTiles()[i][p].getGridPosY(), Orientation.NORTHSOUTH));
							} else if (roadHorizontalSelected) {
								editorMap.addGroup(new Road(editorMap.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p].getGridPosY(), Orientation.WESTEAST));
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

							if (grassSelected) {
								System.out.println("Adding grass at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addSingle(new Grass(i*Map.TILESIZE, p*Map.TILESIZE, Map.TILESIZE, Map.TILESIZE, i, p));
							} else if (dirtSelected) {
								System.out.println("Adding dirt at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addSingle(new Dirt(i*Map.TILESIZE, p*Map.TILESIZE, Map.TILESIZE, Map.TILESIZE, i, p));
							} else if (citySelected) {
								System.out.println("Adding city at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addSingle(new City(i*Map.TILESIZE, p*Map.TILESIZE, Map.TILESIZE, Map.TILESIZE, i, p));
							}else if (blockSelected) {
								System.out.println("Adding block at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addSingle(new Block(i*Map.TILESIZE, p*Map.TILESIZE, Map.TILESIZE, Map.TILESIZE, i, p));
							}else if (eraserSelected) {
								// TODO implement remove properly
								System.out.println("Removing at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.removeSingle(editorMap.getTiles()[i][p]);
//								editorMap.removeGroup(new Road(editorMap.getTiles()[i][p]
//										.getGridPosX(), editorMap.getTiles()[i][p]
//												.getGridPosY(), Seed.NORTHSOUTH));
							}else if (roadVerticalSelected) {
								System.out.println("Adding road at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addGroup(new Road(editorMap
										.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p]
												.getGridPosY(), Orientation.NORTHSOUTH));
							} else if (roadHorizontalSelected) {
								System.out.println("Adding road at "
										+ editorMap.getTiles()[i][p].toString());
								editorMap.addGroup(new Road(editorMap
										.getTiles()[i][p].getGridPosX(),
										editorMap.getTiles()[i][p]
												.getGridPosY(), Orientation.WESTEAST));
							}
						}
					}
				}
			}

		});
		
	
		
		
		/**
		 * Mouse hover for each tile in the EditorMap
		 */
		Tile[][] mapTiles = this.editorMap.getTiles();
		for (int x = 0 ;  x < mapTiles.length; x++) {
			for(int y = 0; y < mapTiles[x].length; y++) {

				Tile t = this.editorMap.getTiles()[x][y];
				
				
				// Get the currently hovered tile
				t.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						hoverTile = t;
						System.out.println(t.toString());
						t.setMouseTransparent(true);
						ghostDraw(t);
						
					}
				});
				
				// TODO : resolve insta-remove issue
				// Forget the tile when no longer hovered
				t.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						//hoverTile.setMouseTransparent(false);
						ghostRemove(hoverTile);
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
			ECC = (EditorControlsController) loader.getController();
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
		case "grassButton":
			grassSelected = true;
			dirtSelected = false;
			citySelected = false;
			blockSelected = false;
			eraserSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrGrass));
			break;
		case "dirtButton":
			grassSelected = false;
			dirtSelected = true;
			citySelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrDirt));
			break;
		case "cityButton":
			grassSelected = false;
			dirtSelected = false;
			citySelected = true;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrCity));
			break;
		case "blockButton":
			grassSelected = false;
			dirtSelected = false;
			citySelected = false;
			eraserSelected = false;
			blockSelected = true;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrBlock));
			break;
		case "eraserButton":
			grassSelected = false;
			dirtSelected = false;
			citySelected = false;
			eraserSelected = true;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrErase));
			break;
		case "roadVerticalButton":
			grassSelected = false;
			dirtSelected = false;
			citySelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = true;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrRoadNS));
			break;
		case "roadHorizontalButton":
			grassSelected = false;
			dirtSelected = false;
			citySelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = true;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrRoadEW));
			break;
		case "interButton":
			grassSelected = false;
			dirtSelected = false;
			citySelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = true;
			canvas.setCursor(new ImageCursor(csrIntersection));
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
			System.out.println(getTileDetails(0,0));
			System.out.println(getTileDetails(1,0));
			System.out.println(getTileDetails(2,0));
			System.out.println(getTileDetails(3,0));
			
			break;
		case "clearMapButton":
			System.out.println("Clicked Clear Map Button");
			this.editorMap = new Map();
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * hover function
	 */
	private void ghostDraw(Tile tile) {
		if (grassSelected) {
			tile.getFrame().setFill(Color.GREEN);
		} else if (dirtSelected){
			tile.getFrame().setFill(Color.YELLOW);
		}
	}
	
	private void ghostRemove(Tile tile) {
		// TODO 
		tile.getFrame().setFill(Color.TRANSPARENT);
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
		ECC.setMapName(mxml.mapName);
		ECC.setMapDate(mxml.mapCreationDate);
		ECC.setMapDesc(mxml.mapDescription);
		ECC.setMapAuthor(mxml.mapAuthor);
		//ECC.setGridSize(60);
		editorMap.setTiles(mxml.getTileGrid());
		editorMap.drawMap();
	}

	public void saveMap(String fileLocation){
		MapXML mxml = new MapXML();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		mxml.writeXML(editorMap.getTiles(), fileLocation, ECC.getMapName(), dateFormat.format(date),
				ECC.getMapDesc(), ECC.getMapAuthor(), 800, ECC.getGridSize(), mapValidated);
		
	}


	public static void main(String[] args) {
		launch(args);
	}
}
