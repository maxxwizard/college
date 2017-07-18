import java.util.*;

public class DijkstraTable
{
	static final int INFINITY = (int) Math.pow(2, 16);
	HashSet<Node> nodes; /* each node is in format <v, D(v), p(v)> */
	HashSet<Integer> nodesSeen; /* N' */
	int source; /* the forwarding table contains shortest paths from this source */

	public DijkstraTable(HashSet<Node> nodes, int source)
	{
		this.nodes = nodes;
		this.nodesSeen = new HashSet<Integer>();
		this.source = source;
	}

	/**
	 * If we've seen addr (i.e. addr is in N'), method returns true.
	 */
	public boolean seen(Integer addr)
	{
		for (Integer n : nodesSeen) {
			if (n == addr.intValue())
				return true;
		}

		return false; // address not found in N'
	}

	public int getDistanceTo(Integer addr)
	{
		for (Node n : nodes) {
			if (n.address == addr.intValue())
				return n.weight;
		}
		return INFINITY; // error
	}

	public boolean setDistanceTo(Integer addr, int distance, int nextHop)
	{
		for (Node n : nodes) {
			if (n.address == addr.intValue()) {
				n.weight = distance;
				n.previous = nextHop;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the node with the parameterized address.
	 */
	public Node get(int addr)
	{
		for (Node n : nodes) {
			if (n.address == addr)
				return n;
		}
		return null; // node not found
	}

	/**
	 * Computes and returns a reverse-ordered shortest path (last element is first hop, second to last is second hop, etc.) between two senders.
	 */
	public LinkedList<Integer> getShortestPath(int destHost)
	{
		LinkedList<Integer> path = new LinkedList<Integer>();
		for (Node n = get(destHost); n != null && n.address != source; n = get(n.previous))
			path.add(n.address);
		
		return path;
	}
}
