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

public class SimConfigXML {

	File inputXmlFile;
	File outputXmlFile;

	NodeList nList;
	Node nNode;
	Element eElement;

	String simName;
	String simCreationDate;
	String simDescription;
	String simAuthor;
	
	int noCars;
	int tickRate;
	int spawnRate;
	int maxSpeedCars;
	int carTruckRatio;
	boolean debugMode;


	public SimConfigXML() {

	}

	//reads XML file and returns 2D array of type tile.  Error thrown if <tile> nodes do not match metadata i.e. grid size matching height x width
	public void readXML(String inputFile) {

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

			nList = doc.getElementsByTagName("sim_details");
			nNode = nList.item(0);
			eElement = (Element) nNode;

			simName = eElement.getElementsByTagName("name").item(0)
					.getTextContent();
			simCreationDate = eElement.getElementsByTagName("date").item(0)
					.getTextContent();
			simDescription = eElement.getElementsByTagName("description")
					.item(0).getTextContent();
			simAuthor = eElement.getElementsByTagName("author").item(0)
					.getTextContent();
			
			nList = doc.getElementsByTagName("sim_specs");
			nNode = nList.item(0);
			eElement = (Element) nNode;
			
			noCars = Integer.parseInt(eElement
					.getElementsByTagName("no_of_cars").item(0).getTextContent());
			tickRate = Integer.parseInt(eElement
					.getElementsByTagName("tickrate").item(0).getTextContent());
			spawnRate = Integer.parseInt(eElement
					.getElementsByTagName("spawnrate").item(0).getTextContent());
			maxSpeedCars = Integer.parseInt(eElement
					.getElementsByTagName("max_speed_cars").item(0).getTextContent());
			carTruckRatio = Integer.parseInt(eElement
					.getElementsByTagName("car_truck_ratio").item(0).getTextContent());
			debugMode = Boolean.parseBoolean(eElement.getElementsByTagName("debug").item(0).getTextContent());
			

		} catch (Exception e) {
			System.out.println("Check that the input file exisits and that it matches XML simulation schema.");
			System.out.println("Error reported:");
			e.printStackTrace();
		}

	}

	//outputs XML file based on given 2D array of type tile plus metadata
	public void writeXML(String outputFile, String nameIn, String dateIn, String descIn, String authIn,
			int noCarsIn, int tickRateIn, int spawnRateIn, int maxSpeedIn, int carTruckRatioIn, boolean debugIn) {

		try {
			
			outputXmlFile = new File(outputFile);
			
			simName = nameIn;
			simCreationDate = dateIn;
			simDescription = descIn;
			simAuthor = authIn;
			
			noCars = noCarsIn;
			tickRate = tickRateIn;
			spawnRate = spawnRateIn;
			maxSpeedCars = maxSpeedIn;
			carTruckRatio = carTruckRatioIn;
			debugMode = debugIn;
			

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root element - simulation
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("simulation");
			doc.appendChild(rootElement);
			
			// create a comment with team tag
			Comment comment = doc.createComment("Team Simulus Traffic Simulator Simulation File");
			doc.insertBefore(comment, rootElement);

			// map_details
			Element simDetails = doc.createElement("sim_details");
			rootElement.appendChild(simDetails);

			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(simName));
			simDetails.appendChild(name);

			Element date = doc.createElement("date");
			date.appendChild(doc.createTextNode(simCreationDate));
			simDetails.appendChild(date);

			Element description = doc.createElement("description");
			description.appendChild(doc.createTextNode(simDescription));
			simDetails.appendChild(description);

			Element author = doc.createElement("author");
			author.appendChild(doc.createTextNode(simAuthor));
			simDetails.appendChild(author);

			// map_specs
			Element simSpecs = doc.createElement("sim_specs");
			rootElement.appendChild(simSpecs);

			Element eNoCars = doc.createElement("no_of_cars");
			eNoCars.appendChild(doc.createTextNode(Integer.toString(noCars)));
			simSpecs.appendChild(eNoCars);

			Element eTickRate = doc.createElement("tickrate");
			eTickRate.appendChild(doc.createTextNode(Integer.toString(tickRate)));
			simSpecs.appendChild(eTickRate);

			Element eSpawnRate = doc.createElement("spawnrate");
			eSpawnRate.appendChild(doc.createTextNode(Integer
					.toString(spawnRate)));
			simSpecs.appendChild(eSpawnRate);

			Element eMaxSpeedCars = doc.createElement("max_speed_cars");
			eMaxSpeedCars.appendChild(doc.createTextNode(Integer
					.toString(maxSpeedCars)));
			simSpecs.appendChild(eMaxSpeedCars);
			
			Element eCartruckRatio = doc.createElement("car_truck_ratio");
			eCartruckRatio.appendChild(doc.createTextNode(Integer
					.toString(carTruckRatio)));
			simSpecs.appendChild(eCartruckRatio);
			
			Element eDubug = doc.createElement("debug");
			eDubug.appendChild(doc.createTextNode(Boolean
					.toString(debugMode)));
			simSpecs.appendChild(eDubug);
			

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

	public String toString() {
	
		return "No of Cars: " + noCars + "\n" + "Tickrate: " + tickRate + "\n" + "Max Speed of Cars: " + maxSpeedCars + 
				"\n" + "Car to Truck Ratio: " + carTruckRatio + "\n" + "Debug Mode: " + debugMode;
}

}
