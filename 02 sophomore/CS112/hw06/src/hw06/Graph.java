package hw06;
import java.io.*;
import java.math.*;
import java.util.*;

/**
 * Matthew Huynh
 * CS112A1 - hw6
 * April 24, 2009
 *
 * Graph.java
 * Data structures for use by GraphClient.java to process 3 types of ranking
 * on an input graph.
 * 
 * Code skeleton provided by Ashwin Thangali.
 */
 
public class Graph {
    
    // adjacencyList is an array of NodeLists, the NodeList class is defined below. 
    NodeList [] adjacencyList;
    
    // numNodes is number of nodes in the graph
    int numNodes;
    int getNumNodes(){ return numNodes; }
    
    public Graph(String graphFileName) throws FileNotFoundException {
        
        Scanner fileScanner = new Scanner(new FileInputStream(graphFileName));
        int maxNodeId = -1;
 
        // TASK: Get the maximum NodeId in the input file.
        while (fileScanner.hasNextInt()) {
        	int nextInt = fileScanner.nextInt();
        	if (nextInt > maxNodeId)
        		maxNodeId = nextInt;
        }
        
        fileScanner.close();
        
        // Set numNodes: note that the nodeIds are in the range [1, maxNodeId].
        numNodes = maxNodeId + 1;  
        
        // TASKS:
        // (a) initialize adjacencyList to NodeList array of length numNodes.
        adjacencyList = new NodeList[numNodes];
        // (b) initialize each adjacencyList element to a new NodeList.
        for (int i = 0; i < adjacencyList.length; i++) {
        	adjacencyList[i] = new NodeList();
        }
        
        // Reopen the file to construct the graph.
        fileScanner = new Scanner(new FileInputStream(graphFileName));
        while( fileScanner.hasNextInt() ){
            // TASKS: 
            // (a) Read a pair of nodeIds (node1, node2) from the input file.
        	int node1 = fileScanner.nextInt();
        	int node2 = fileScanner.nextInt();
            // (b) Add node2 to the NodeList corresponding to node1 in adjacencyList.
        	adjacencyList[node1].addToHead(node2);
        }
        fileScanner.close();
        
        System.out.println("Graph built from " + graphFileName);
    }
    
    // This method returns the shortest path distance from startNode to all nodes 
    // in the graph using Breadth First Search.
    public int[] BFS_distance( int startNode ){
        
        // BFS_queue will be used for BFS traversal.
        // Use BFS_queue.add(...) and BFS_queue.remove() to enqueue and dequeue nodes.   
        LinkedList<Integer> BFS_queue = new LinkedList<Integer>();
        
        // Visited array keeps track of nodes that are visited: 
        //   visited nodes are nodes that have previously been added to the queue.
        boolean [] visited = new boolean[numNodes];
        
        // bfs_distances array stores the distance from startNode to all nodes in the graph. 
        int [] bfs_distances = new int[numNodes];
        // Initialize the distances to -1, this implies their distance is not yet known. 
        for( int i = 0; i < numNodes; i++ )    bfs_distances[i] = -1;
        
        // TASKS: perform BFS initialization here.
        // (a) Insert startNode into BFS_queue
        BFS_queue.add(startNode);
        // (b) Set bfs_distances for startNode: what is its distance value?
        bfs_distances[startNode] = 0;
        // (c) Set visited for startNode.
        visited[startNode] = true;
        
        // Loop until the BFS_queue is empty.
        while( !BFS_queue.isEmpty() ) {
            
            // TASK: get currentNode from BFS_queue.
        	int currentNode = BFS_queue.remove();
            
            // TASKS:
            //  For each 'unvisited' node V adjacent to currentNode do (a) - (c) below
            //  (you can use the NodeList iterator to get nodes adjacent to currentNode)
            //
        	for (Integer adjacentNode : adjacencyList[currentNode]) {
        		if (!visited[adjacentNode]) {
        			//     (a) add V to the BFS_queue
                	BFS_queue.add(adjacentNode);
                    //     (b) set the visited flag for V in visited array
                	visited[adjacentNode] = true;
                    //     (c) set distance value for V in bfs_distances array
                    //           HINT: you will need to use currentNode's distance.
                	bfs_distances[adjacentNode] = bfs_distances[currentNode] + 1;
                    //
        		}
        	} 
        }// End While.
        
        return bfs_distances;
    }
    
