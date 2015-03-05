package com.simulus.util;

import javafx.scene.image.Image;

public class ResourceBuilder {

    private static Image EWLaneTexture = new Image(ResourceBuilder.class.getResource("/resources/roadtile_eastwest.png").toString());
    private static Image NSLaneTexture = new Image(ResourceBuilder.class.getResource("/resources/roadtile_northsouth.png").toString());
    private static Image doubleNSLaneTexture = new Image(ResourceBuilder.class.getResource("/resources/double_northsouth.png").toString());
    private static Image doubleEWLaneTexture = new Image(ResourceBuilder.class.getResource("/resources/double_eastwest.png").toString());
    private static Image landTexture = new Image(ResourceBuilder.class.getResource("/resources/land.png").toString());
    private static Image dirtTexture= new Image(ResourceBuilder.class.getResource("/resources/dirt.png").toString());
    private static Image grassTexture = new Image(ResourceBuilder.class.getResource("/resources/grass.png").toString());
    private static Image blockTexture = new Image(ResourceBuilder.class.getResource("/resources/block.png").toString());
    private static Image boxjunctionTexture = new Image(ResourceBuilder.class.getResource("/resources/boxjunction.png").toString());
    private static Image cityTexture = new Image(ResourceBuilder.class.getResource("/resources/city.png").toString());
    
    private static Image blockCursor = new Image(ResourceBuilder.class.getResource("/resources/csr_block.png").toString());
    private static Image boxjunctionCursor = new Image(ResourceBuilder.class.getResource("/resources/csr_boxjunction.png").toString());
    private static Image cityCursor = new Image(ResourceBuilder.class.getResource("/resources/csr_city.png").toString());
    private static Image dirtCursor = new Image(ResourceBuilder.class.getResource("/resources/csr_dirt.png").toString());
    private static Image EWLaneCursor = new Image(ResourceBuilder.class.getResource("/resources/csr_eastwest.png").toString());
    private static Image NSLaneCursor = new Image(ResourceBuilder.class.getResource("/resources/csr_northsouth.png").toString());
    private static Image eraseCursor = new Image(ResourceBuilder.class.getResource("/resources/csr_erase.png").toString());
    private static Image grassCursor = new Image(ResourceBuilder.class.getResource("/resources/csr_grass.png").toString());
    
    private static Image deleteIcon = new Image(ResourceBuilder.class.getResource("/resources/delete.png").toString());
    private static Image disketteIcon = new Image(ResourceBuilder.class.getResource("/resources/diskette.png").toString());
    private static Image resizeIcon = new Image(ResourceBuilder.class.getResource("/resources/resize_icon.png").toString());
    private static Image eraseIcon = new Image(ResourceBuilder.class.getResource("/resources/erase.png").toString());
    private static Image folderIcon = new Image(ResourceBuilder.class.getResource("/resources/folder.png").toString());
    private static Image openIconSmall = new Image(ResourceBuilder.class.getResource("/resources/ic_open_sm.png").toString());
    private static Image openIcon = new Image(ResourceBuilder.class.getResource("/resources/ic_open.png").toString());
    private static Image saveIcon = new Image(ResourceBuilder.class.getResource("/resources/ic_save.png").toString());
    private static Image saveIconSmall = new Image(ResourceBuilder.class.getResource("/resources/ic_save_sm.png").toString());
    private static Image validateIcon = new Image(ResourceBuilder.class.getResource("/resources/validate.png").toString());
    
    private static Image logoSmall = new Image(ResourceBuilder.class.getResource("/resources/logo1_small.png").toString());

	public static Image getEWLaneTexture() {
		return EWLaneTexture;
	}

	public static Image getNSLaneTexture() {
		return NSLaneTexture;
	}

	public static Image getDoubleNSLaneTexture() {
		return doubleNSLaneTexture;
	}

	public static Image getDoubleEWLaneTexture() {
		return doubleEWLaneTexture;
	}

	public static Image getLandTexture() {
		return landTexture;
	}

	public static Image getDirtTexture() {
		return dirtTexture;
	}

	public static Image getGrassTexture() {
		return grassTexture;
	}

	public static Image getBlockTexture() {
		return blockTexture;
	}

	public static Image getBoxjunctionTexture() {
		return boxjunctionTexture;
	}

	public static Image getCityTexture() {
		return cityTexture;
	}

	public static Image getBlockCursor() {
		return blockCursor;
	}

	public static Image getBoxjunctionCursor() {
		return boxjunctionCursor;
	}

	public static Image getCityCursor() {
		return cityCursor;
	}

	public static Image getDirtCursor() {
		return dirtCursor;
	}

	public static Image getEWLaneCursor() {
		return EWLaneCursor;
	}

	public static Image getNSLaneCursor() {
		return NSLaneCursor;
	}

	public static Image getEraseCursor() {
		return eraseCursor;
	}

	public static Image getGrassCursor() {
		return grassCursor;
	}

	public static Image getDeleteIcon() {
		return deleteIcon;
	}

	public static Image getDisketteIcon() {
		return disketteIcon;
	}

	public static Image getResizeIcon() {
		return resizeIcon;
	}

	public static Image getEraseIcon() {
		return eraseIcon;
	}

	public static Image getFolderIcon() {
		return folderIcon;
	}

	public static Image getOpenIconSmall() {
		return openIconSmall;
	}

	public static Image getOpenIcon() {
		return openIcon;
	}

	public static Image getSaveIcon() {
		return saveIcon;
	}

	public static Image getSaveIconSmall() {
		return saveIconSmall;
	}

	public static Image getValidateIcon() {
		return validateIcon;
	}

	public static Image getLogoSmall() {
		return logoSmall;
	}  
}
