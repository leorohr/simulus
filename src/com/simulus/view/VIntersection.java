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

	public VIntersection(int posX, int posY, int width, int height){
		frame = new Road(posX, posY, width, height);
		path1 = PathBuilder.create()
                .elements( 
                    new MoveTo(posX,posY+height/4),
                    new LineTo(posX+width, posY+height/4)
                )
                .build();
        path1.setStroke(Color.RED);
        path1.getStrokeDashArray().setAll(5d,5d);
        
        path2 = PathBuilder.create()
                .elements( 
                    new MoveTo(posX,posY+(height*3)/4),
                    new LineTo(posX+width, posY+(height)*3/4)
                )
                .build();
        path2.setStroke(Color.RED);
        path2.getStrokeDashArray().setAll(5d,5d);
        
        path3 = PathBuilder.create()
                .elements( 
                    new MoveTo(posX+(width/4),posY),
                    new LineTo(posX+(width/4), posY+height)
                )
                .build();
        path3.setStroke(Color.RED);
        path3.getStrokeDashArray().setAll(5d,5d);
        
        path4 = PathBuilder.create()
                .elements( 
                		new MoveTo(posX+((width*3)/4),posY),
                        new LineTo(posX+((width*3)/4), posY+height)
                )
                .build();
        path4.setStroke(Color.RED);
        path4.getStrokeDashArray().setAll(5d,5d);

		this.getChildren().addAll(frame, path1, path2);
		Thread t = new Thread(){
			public void run(){
				try{
					sleep(2000);
					switchLights();
					sleep(2000);
					switchLights();
				}catch(Exception e){
					
				}
			}
		};
		t.start();
	}
	
	public void switchLights(){
		this.getChildren().remove(1);
		this.getChildren().remove(2);
		if(!isSwitched){
			this.getChildren().addAll(path3, path4);
			isSwitched = true;
		}else{
			this.getChildren().addAll(path1, path2);
			isSwitched = false;
		}
	}
}