    // This method computes "diameter" of the input graph.
    // The diameter of the graph is the shortest path distance between farthest 
    // pair of nodes.
    // We get the graph diameter by running BFS starting from all nodes in the 
    // graph and computing the max distance value over all BFS traversals.
    void getGraphDiameter(){

        int diameterNode1 = 0, diameterNode2 = 0;
        int graphDiameter = -1;
        for( int i = 0; i < numNodes; i++ ){
            if( adjacencyList[i].getSize() == 0 )
                continue;
            int [] cur_bfs_distance = BFS_distance( i );
            for( int j = 0; j < numNodes; j++ ){
                if( cur_bfs_distance[j] > graphDiameter ){
                    diameterNode1 = i;
                    diameterNode2 = j;
                    graphDiameter = cur_bfs_distance[j];
                }
            }
        }
        System.out.println("Graph diameter = " + graphDiameter);
        System.out.println("Diameter vertices: ( " + 
                    diameterNode1 + ", " + diameterNode2 + " )");
    }
    
    int getNodeDegree(int node){
        // HOMEWORK TASK:
        //   return the degree for input node.   
        return adjacencyList[node].size;
    }
    
    float getClusteringCoefficient( int node ) {
        // HOMEWORK TASK:
        //   return the clusteringCoefficient for input node.
    	
    	float CC = 0;
    	
    	// Vi = original node
    	// Vj = neighbor of Vi
    	// Vk = possible neighbor of Vi
    	
    	// go through Vi's neighbors
        NodeList neighborsVi = adjacencyList[node];
        for (Integer Vj : neighborsVi) {      	
        	// go through the neighbors of Vj to see if Vj and Vk connected (have an edge between them)
        	for (Integer Vk : neighborsVi) {
        		if (areNeighbors(Vj, Vk))
    				CC += 1;
        	}
        }
        
        // edges are double-counted so divide by half
        CC /= (float) 2;
        
        return CC / (float) nChoose2(neighborsVi.size);
    }
    
    public static int nChoose2(int n) {
    	BigInteger answer = fact(n).divide(fact(n-2).multiply(new BigInteger("2")));
    	return answer.intValue();
	}

	public static BigInteger fact(int n) {
		if (n <= 1) {
			return(new BigInteger("1"));
	    } else {
			BigInteger bigN = new BigInteger(String.valueOf(n));
		    return(bigN.multiply(fact(n - 1)));
	    }
	}
    
    boolean areNeighbors(Integer node1, Integer node2) {
    	NodeList neighborsOfNode2 = adjacencyList[node2];
    	for (Integer currNode : neighborsOfNode2) {
    		if (currNode.intValue() == node1.intValue())
    			return true;
    	}
    	return false;
    }
    
    float getClosenessCentrality( int node ) {
        // HOMEWORK TASK:
        //   return the closeness centrality (CLC) for input node.
    	float avgBFS_distance = 0;
    	int countOfNodes = 0;
    	
    	int[] BFS_distances = BFS_distance(node);
    	
    	for (int i : BFS_distances) {
    		if (i > 0) {
    			avgBFS_distance += i;
    			countOfNodes++;
    		}
    	}
    	
    	avgBFS_distance /= countOfNodes;
    	
        return avgBFS_distance;
    }
}
 
class Node{
    int nodeId;
    Node nextNode;
    public Node(int newNodeId, Node newNext){
        nodeId = newNodeId;
        nextNode = newNext; 
    }
    public int getNodeId(){ return nodeId; }
    public Node getNext() { return nextNode; }
}
 
class NodeListIterator implements Iterator<Integer>{
    Node currentNode;
    public NodeListIterator(Node headNode){
        currentNode = headNode;
    }
    public boolean hasNext(){
        return (currentNode != null);
    }
    public Integer next(){ 
        Integer currentNodeId = currentNode.getNodeId();
        currentNode = currentNode.getNext();
        return currentNodeId; 
    }
    public void remove(){}
}
 
class NodeList implements Iterable<Integer>{
    int size;
    Node headNode;
    public NodeList(){
        size = 0;
        headNode = null;
    }
    public int getSize(){ return size; }
    public void addToHead(int addNodeId){
        headNode = new Node(addNodeId, headNode);
        size++;
    }
    public NodeListIterator iterator(){
        return new NodeListIterator(headNode);
    }
}