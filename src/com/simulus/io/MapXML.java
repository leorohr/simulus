package com.simulus.io;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

public class MapXML {

	File inputXmlFile;
	File outputXmlFile;

	NodeList nList;
	Node nNode;
	Element eElement;

	String mapName;
	String mapCreationDate;
	String mapDescription;
	String mapAuthor;

	int mapHeight;
	int mapWidth;
	int mapTileHeight;
	int mapTileWidth;

	private int xPos;
	private int yPos;
	private String type;
	private String direction;

	TileXML[][] fullGrid;

	public MapXML() {

	}

	//reads XML file and returns 2D array of type tile.  Error thrown if <tile> nodes do not match metadata i.e. grid size matching height x width
	public TileXML[][] readXML(String inputFile) {

		try {

			inputXmlFile = new File(inputFile);

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

			mapHeight = Integer.parseInt(eElement
					.getElementsByTagName("height").item(0).getTextContent());
			mapWidth = Integer.parseInt(eElement.getElementsByTagName("width")
					.item(0).getTextContent());
			mapTileHeight = Integer.parseInt(eElement
					.getElementsByTagName("tile_size_height").item(0)
					.getTextContent());
			mapTileWidth = Integer.parseInt(eElement
					.getElementsByTagName("tile_size_width").item(0)
					.getTextContent());

			fullGrid = new TileXML[mapWidth / mapTileWidth][mapHeight / mapTileHeight];
			
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
					direction = eElement.getElementsByTagName("direction")
							.item(0).getTextContent();

					fullGrid[xPos][yPos] = new TileXML(type, direction);

				}
				
				
			}
			


		} catch (Exception e) {
			System.out.println("Check that the input file exisits and that it matches XML map schema.");
			System.out.println("Error reported:");
			e.printStackTrace();
		}

		return fullGrid;

	}

	//outputs XML file based on given 2D array of type tile plus metadata
	public void writeXML(TileXML[][] gridIn, String outputFile, String nameIn, String dateIn, String descIn, String authIn,
			int mHeightIn, int mWidthIn, int tHeightIn, int tWidthIn) {

		try {
			
			fullGrid = gridIn;
			outputXmlFile = new File(outputFile);
			
			mapName = nameIn;
			mapCreationDate = dateIn;
			mapDescription = descIn;
			mapAuthor = authIn;

			mapHeight = mHeightIn;
			mapWidth = mWidthIn;
			mapTileHeight = tHeightIn;
			mapTileWidth = tWidthIn;
			

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root element - map
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("map");
			doc.appendChild(rootElement);
			
			// create a comment with team tag
			Comment comment = doc.createComment("Team Simulus Traffic Simulator Map File");
			doc.insertBefore(comment, rootElement);

			// map_details
			Element mapDetails = doc.createElement("map_details");
			rootElement.appendChild(mapDetails);

			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(mapName));
			mapDetails.appendChild(name);

			Element date = doc.createElement("date");
			date.appendChild(doc.createTextNode(mapCreationDate));
			mapDetails.appendChild(date);

			Element description = doc.createElement("description");
			description.appendChild(doc.createTextNode(mapDescription));
			mapDetails.appendChild(description);

			Element author = doc.createElement("author");
			author.appendChild(doc.createTextNode(mapAuthor));
			mapDetails.appendChild(author);

			// map_specs
			Element mapSpecs = doc.createElement("map_specs");
			rootElement.appendChild(mapSpecs);

			Element height = doc.createElement("height");
			height.appendChild(doc.createTextNode(Integer.toString(mapHeight)));
			mapSpecs.appendChild(height);

			Element width = doc.createElement("width");
			width.appendChild(doc.createTextNode(Integer.toString(mapWidth)));
			mapSpecs.appendChild(width);

			Element tile_size_height = doc.createElement("tile_size_height");
			tile_size_height.appendChild(doc.createTextNode(Integer
					.toString(mapTileHeight)));
			mapSpecs.appendChild(tile_size_height);

			Element tile_size_width = doc.createElement("tile_size_width");
			tile_size_width.appendChild(doc.createTextNode(Integer
					.toString(mapTileWidth)));
			mapSpecs.appendChild(tile_size_width);

			// grid elements
			Element grid = doc.createElement("grid");
			rootElement.appendChild(grid);
			

			for (int r = 0; r < (mapHeight / mapTileHeight); r++) {
				for (int c = 0; c < (mapWidth / mapTileWidth); c++) {

					Element tile = doc.createElement("tile");
					grid.appendChild(tile);

					Element x = doc.createElement("x");
					x.appendChild(doc.createTextNode(String.format("%02d", c)));
					tile.appendChild(x);

					Element y = doc.createElement("y");
					y.appendChild(doc.createTextNode(String.format("%02d", r)));
					tile.appendChild(y);
					
				
					Element type = doc.createElement("type");
					type.appendChild(doc.createTextNode(fullGrid[c][r].type));
					tile.appendChild(type);

					Element direction = doc.createElement("direction");
					direction.appendChild(doc
							.createTextNode(fullGrid[c][r].direction));
					tile.appendChild(direction);

				}

			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outputXmlFile);

			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}

	public TileXML[][] getGrid() {
	
		return fullGrid;
	}

}
