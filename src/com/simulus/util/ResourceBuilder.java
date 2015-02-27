package com.simulus.util;

import javafx.scene.image.Image;

public class ResourceBuilder {

    private static Image ewLaneTexture = new Image(ResourceBuilder.class.getResource("/resources/roadtile_eastwest.png").toString());
    private static Image nsLaneTexture = new Image(ResourceBuilder.class.getResource("/resources/roadtile_northsouth.png").toString());
    private static Image landTexture = new Image(ResourceBuilder.class.getResource("/resources/land.png").toString());


    public static Image getEWLaneTexture() {
        return ewLaneTexture;
    }

    public static Image getNSLaneTexture() {
        return nsLaneTexture;
    }

    public static Image getLandTexture() {
        return landTexture;
    }

}
