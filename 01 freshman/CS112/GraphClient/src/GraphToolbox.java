import java.io.*;
import java.util.*;

/**
 * Matthew Huynh
 * CS112A1 - hw
 * May 1, 2008
 *
 * GraphToolbox.java
 * Class that stores the network data in a customized HashMap and has
 * functions for outputting centrality measures for the vertices.
 */

public class GraphToolbox {
	
	public HashMap<String,HashSet<String>> map;

	public GraphToolbox()
	{
		map = new HashMap<String, HashSet<String>>();
	}
	
	public void degreeRanking(String filename) throws FileNotFoundException
	{
		PrintStream output = new PrintStream(new FileOutputStream(new File(filename)));
		
		String[] vertices = new String[map.keySet().size()];
		int[] degrees = new int[map.keySet().size()];
		map.keySet().toArray(vertices);
		
		for (int i = 0; i < vertices.length; i++)
		{
			degrees[i] = map.get(vertices[i]).size();
		}
		
		bubbleSort(degrees, vertices);
		
		for (int i = 0; i < degrees.length; i++)
		{
			output.println(degrees[i] + "\t" + vertices[i]);
		}
		
	}
	
	public void betweennessRanking(String filename) throws FileNotFoundException
	{
		PrintStream output = new PrintStream(new FileOutputStream(new File(filename)));
		
		HashMap<String,int> counters = new HashMap<String,int>();
		HashMap<String,String> preds = new HashMap<String,String>();
		
		Iterator it = map.entrySet().iterator();
		
		// COPY VERTICES
		
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry) it.next();
			String vertex = (String) entry.getKey();
			HashSet neighbors = (HashSet) entry.getValue();
		}
		
	}
	
	private void shortestPath (String v, HashSet n)
	{
		for (String v : n)
		{
			v
		}
	}
	
	public static void bubbleSort(int[] a, String[] b)
	{
		int temp;
		String temp2;
		for (int i = 0; i < a.length-1; i++) {
			for (int j = 0; j < a.length-1-i; j++)
				if (a[j+1] > a[j]) {  
					temp = a[j];
					temp2 = b[j];
					a[j] = a[j+1];
					b[j] = b[j+1];
					a[j+1] = temp;
					b[j+1] = temp2;
				}
		}
	}
	
}
