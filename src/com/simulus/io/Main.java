package com.simulus.io;

public class Main {

	public static void main(String[] args) {
	
		MapXML m1 = new MapXML();
		TileXML[][] t;
		
		t = m1.readXML("C:\\Users\\Administrator\\Desktop\\map.xml");
		
		for (int i = 0; i < t.length; i++){
			for (int j = 0; j < t.length; j++){
			
				if("land".equals(t[j][i].type)){
					System.out.print("++");
				}else if("lane".equals(t[j][i].type) && "east".equals(t[j][i].direction)){
					System.out.print("==");
				}else {
					System.out.print("||");
				}
				
			
			}
			System.out.println("");
		}
		
		MapXML m2 = new MapXML();
		
		m2.writeXML(t,"C:\\Users\\Administrator\\Desktop\\savedmap.xml", "Strand Intersection", "20-02-2015", "The chaos of the Strand", "Alan", 5, 5, 1, 1);
		
		
		SimConfigXML s = new SimConfigXML();
		s.readXML("C:\\Users\\Administrator\\Desktop\\simconfig.xml");
		
		s.writeXML("C:\\Users\\Administrator\\Desktop\\savedconf.xml", "n", "12-02-2015", "test file", "bob", 10, 20, 30, 40, 50, true);
		System.out.println();
		System.out.println("Simulation ran with following the config:");
		System.out.println(s.toString());
		
		
	}

}
