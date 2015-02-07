package com.simulus;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.controller.SimulationController;
import com.simulus.model.Intersection;
import com.simulus.model.Map;
import com.simulus.model.Vehicle;
import com.simulus.view.Road;
import com.simulus.view.TrafficLight;
import com.simulus.view.VCar;
import com.simulus.view.VVehicle;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private int frameNo = 0;
	private ArrayList<VVehicle> cars;
	private VCar car = null;
	private AnchorPane overview;
	private TrafficLight lights;
	private Rectangle rect;
	private Group tile;
	private int gridSize;
	private int windowWidth = 900;
	private int windowHeight = 900;
	private static MainApp instance;
	private SimulationController controller;
	private int tileWidth;
	
	private ArrayList<Road> roads = new ArrayList<>();
	private Rectangle[][] tiles = new Rectangle[30][30];
	
	public static MainApp getInstance(){
		return instance;
	}
	
	public MainApp(){
		super();
		
		//Synchronized call to access the application's instance
		synchronized(MainApp.class){
			if(instance != null) throw new UnsupportedOperationException(
					getClass()+" ");
			instance = this;
		}
	}
	@Override
	public void start(final Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Simulus");
		lights = new TrafficLight(100, 100, 20, 60);		
		cars = new ArrayList<VVehicle>();
		
		
		initRootLayout();
		showMainView();
		
		//Get SimulationController; initialises controller on first call.
		controller = SimulationController.getInstance();
		
		/**
		 * Ticking loop
		 */
/*		AnimationTimer timer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			@Override
			public void handle(long now) {
				//Increment the frame number
				frameNo++;
				//Spawn a car at frame 1
				System.out.println(frameNo);
				if (frameNo == 1) {
					cars.add(new VCar(500, 900, Direction.NORTH));
					cars.add(new VCar(500, 800+(90/3), Direction.NORTH));
					cars.add(new VCar(500, 800+(90/3)+(90/3), Direction.NORTH));
					System.out.println("Start X Position: "+cars.get(cars.size()-1).getX()+"\nStart Y Position: "+ cars.get(cars.size()-1).getY());
					for(int i = 0; i < cars.size(); i++)
						rootLayout.getChildren().add(cars.get(i));
				}
				if(frameNo%8 == 0){
					for(int i = 0; i < cars.size(); i++);
						//cars.get(i).moveVehicle();
				}
			}
		};
		timer.start();*/
	}

	/**
	 * Initialise the root layout
	 */
	private void initRootLayout() {

		try {

			/*FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			*/
			rootLayout = new BorderPane();
			//rootLayout.setBorder(new Border());
			Scene scene = new Scene(rootLayout, windowWidth, windowHeight);

	        rootLayout.prefHeightProperty().bind(scene.heightProperty());
	        rootLayout.prefWidthProperty().bind(scene.widthProperty());
			Screen screen = Screen.getPrimary();
	        Rectangle2D bounds = screen.getVisualBounds();
			primaryStage.setScene(scene);
			//primaryStage.setWidth(bounds.getHeight()/2);
			//primaryStage.setHeight(bounds.getWidth()/2);
			
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent t) {
	                Platform.exit();
	                System.exit(0);
	            }
	        });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void setGridSize(int size){
		gridSize = size;
		tileWidth = windowWidth/gridSize;
	}

	/**
	 * TODO
	 */
	private void showMainView() {
		try {
			//FXMLLoader loader = new FXMLLoader();
			//loader.setLocation(MainApp.class.getResource("view/MainView.fxml"));
			//overview = (AnchorPane) loader.load();
			// Set overview as center widget of the pane
			//rootLayout.setCenter(overview);
			//rootLayout.getChildren().add(lights);
			//rootLayout.getChildren().add(tile);
			int count = 90/3;
			
			for(int i = 0; i < 30; i ++){
				for(int p = 0; p <30; p++){
					
					tiles[i][p] = new Rectangle(count*p, count*i, count, count);
					tiles[i][p].setFill(Color.TRANSPARENT);
					tiles[i][p].setStroke(Color.BLACK);
					rootLayout.getChildren().add(tiles[i][p]);
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void redrawCars(List<Vehicle> vehicles){
		
		//Clear Screen
		rootLayout.getChildren().clear();
		
		//Draw Roads
		for(int i = 0; i < roads.size(); i++)
			rootLayout.getChildren().add(roads.get(i));
		
		//Draw cars
		for(Vehicle v : vehicles) {
			VCar car = new VCar(v.getX()*tileWidth, v.getY()*tileWidth,v.getDirection());
			switch(car.getDirection()){
			case NORTH:
				car.setY((v.getY()*tileWidth)+(tileWidth/2)-(car.getHeight()/2));
				if(v.getLaneID() == 1)
					car.setX((v.getX()*tileWidth)+(tileWidth/2)-(tileWidth/8)-(car.getWidth()/2));
				if(v.getLaneID() == 0)
					car.setX((v.getX()*tileWidth)+(tileWidth/8)-(car.getWidth()/2));
				break;
				
			case WEST:
				car.setX((v.getX()*tileWidth)+(tileWidth/2)-(car.getWidth()/2));
				if(v.getLaneID() == 1)
					car.setY((v.getY()*tileWidth)+tileWidth-(tileWidth/8)-(car.getHeight()/2));
				if(v.getLaneID() == 0)
					car.setY((v.getY()*tileWidth)+((tileWidth*3)/4)-(tileWidth/8)-(car.getHeight()/2));
				break;
				
			case SOUTH:
				car.setY((v.getY()*tileWidth)+(tileWidth/2)-(car.getHeight()/2));
				if(v.getLaneID() == 1)
					car.setX((v.getX()*tileWidth)+tileWidth-(tileWidth/8)-(car.getWidth()/2));
				if(v.getLaneID() == 0)
					car.setX((v.getX()*tileWidth)+((tileWidth*3)/4)-(tileWidth/8)-(car.getWidth()/2));
				break;
				
			case EAST:
				car.setX((v.getX()*tileWidth)+(tileWidth/2)-(car.getWidth()/2));
				if(v.getLaneID() == 1)
					car.setY((v.getY()*tileWidth)+(tileWidth/2)-(tileWidth/8)-(car.getHeight()/2));
				if(v.getLaneID() == 0)
					car.setY((v.getY()*tileWidth)+(tileWidth/8)-(car.getHeight()/2));
				break;
				
			default:
				break;
			}
			
			cars.add(car);
			rootLayout.getChildren().add(car);
		}
	}
	
	public void switchLights() {
		
	}
	
	public void readMap() {
		Map map = Map.getInstance();
		for(int i=0; i<gridSize; i++) {
			for(int j=0; j<gridSize; j++) {
				com.simulus.model.Road r = map.getTile(i, j).content;
				if(r == null)
					continue;
				if(r instanceof Intersection) {
					roads.add(new Road(i*tileWidth, j*tileWidth, tileWidth, tileWidth));
				} else if(r instanceof com.simulus.model.Road) {
					roads.add(new Road(i*tileWidth, j*tileWidth, tileWidth, tileWidth, r.getSeed()));
				}
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
