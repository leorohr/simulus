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

import com.simulus.view.Land;
import com.simulus.view.Lane;
import com.simulus.view.Tile;

import java.io.File;

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
	int numOfTiles;

	private int xPos;
	private int yPos;
	private String type;
	private String attribute;

	TileXML[][] fullGrid;

	public MapXML() {

	}

	// reads XML file and returns 2D array of type tile. Error thrown if <tile>
	// nodes do not match metadata i.e. grid size matching height x width
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

			canvasSize = Integer.parseInt(eElement
					.getElementsByTagName("canvas_size").item(0).getTextContent());

			numOfTiles = Integer.parseInt(eElement
					.getElementsByTagName("number_of_tiles").item(0)
					.getTextContent());

			fullGrid = new TileXML[numOfTiles][numOfTiles];

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
					attribute = eElement.getElementsByTagName("attribute")
							.item(0).getTextContent();

					fullGrid[xPos][yPos] = new TileXML(type, attribute);

				}

			}

		} catch (Exception e) {
			System.out
					.println("Check that the input file exisits and that it matches XML map schema.");
			System.out.println("Error reported:");
			e.printStackTrace();
		}

		return fullGrid;

	}

	// outputs XML file based on given 2D array of type tile plus metadata
	public void writeXML(Tile[][] gridIn, String outputFile, String nameIn,
			String dateIn, String descIn, String authIn, int canvasSizeIn, int numOfTilesIn) {

		try {

			outputXmlFile = new File(outputFile);

			mapName = nameIn;
			mapCreationDate = dateIn;
			mapDescription = descIn;
			mapAuthor = authIn;
			
			canvasSize = canvasSizeIn;
			numOfTiles = numOfTilesIn;

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

			// grid elements
			Element eGrid = doc.createElement("grid");
			rootElement.appendChild(eGrid);
			

			for (int r = 0; r < numOfTiles; r++) {
				for (int c = 0; c < numOfTiles; c++) {

					String tileType = "";
					String tileAttribute = "";
					Tile t = gridIn[c][r];
					
					if (t instanceof Lane) {
						tileType = "lane";
						tileAttribute = ((Lane) t).getDirection().toString();
					} else if (t instanceof Land) {
						tileType = ((Land) t).getLandType().toString();
						tileAttribute = "";
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

					Element eAttribute = doc.createElement("attribute");
					eAttribute.appendChild(doc
							.createTextNode(tileAttribute));
					eTile.appendChild(eAttribute);

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
	
	public String toString() {

		return "Map Name: " + mapName + "\n" + "Date: " + mapCreationDate + "\n"
				+ "Description: " + mapDescription + "\n"
				+ "Author: " + mapAuthor + "\n"
				+ "Canvas Size: " + canvasSize + "\n"
				+ "Number of Tiles: " + numOfTiles;
	}
	



}
