package com.simulus.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;

public class VIntersection extends Group{
	Road frame;
	Line l;
	Path path1, path2, path3, path4;
	boolean isSwitched = false;
	private int id;

	public VIntersection(int posX, int posY, int width, int height, int id){
		this.id = id;
		frame = new Road(posX, posY, width, height);
		path1 = PathBuilder.create()
                .elements( 
                    new MoveTo(posX,posY),
                    new LineTo(posX+width, posY)
                )
                .build();
        path1.setStroke(Color.RED);
        path1.getStrokeDashArray().setAll(5d,5d);
        
        path2 = PathBuilder.create()
                .elements( 
                    new MoveTo(posX,posY+height),
                    new LineTo(posX+width, posY+height)
                )
                .build();
        path2.setStroke(Color.RED);
        path2.getStrokeDashArray().setAll(5d,5d);
        
        path3 = PathBuilder.create()
                .elements( 
                    new MoveTo(posX+width,posY),
                    new LineTo(posX+width, posY+height)
                )
                .build();
        path3.setStroke(Color.RED);
        path3.getStrokeDashArray().setAll(5d,5d);
        
        path4 = PathBuilder.create()
                .elements( 
                		new MoveTo(posX,posY),
                        new LineTo(posX, posY+height)
                )
                .build();
        path4.setStroke(Color.RED);
        path4.getStrokeDashArray().setAll(5d,5d);

		this.getChildren().addAll(frame, path1, path2);
		switchLights();
		
	}
	
	public void switchLights(){
		this.getChildren().clear();
		if(!isSwitched){
			this.getChildren().addAll(frame, path3, path4);
			isSwitched = true;
		}else{
			this.getChildren().addAll(frame, path1, path2);
			isSwitched = false;
		}
	}
	public int getIntersectionId(){
		return id;
	}
}
