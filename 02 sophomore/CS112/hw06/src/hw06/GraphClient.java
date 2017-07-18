package hw06;
import java.io.*;
import java.util.*;

/**
 * Matthew Huynh
 * CS112A1 - hw6
 * April 24, 2009
 *
 * GraphClient.java
 * A program that creates a graph from a text file of edges and produces 3 text files:
 * - degreeRanking.txt
 * - clusteringRanking.txt
 * - closenessRanking.txt
 * 
 * The entire processing of graph.txt took ~70 seconds on my AMD64 X2 2.2Ghz,
 * with a max heap size of 768MB (unsure if this is necessary).
 * 
 * Code skeleton provided by Ashwin Thangali.
 */
 
class Pair { 
    int nodeId;
    float nodeRank;
    Pair(int inNodeId, float inRank){
        nodeId = inNodeId;
        nodeRank = inRank;
    }
    int getNodeId(){ return nodeId; }
    float getNodeRank(){ return nodeRank; }
}
 
public class GraphClient {
    public static void main(String[] args) throws Exception{
        
        Graph graph = new Graph("graph.txt");
        // We will compute the graph diameter for the lab example.
        //graph.getGraphDiameter();
        
        Pair [] nodeDegrees = new Pair[graph.getNumNodes()];
        // HOMEWORK TASK: 
        // (a) Complete getNodeDegree() in Graph class
        // (b) Assign node degrees for all nodes into the nodeDegrees array,
        //     create a new Pair instance (nodeId, nodeDegree) for each node.
        for (int i = 0; i < nodeDegrees.length; i++) {
        	nodeDegrees[i] = new Pair (i, graph.getNodeDegree(i));
        }
        mergesort_descending(nodeDegrees);
        writePairArrayToFile(nodeDegrees, "degreeRanking.txt");
        System.out.println("degreeRanking.txt produced");
        
        Pair [] clusteringCoefficients = new Pair[100];
        // HOMEWORK TASK:
        // (a) Complete getClusteringCoefficient() in Graph class
        // (b) Assign node clusteringCoefficients for top 100 nodeDegree vertices
        //     into clusteringCoefficients array, 
        //     create a new Pair instance (nodeId, clusteringCoefficient) for 
        //     top 100 nodeDegree nodes.        
        Scanner degreeRanking = new Scanner(new FileInputStream("degreeRanking.txt"));
        for (int i = 0; i < clusteringCoefficients.length; i++) {
        	int currNode = degreeRanking.nextInt();
        	degreeRanking.nextLine();
        	clusteringCoefficients[i] = new Pair (currNode, graph.getClusteringCoefficient(currNode));
        }
        mergesort_descending(clusteringCoefficients);
        writePairArrayToFile(clusteringCoefficients, "clusteringRanking.txt");
        degreeRanking.close();
        System.out.println("clusteringRanking.txt produced");
        
        Pair [] CLC = new Pair[100];
        // HOMEWORK TASK:
        // (a) Complete getClosenessCentrality() in Graph class
        // (b) Assign node closenessCentrality for top 100 nodeDegree vertices
        //     into CLC array, 
        //     create a new Pair instance (nodeId, closenessCentrality) for 
        //     top 100 nodeDegree nodes.
        degreeRanking  = new Scanner(new FileInputStream("degreeRanking.txt"));
        for (int i = 0; i < clusteringCoefficients.length; i++) {
        	int currNode = degreeRanking.nextInt();
        	degreeRanking.nextLine();
        	CLC[i] = new Pair (currNode, graph.getClosenessCentrality(currNode));
        }
        mergesort_ascending(CLC);
        writePairArrayToFile(CLC, "closenessRanking.txt");
        degreeRanking.close();
        System.out.println("closenessRanking.txt produced");
        
        System.out.println("end of program.");
    }
    
    static void writePairArrayToFile( Pair [] pairs, String outFile ) 
                throws FileNotFoundException{
        PrintStream out = new PrintStream(outFile);
        for( int i = 0; i < Math.min(pairs.length, 1000); i++ )
        	out.printf("%7d %12.6f\r\n", pairs[i].getNodeId(), pairs[i].getNodeRank());
        out.close();
    }
    
    static void mergesort_ascending( Pair [] A ){
        mergesort_descending(A);
        for( int i = 0; i < A.length/2; i++ ){
            Pair temp = A[i];
            A[i] = A[A.length - 1 - i];
            A[A.length - 1 - i] = temp;
        }
    }
    
    public static void mergesort_descending( Pair [] A ){
        int chunk = 2;
        while(true) {
            for( int chunkStart = 0; chunkStart < A.length; chunkStart += chunk ){
                int start = chunkStart;
                int middle = chunkStart + chunk/2;
                int end = chunkStart + chunk - 1;
                if( middle >= A.length )
                    break;
                if( end >= A.length )
                    end = A.length - 1;
                merge(A, start, middle, end);
            }
            if( chunk > A.length )
                break;
            else
                chunk *= 2;
        }
    }
    
    static void merge (Pair [] A,  int leftPos, int rightPos, int rightEnd){
        int leftEnd = rightPos - 1;
        int numElements = rightEnd - leftPos + 1;
        Pair [] B = new Pair[numElements];
        int bPos = 0;
 
        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (A[leftPos].nodeRank >= A[rightPos].nodeRank)
                B[bPos++] = A[leftPos++];
            else
            B[bPos++] = A[rightPos++];
        }
        while (leftPos <= leftEnd) 
            B[bPos++] = A[leftPos++];
            
        while (rightPos <= rightEnd) 
            B[bPos++] = A[rightPos++];
 
        leftPos = rightEnd - numElements + 1;
        for (bPos = 0; bPos < numElements; bPos ++)
            A[leftPos++] = B[bPos];
    }
} 
