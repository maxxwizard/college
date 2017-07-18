import java.io.*;
import java.util.*;

/**
 * Matthew Huynh
 * CS112A1 - hw
 * May 1, 2008
 *
 * GraphClient.java
 * This application takes in a network data file and outputs
 * three measures of centrality for each vertex.
 */

public class GraphClient {

	public static final String DATA = "network.txt";
	
	public static void main(String[] args) {
	
		Scanner data = null;
		try {
			data = new Scanner(new File(DATA));
		} catch (FileNotFoundException e) {
			System.out.println("Data file '" + DATA + "' not found!");
			e.printStackTrace();
		}
		
		GraphToolbox network = new GraphToolbox();
		
		while (data.hasNextLine())
		{
			String s = data.nextLine();
			String[] vertex = s.split("\t");
			if (network.map.containsKey(vertex[0]))
			{
				network.map.get(vertex[0]).add(vertex[1]);
			} else {
				network.map.put(vertex[0], new HashSet<String>());
				network.map.get(vertex[0]).add(vertex[1]);
			}
		}
		
		// DEGREE RANKING OUTPUT TO FILE
		try {
			network.degreeRanking("degreeRanking.txt");
			System.out.println("degreeRanking.txt created");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
