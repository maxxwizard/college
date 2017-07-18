/**
 * @author Matthew Huynh
 * 
 * Graph is a HashMap, where the keys are nodes. Each value is an
 * adjacency list of the 
 */

import java.util.*;

public class Graph implements Cloneable
{

	static final int INFINITY = (int) Math.pow(2, 16);
	HashMap<Integer, LinkedList<Node>> adjacencyList;

	public Graph()
	{
		adjacencyList = new HashMap<Integer, LinkedList<Node>>();
	}

	/**
	 * Returns a deep copy of this Graph object.
	 */
	public Graph clone()
	{
		Graph g = new Graph();
		g.adjacencyList.putAll(adjacencyList);
		return g;
	}

	/**
	 * Creates a host and its adjacent edges.
	 */
	void add(Integer hostAddress, ArrayList<Node> adjacentNodes)
	{
		makeVertex(hostAddress);
		for (Node n : adjacentNodes) {
			// Create the node if it doesn't exist.
			makeVertex(n.address);
			// Create an edge between the two nodes.
			makeEdge(hostAddress, n.address, n.weight);
		}
	}

	/**
	 * Clears a host's adjacency list and rebuilds it.
	 */
	public void set(int hostAddress, ArrayList<Node> adjacentNodes)
	{
		LinkedList<Node> outgoingEdges = adjacencyList.get(hostAddress);
		if (outgoingEdges != null) {
			deleteEdges(hostAddress, outgoingEdges); // delete all its incoming edges
			outgoingEdges.clear(); // clear the host's adjacency list
		}
		add(hostAddress, adjacentNodes); // re-create this host's adjacency list
	}

	/**
	 * Deletes all of a host's edges.
	 */
	public void deleteEdges(int hostAddress, LinkedList<Node> outgoingEdges)
	{
		// for each outgoing edge from hostAddress
		for (Node n : outgoingEdges) {
			// get the adjacency list of the Node on the other side of the edge
			LinkedList<Node> oppositeList = adjacencyList.get(n.address);
			// delete the edge pointing back to the hostAddress
			LinkedList<Node> updatedList = new LinkedList<Node>();
			for (Node x : oppositeList)
			{
				if (x.address != hostAddress)
					updatedList.add(x);
			}
			adjacencyList.put(n.address, updatedList);
		}
	}

	/**
	 * Creates a Node by initializing an empty LinkedList and adding it to the graph. The method does nothing if the Node already exists.
	 */
	void makeVertex(Integer address)
	{
		LinkedList<Node> nList = adjacencyList.get(address);
		if (nList == null) {
			nList = new LinkedList<Node>();
			adjacencyList.put(address, nList);
		}
	}

	/**
	 * Creates an edge between two Nodes by adding (or updating) Nodes to each of their adjacency lists.
	 */
	void makeEdge(Integer address1, Integer address2, int weight)
	{
		LinkedList<Node> uList = adjacencyList.get(address1);
		LinkedList<Node> vList = adjacencyList.get(address2);
		boolean uExist = false, vExist = false;
		for (Node u : uList)
		{
			if (u.address == address2)
			{
				u.weight = weight;
				uExist = true;
			}
		}
		if (!uExist)
			uList.add(new Node(address2, weight));
		
		for (Node v : vList)
		{
			if (v.address == address1)
			{
				v.weight = weight;
				vExist = true;
			}
		}
		if (!vExist)
			vList.add(new Node(address1, weight));
	}

	/**
	 * Returns a LinkedList<Node> of a host's neighbors.
	 */
	LinkedList<Node> getNeighbors(Integer address)
	{
		return adjacencyList.get(address);
	}

	/**
	 * Given a LinkedList<Node>, this method returns a LinkedList<Integer> of the hosts' addresses (essentially drops the weight portion of Nodes).
	 */
	static LinkedList<Integer> getAddresses(LinkedList<Node> list)
	{
		LinkedList<Integer> addresses = new LinkedList<Integer>();
		for (Node n : list) {
			addresses.add(n.address);
		}
		return addresses;
	}

	public int getDistanceBetween(Integer u, Integer v)
	{
		for (Node x : adjacencyList.get(u)) {
			if (x.address == v) {
				return x.weight;
			}
		}
		return INFINITY;
	}

	public void print()
	{
		System.out.println();
		for (Integer address : adjacencyList.keySet()) {
			System.out.print(address + ": ");
			for (Node n : adjacencyList.get(address)) {
				System.out.print("<" + n.address + ", " + n.weight + "> ");
			}
			System.out.println();
		}
		System.out.println();
	}

}

class Node
{
	int address;
	int weight;
	Integer previous;

	public Node(int addr, int wght)
	{
		address = addr;
		weight = wght;
		previous = null;
	}

	public Node(int addr, int wght, Integer prev)
	{
		address = addr;
		weight = wght;
		previous = prev;
	}

	@Override
	public String toString()
	{
		return ("<" + this.address + ", " + this.weight + ", " + this.previous + ">");
	}

}