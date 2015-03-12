package com.simulus;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.simulus.util.Configuration;
import com.simulus.util.ResourceBuilder;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Orientation;
import com.simulus.view.Tile;
import com.simulus.view.intersection.Intersection;
import com.simulus.view.intersection.IntersectionTile;
import com.simulus.view.map.Block;
import com.simulus.view.map.Water;
import com.simulus.view.map.Dirt;
import com.simulus.view.map.Grass;
import com.simulus.view.map.Land;
import com.simulus.view.map.Lane;
import com.simulus.view.map.Map;
import com.simulus.view.map.Road;

public class EditorApp extends Application {

	private Stage editorStage;
	private BorderPane rootLayout;
	private Pane canvas;
	private Map editorMap;
	private int gridSize = Configuration.gridSize;
	private int tileSize = Configuration.tileSize;
	private boolean mapValidated;
	private Scene scene;

	private Image csrRoadEW = ResourceBuilder.getEWLaneCursor();
	private Image csrRoadNS = ResourceBuilder.getNSLaneCursor();
	private Image csrGrass = ResourceBuilder.getGrassCursor();
	private Image csrDirt = ResourceBuilder.getDirtCursor();
	private Image csrWater = ResourceBuilder.getWaterCursor();
	private Image csrBlock = ResourceBuilder.getBlockCursor();
	private Image csrIntersection = ResourceBuilder.getBoxjunctionCursor();
	private Image csrErase = ResourceBuilder.getEraseCursor();

	private boolean grassSelected = false;
	private boolean dirtSelected = false;
	private boolean waterSelected = false;
	private boolean blockSelected = false;
	private boolean eraserSelected = false;
	private boolean roadVerticalSelected = false;
	private boolean roadHorizontalSelected = false;
	private boolean interSelected = false;


	private Tile hoverTile;
	int xFixed;
	int yFixed;
	boolean firstDrag = true;

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

							Tile t = editorMap.getTiles()[i][p];

							if (t instanceof Lane || t instanceof IntersectionTile){

							} else{
								if (grassSelected) {
									if (event.isShiftDown()){
										floodFill("grass", i, p);
									}else{
										editorMap.addSingle(new 
												Grass(i*tileSize, p*tileSize, tileSize, tileSize, i, p));
									}
								} else if (dirtSelected) {
									if (event.isShiftDown()){
										floodFill("dirt", i, p);
									}else{
										editorMap.addSingle(new 
												Dirt(i*tileSize, p*tileSize, tileSize, tileSize, i, p));
									}
								} else if (waterSelected) {
									if (event.isShiftDown()){
										floodFill("water", i, p);
									}else{
										editorMap.addSingle(new Water(i*tileSize, p*tileSize, tileSize, tileSize, i, p));
									}
								}else if (interSelected) {
									editorMap.addGroup(new Intersection(editorMap.getTiles()[i][p].getGridPosX(),
											editorMap.getTiles()[i][p].getGridPosY()));
								} else if (roadVerticalSelected) {
									editorMap.removeGroup(new Road(editorMap.getTiles()[i][p].getGridPosX(), 
											editorMap.getTiles()[i][p].getGridPosY(), Orientation.NORTHSOUTH));
									editorMap.addGroup(new Road(editorMap.getTiles()[i][p].getGridPosX(),
											editorMap.getTiles()[i][p].getGridPosY(), Orientation.NORTHSOUTH));
								} else if (roadHorizontalSelected) {
									editorMap.addGroup(new Road(editorMap.getTiles()[i][p].getGridPosX(),
											editorMap.getTiles()[i][p].getGridPosY(), Orientation.WESTEAST));
								}
							}

