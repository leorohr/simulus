package com.simulus.mapping;

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
		
		m2.writeXML(t,"C:\\Users\\Administrator\\Desktop\\saved.xml", "Strand Intersection", "20-02-2015", "The chaos of the Strand", "Alan", 5, 5, 1, 1);
		
	}

}
