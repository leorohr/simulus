package com.simulus;
/**
 * @author Ebrahim K1463831
 */
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
 
/**
 * A class which moves a node using a translation
 * transition playing multiple times within an animation timer.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/FillTransition
 * @related animation/transitions/ParallelTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/PauseTransition
 * @related animation/transitions/RotateTransition
 * @related animation/transitions/ScaleTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @see javafx.animation.TranslateTransition
 * @see javafx.animation.TranslateTransitionBuilder
 * @see javafx.animation.Transition
 */
public class FXMovingCar extends Application {
 
	//Initialise translation
    private TranslateTransition translateTransition;
    
    //The node to translate
    Rectangle car;
    
    //Keeps track of the frames
    private int frameNo;
    
    //The position of the node on X
    private int posX;
    
    //The position of the node on Y
    private int posY;
    
    //Initialises the stage
    private void init(Stage primaryStage) {
        Group root = new Group();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        //Position of window 
        primaryStage.setX(0);
        primaryStage.setY(0);
        //Height and width of window
        primaryStage.setWidth(bounds.getWidth() / 2);
        primaryStage.setHeight(bounds.getHeight() / 2);
        //Initialising window
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 400,40));
        System.out.println();
        //Let position X be roughly in the center (left side of the "road")
        posX= (int) ((primaryStage.getWidth()/2)-50);
        
        //Let position Y start slightly off screen
        posY=(int) ((primaryStage.getHeight()/2)+25);
        
        //Initialise node
        car = new Rectangle(posX, posY, 40, 40);
        car.setArcHeight(20);
        car.setArcWidth(50);
        car.setFill(Color.GRAY);
        
        //Add node to the scene
        root.getChildren().add(car);
        
        //Create a new translation. Values can't be changed between translations
        translateTransition = new TranslateTransition(Duration.millis(1),car);
        translateTransition.setFromY(posY);
        translateTransition.setToY(posY - 1);
        translateTransition.setCycleCount(1);
        
        //Determines how the node accelerates/decelerates
        translateTransition.setInterpolator(Interpolator.LINEAR);
    
        translateTransition.setNode(car);
        
        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//After the translation, set the new node position
            	posY = posY - 1;
            	//Reset the translation area for the next animation using the new Y position
            	translateTransition.setFromY(posY);
                translateTransition.setToY(posY-1);
            }
        });
    }
 
    public void play() {
        translateTransition.play();
    }
 
    @Override public void stop() {
        translateTransition.stop();
    }

    @Override public void start(Stage primaryStage) throws Exception {
    	//Stage initialisation
        init(primaryStage);
        primaryStage.show();
        //Animation loop
        AnimationTimer timer = new AnimationTimer() {
        	//When the timer is started, this method loops endlessly
            @Override
            public void handle(long now) {
            	//Plays the transition from start to finish (the loop doesn't replay the animation till its over)
	            play();
	            //Increase frame number
                frameNo++;
                System.out.println("Frame: "+ frameNo);
            }
            
        };
        //Starts the animation loop
        timer.start();	
    }
    public static void main(String[] args) { launch(args); }
}