							if (eraserSelected) {
								if(t instanceof Land && !(t instanceof Block) ) {
									if (event.isShiftDown()){
										floodFillRemove(i,p);
									}else{
									editorMap.removeSingle(editorMap.getTiles()[i][p]);	
									}
								} else if (t instanceof Lane || t instanceof IntersectionTile || t instanceof Block){
									groupErase(t);
								}
							}else if (blockSelected && t instanceof Lane) {
								editorMap.addSingle(new Lane(i*tileSize, p*tileSize, tileSize, tileSize, i, p, ((Lane) t).getDirection(), ((Lane) t).getLaneNo(), true));
							}
						}
					}
			}
		});


		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				firstDrag = true;
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

							Tile t = editorMap.getTiles()[i][p];

							if (t instanceof Lane || t instanceof IntersectionTile){

							} else{
								if (grassSelected) {
									editorMap.addSingle(new Grass(i*tileSize, p*tileSize, tileSize, tileSize, i, p));
								} else if (dirtSelected) {
									editorMap.addSingle(new Dirt(i*tileSize, p*tileSize, tileSize, tileSize, i, p));
								} else if (waterSelected) {
									editorMap.addSingle(new Water(i*tileSize, p*tileSize, tileSize, tileSize, i, p));
								}else if (roadVerticalSelected) {
									if(firstDrag == true){
										xFixed = i;
										firstDrag = false;}
									else{

									}
									editorMap.addGroup(new Road(editorMap.getTiles()[xFixed][p].getGridPosX(),
											editorMap.getTiles()[xFixed][p].getGridPosY(), Orientation.NORTHSOUTH));
								} else if (roadHorizontalSelected) {
									if(firstDrag == true){
										yFixed = p;
										firstDrag = false;}
									else{

									}
									editorMap.addGroup(new Road(editorMap.getTiles()[i][yFixed].getGridPosX(),
											editorMap.getTiles()[i][yFixed].getGridPosY(), Orientation.WESTEAST));
								}
							}

							if (eraserSelected) {
								if(t instanceof Land && !(t instanceof Block) ) {
									editorMap.removeSingle(editorMap.getTiles()[i][p]);									
								} else if (t instanceof Lane || t instanceof IntersectionTile || t instanceof Block){
									groupErase(t);									
								}
							} else if (blockSelected && t instanceof Lane) {
								// TODO: Blockage
								editorMap.removeSingle(t);
								editorMap.addSingle(new Lane(i*tileSize, p*tileSize, tileSize, tileSize, i, p, ((Lane) t).getDirection(), ((Lane) t).getLaneNo(), true));
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
						//System.out.println(t.toString());
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

			public void handle(long now) { // Increment the frame number
				getCanvas().getChildren().clear();
				editorMap.drawMap(canvas);
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
					.getResource("view/ui/EditorRootLayout.fxml"));
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
					.getResource("view/ui/EditorControls.fxml"));
			AnchorPane controls = (AnchorPane) loader.load();
			ECC = (EditorControlsController) loader.getController();
			rootLayout.setRight(controls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getGridSize() {
		return gridSize;
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
			waterSelected = false;
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
			waterSelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrDirt));
			break;
		case "waterButton":
			grassSelected = false;
			dirtSelected = false;
			waterSelected = true;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = false;
			canvas.setCursor(new ImageCursor(csrWater));
			break;
		case "blockButton":
			grassSelected = false;
			dirtSelected = false;
			waterSelected = false;
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
			waterSelected = false;
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
			waterSelected = false;
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
			waterSelected = false;
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
			waterSelected = false;
			eraserSelected = false;
			blockSelected = false;
			roadVerticalSelected = false;
			roadHorizontalSelected = false;
			interSelected = true;
			canvas.setCursor(new ImageCursor(csrIntersection));
			break;
		case "openMapButton":
			openMapDialog();
			break;
		case "saveMapButton":
			saveMapDialog();
			break;
		case "clearMapButton":
			this.editorMap = new Map();
			break;
		default:
			break;
		}
	}

	public File saveMapDialog(){
		fileChooser = new FileChooser();
		fileChooser.setTitle("Save Map XML...");
		extFilter = new FileChooser.ExtensionFilter("Simulus Map Files (*.map)", "*.map");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialFileName(ECC.getMapName());
		selectedFile = fileChooser.showSaveDialog(editorStage);
		if (selectedFile != null) {
			saveMap(selectedFile.getPath());
		}
	
		return selectedFile;
	}

	public void openMapDialog(){
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open Map XML...");
		extFilter = new FileChooser.ExtensionFilter("Simulus Map Files (*.map)", "*.map");
		fileChooser.getExtensionFilters().add(extFilter);
		selectedFile = fileChooser.showOpenDialog(editorStage);
		if (selectedFile != null) {
			loadMap(selectedFile.getPath());
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

	/**
	 * Checks the group membership of a tile,
	 *  if tile is a Lane or Block then <code>removeGroup()</code> is called on LaneNo 0
	 *  if the tile is an IntersectionTile then removeGroup() is called on the Intersection 
	 * 
	 * 
	 * @param t - The tile whose group is to be erased
	 */
	private void groupErase(Tile t) {
		if (t instanceof Lane || t instanceof Block) {
			Direction dir = Direction.NONE;
			int laneNo = 0;
			if (t instanceof Lane) {
				dir = ((Lane) t).getDirection();
				laneNo = ((Lane) t).getLaneNo();
			} else if (t instanceof Block) {
				dir = ((Block) t).getDirection();
				laneNo = ((Block) t).getLaneNo();
			}
			int x = t.getGridPosX();
			int y = t.getGridPosY();
			Orientation orientation = Orientation.EMPTY;

			if (dir == Direction.NORTH || dir == Direction.SOUTH) {
				while (laneNo > 0) {
					x--;
					laneNo--;
				}
				orientation = Orientation.NORTHSOUTH;
			} else if (dir == Direction.WEST || dir == Direction.EAST) {
				while (laneNo > 0) {
					y--;
					laneNo--;
				}
				orientation = Orientation.WESTEAST;
			}
			editorMap.removeGroup(new Road(x, y, orientation));

		} else if (t instanceof IntersectionTile) {
			editorMap.removeGroup(((IntersectionTile) t).getIntersection());
		}
	}

	public void loadMap(String fileLocation){

		MapXML mxml = new MapXML();
		mxml.readXML(fileLocation);
		ECC.setMapName(mxml.mapName);
		ECC.setMapDate(mxml.mapCreationDate);
		ECC.setMapDesc(mxml.mapDescription);
		ECC.setMapAuthor(mxml.mapAuthor);
		//ECC.setGridSize(60);
		//editorMap.setTiles(mxml.getTileGrid());
		//editorMap.drawMap(canvas);
		editorMap.loadEditorMap(new File(fileLocation));
	}

	public void saveMap(String fileLocation){
		MapXML mxml = new MapXML();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		mxml.writeXML(editorMap.getTiles(), fileLocation, ECC.getMapName(), dateFormat.format(date),
				ECC.getMapDesc(), ECC.getMapAuthor(), 800, ECC.getGridSize(), mapValidated);

	}

	//TODO implment some validation checks based on tiles
	private void validateMap(){

		Tile[][] mapTiles = this.editorMap.getTiles();
		for (int x = 0 ;  x < mapTiles.length; x++) {
			for(int y = 0; y < mapTiles[x].length; y++) {

				Tile t = this.editorMap.getTiles()[x][y];

			}
		}
	}

	private void floodFill(String tFill, int xIn, int yIn){
		Tile[][] mapTiles = this.editorMap.getTiles();
		
		if (xIn >=0 && xIn < mapTiles.length && yIn >= 0 && yIn < mapTiles.length){
			
			Tile t = this.editorMap.getTiles()[xIn][yIn];
			
			if (t instanceof Lane || t instanceof Land  || t instanceof IntersectionTile){
				return;
			} else {
				switch(tFill){
				case "grass":
					editorMap.addSingle(new Grass(xIn*tileSize, yIn*tileSize, tileSize, tileSize, xIn, yIn));
					break;
				case "dirt":
					editorMap.addSingle(new Dirt(xIn*tileSize, yIn*tileSize, tileSize, tileSize, xIn, yIn));
					break;
				case "water":
					editorMap.addSingle(new Water(xIn*tileSize, yIn*tileSize, tileSize, tileSize, xIn, yIn));
					break;
				}
				floodFill(tFill, xIn + 1, yIn);
				floodFill(tFill, xIn - 1, yIn);
				floodFill(tFill, xIn, yIn + 1);
				floodFill(tFill, xIn, yIn - 1);
			}
		}

	}
	
	private void floodFillRemove(int xIn, int yIn){
		Tile[][] mapTiles = this.editorMap.getTiles();
		
		if (xIn >=0 && xIn < mapTiles.length && yIn >= 0 && yIn < mapTiles.length){
			
			Tile t = this.editorMap.getTiles()[xIn][yIn];
			
			if (t instanceof Lane || t instanceof IntersectionTile){
				return;
			} else if (t instanceof Land){
				editorMap.removeSingle(editorMap.getTiles()[xIn][yIn]);	
				floodFillRemove(xIn + 1, yIn);
				floodFillRemove(xIn - 1, yIn);
				floodFillRemove(xIn, yIn + 1);
				floodFillRemove(xIn, yIn - 1);
			} else{}
		}

	}
	

	public Stage getPrimaryStage() {
		return editorStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}


}