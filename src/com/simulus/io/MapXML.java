package com.simulus.io;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.simulus.util.enums.Direction;
import com.simulus.view.Tile;
import com.simulus.view.intersection.IntersectionTile;
import com.simulus.view.map.Block;
import com.simulus.view.map.City;
import com.simulus.view.map.Dirt;
import com.simulus.view.map.Grass;
import com.simulus.view.map.Land;
import com.simulus.view.map.Lane;
import com.simulus.view.map.Map;

public class MapXML {

	File inputXmlFile;
	File outputXmlFile;

	NodeList nList;
	Node nNode;
	Element eElement;

	public String mapName;
	public String mapCreationDate;
	public String mapDescription;
	public String mapAuthor;

	int canvasSize;
	public int numOfTiles;
	boolean validated;

	private int xPos;
	private int yPos;
	private String type;
	private String subType;
	private String direction;
	private String laneNo;
	private String isBlock;

	Map importedMap = new Map();
	public Tile[][] fullGrid;

	public MapXML() {

	}

	// reads XML file and returns 2D array of type tile. Error thrown if <tile>
	// nodes do not match metadata i.e. grid size matching height x width
	public void readXML(String inputFile) {

		try {

			File inputXmlFile = new File(inputFile);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			nList = doc.getElementsByTagName("map_details");
			nNode = nList.item(0);
			eElement = (Element) nNode;

			mapName = eElement.getElementsByTagName("name").item(0)
					.getTextContent();
			mapCreationDate = eElement.getElementsByTagName("date").item(0)
					.getTextContent();
			mapDescription = eElement.getElementsByTagName("description")
					.item(0).getTextContent();
			mapAuthor = eElement.getElementsByTagName("author").item(0)
					.getTextContent();

			nList = doc.getElementsByTagName("map_specs");
			nNode = nList.item(0);
			eElement = (Element) nNode;

			canvasSize = Integer.parseInt(eElement
					.getElementsByTagName("canvas_size").item(0).getTextContent());

			numOfTiles = Integer.parseInt(eElement
					.getElementsByTagName("number_of_tiles").item(0)
					.getTextContent());
			
			validated = Boolean.parseBoolean(eElement
					.getElementsByTagName("validated").item(0)
					.getTextContent());
			
			int tileSize = 800 / numOfTiles;
			
			fullGrid = new Tile[numOfTiles][numOfTiles];

			nList = doc.getElementsByTagName("tile");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					eElement = (Element) nNode;

					xPos = Integer.parseInt(eElement.getElementsByTagName("x")
							.item(0).getTextContent());
					yPos = Integer.parseInt(eElement.getElementsByTagName("y")
							.item(0).getTextContent());
					type = eElement.getElementsByTagName("type").item(0)
							.getTextContent();
					subType = eElement.getElementsByTagName("subType")
							.item(0).getTextContent();
					direction = eElement.getElementsByTagName("direction")
							.item(0).getTextContent();
					laneNo = eElement.getElementsByTagName("laneNo")
							.item(0).getTextContent();
					isBlock = eElement.getElementsByTagName("isBlock")
							.item(0).getTextContent();
					
					
					//TODO building a [][] of type tile and land tile.
					// check regarding empty tile and how to add lane tiles
					//we return the full [][]
					switch(type){
						case "": //add empty tile
						fullGrid[xPos][yPos] = new Tile(xPos * tileSize, yPos * tileSize, tileSize,
									 tileSize, xPos, yPos);
						System.out.println(xPos + ":" + yPos + " " + "Empty");
						break;
						case "land":	
							switch(subType){
							case "GRASS":
								 fullGrid[xPos][yPos] = new Grass(xPos * tileSize, yPos * tileSize, tileSize,
										 tileSize, xPos, yPos);
								break;
								case "DIRT":  //add dirt tile
								fullGrid[xPos][yPos] = new Dirt(xPos * tileSize, yPos * tileSize, tileSize,
										 tileSize, xPos, yPos);
								break;
								case "CITY":  //add city tile
								fullGrid[xPos][yPos] = new City(xPos * tileSize, yPos * tileSize, tileSize,
										 tileSize, xPos, yPos);
								break;
//								case "BLOCK":  // TODO: add block tile
//									fullGrid[xPos][yPos] = new Block(xPos * tileSize, yPos * tileSize, tileSize,
//											 tileSize, xPos, yPos, Direction.NONE, Integer.parseInt(attribute2));
//								break;
							}
							System.out.println(xPos + ":" + yPos + " " + subType);
						break;
						case "lane":  //add lane tile
							switch(direction){
								case "EAST": //add east tile
									fullGrid[xPos][yPos] = new Lane(xPos * tileSize, yPos * tileSize, tileSize,
											 tileSize, xPos, yPos, Direction.EAST, Integer.parseInt(laneNo), Boolean.parseBoolean(isBlock));
								break;
								case "WEST": //add west tile
									fullGrid[xPos][yPos] = new Lane(xPos * tileSize, yPos * tileSize, tileSize,
											 tileSize, xPos, yPos, Direction.WEST, Integer.parseInt(laneNo), Boolean.parseBoolean(isBlock));
								break;
								case "NORTH": //add north tile
									fullGrid[xPos][yPos] = new Lane(xPos * tileSize, yPos * tileSize, tileSize,
											 tileSize, xPos, yPos, Direction.NORTH, Integer.parseInt(laneNo), Boolean.parseBoolean(isBlock));
								break;
								case "SOUTH": //add south tile
									fullGrid[xPos][yPos] = new Lane(xPos * tileSize, yPos * tileSize, tileSize,
											 tileSize, xPos, yPos, Direction.SOUTH, Integer.parseInt(laneNo), Boolean.parseBoolean(isBlock));
								break;
							}
							System.out.println(xPos + ":" + yPos + " " + direction + " Lane: " + laneNo + " isBlock: " + isBlock);
						break;
						case "intersection":
							fullGrid[xPos][yPos] = new IntersectionTile(xPos * tileSize, yPos * tileSize, tileSize,
											 tileSize, xPos, yPos);
							System.out.println(xPos + ":" + yPos + " " + type);
						break;
					}
 
				}

			}

		} catch (Exception e) {
			System.out
					.println("Check that the input file exists and that it matches XML map schema.");
			System.out.println("Error reported:");
			e.printStackTrace();
		}

		//return fullGrid;

	}

	// outputs XML file based on given 2D array of type tile plus metadata
	public void writeXML(Tile[][] gridIn, String outputFile, String nameIn,
			String dateIn, String descIn, String authIn, int canvasSizeIn, int numOfTilesIn, boolean validatedIn) {

		try {

			outputXmlFile = new File(outputFile);

			mapName = nameIn;
			mapCreationDate = dateIn;
			mapDescription = descIn;
			mapAuthor = authIn;
			
			canvasSize = canvasSizeIn;
			numOfTiles = numOfTilesIn;
			validated = validatedIn;

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root element - map
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("map");
			doc.appendChild(rootElement);

			// create a comment with team tag
			Comment eComment = doc
					.createComment("Team Simulus Traffic Simulator Map File");
			doc.insertBefore(eComment, rootElement);

			// map_details
			Element eMapDetails = doc.createElement("map_details");
			rootElement.appendChild(eMapDetails);

			Element eName = doc.createElement("name");
			eName.appendChild(doc.createTextNode(mapName));
			eMapDetails.appendChild(eName);

			Element eDate = doc.createElement("date");
			eDate.appendChild(doc.createTextNode(mapCreationDate));
			eMapDetails.appendChild(eDate);

			Element eDescription = doc.createElement("description");
			eDescription.appendChild(doc.createTextNode(mapDescription));
			eMapDetails.appendChild(eDescription);

			Element eAuthor = doc.createElement("author");
			eAuthor.appendChild(doc.createTextNode(mapAuthor));
			eMapDetails.appendChild(eAuthor);

			// map_specs
			Element eMapSpecs = doc.createElement("map_specs");
			rootElement.appendChild(eMapSpecs);

			Element eCanvasSize = doc.createElement("canvas_size");
			eCanvasSize.appendChild(doc.createTextNode(Integer
					.toString(canvasSize)));
			eMapSpecs.appendChild(eCanvasSize);
			
			Element eNumOfTiles = doc.createElement("number_of_tiles");
			eNumOfTiles.appendChild(doc.createTextNode(Integer.toString(numOfTiles)));
			eMapSpecs.appendChild(eNumOfTiles);
			
			Element eValidated = doc.createElement("validated");
			eValidated.appendChild(doc.createTextNode(Boolean.toString(validated)));
			eMapSpecs.appendChild(eValidated);

			// grid elements
			Element eGrid = doc.createElement("grid");
			rootElement.appendChild(eGrid);
			

			for (int r = 0; r < numOfTiles; r++) {
				for (int c = 0; c < numOfTiles; c++) {

					String tileType = "";
					String subType = "";
					String direction = "";
					String laneNo = "";
					String isBlock = "";
					Tile t = gridIn[c][r];
					
					if (t instanceof Lane) {
						tileType = "lane";
						direction = ((Lane) t).getDirection().toString();
						laneNo = Integer.toString(((Lane) t).getLaneNo());
						isBlock = Boolean.toString((((Lane) t).getIsBlock()));
					} else if (t instanceof Land) {
						tileType = "land";
						subType = ((Land) t).getLandType().toString();
					} else if (t instanceof IntersectionTile) {
						tileType = "intersection";
					}
					
					Element eTile = doc.createElement("tile");
					eGrid.appendChild(eTile);

					Element eX = doc.createElement("x");
					eX.appendChild(doc.createTextNode(String.format("%02d", c)));
					eTile.appendChild(eX);

					Element eY = doc.createElement("y");
					eY.appendChild(doc.createTextNode(String.format("%02d", r)));
					eTile.appendChild(eY);
					
					Element eType = doc.createElement("type");
					eType.appendChild(doc.createTextNode(tileType));
					eTile.appendChild(eType);
					
					Element eSubType = doc.createElement("subType");
					eSubType.appendChild(doc.createTextNode(subType));
					eTile.appendChild(eSubType);
					
					Element eDirection = doc.createElement("direction");
					eDirection.appendChild(doc
							.createTextNode(direction));
					eTile.appendChild(eDirection);

					Element eLaneNo = doc.createElement("laneNo");
					eLaneNo.appendChild(doc
							.createTextNode(laneNo));
					eTile.appendChild(eLaneNo);

					Element eIsBlock = doc.createElement("isBlock");
					eIsBlock.appendChild(doc
							.createTextNode(isBlock));
					eTile.appendChild(eIsBlock);
					
				}

			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outputXmlFile);

			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}
	
	public Tile[][] getTileGrid(){
		return fullGrid;
	}
	
	public String toString() {

		return "Map Name: " + mapName + "\n" + "Date: " + mapCreationDate + "\n"
				+ "Description: " + mapDescription + "\n"
				+ "Author: " + mapAuthor + "\n"
				+ "Canvas Size: " + canvasSize + "\n"
				+ "Number of Tiles: " + numOfTiles + "\n"
				+ "Map Validated: " + validated;
	}

}