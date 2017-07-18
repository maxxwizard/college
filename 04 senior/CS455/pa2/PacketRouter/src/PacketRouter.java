import java.io.*;
import java.util.*;

/**
 * @author Matthew Huynh
 * 
 */
public class PacketRouter
{
	static final boolean DEBUG = false;
	static final boolean DEBUG_ROUTER_NOTIFICATIONS = false;
	static final boolean DEBUG_DISPLAY_SHORTEST_PATHS = false;
	static final boolean DEBUG_COMPUTE_FORWARDING_TABLE = false;
	static final boolean DEBUG_DIJKSTRA = false;
	static final boolean DEBUG_LINE_PARSED = false;
	static final int INFINITY = (int) Math.pow(2, 16);
	static Graph g;
	static HashMap<Integer, Integer> seqNumbers; /* <host address, max sequence seen */
	static DijkstraTable forwardingTable; /* gets re-computed */
	static boolean fwdTableDirty = false;
	static int routerAddress; /* this router's address */
	static ArrayList<MulticastSession> multicastSessions; /* router keeps track of all active multicast sessions */

	public static void main(String[] args)
	{
		if (args.length != 1) {
			System.err.println("usage: java PacketRouter <filename>");
			System.exit(1);
		}

		if (DEBUG_ROUTER_NOTIFICATIONS)
			System.out.println("ROUTER: Parsing trace file '" + args[0] + "'.");

		// Initialize a new graph and sequence number counters.
		g = new Graph();
		seqNumbers = new HashMap<Integer, Integer>();
		multicastSessions = new ArrayList<MulticastSession>();

		ParseTraceFile(new File(args[0]));

		if (DEBUG_ROUTER_NOTIFICATIONS)
			System.out.println("ROUTER: Trace file parsed.");
	}

	private static int ParseTraceFile(File f)
	{
		Event evt = null;
		Scanner sc = null;
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		while (sc.hasNextLine()) {
			String line = sc.nextLine(); // move to next line
			if (line.startsWith("#") || line.startsWith(" ") || line.length() == 0) {
				continue; // Skip blank lines and comments.
			} else {
				// Parse a real input line.
				evt = ParseEvent(line);
				if (evt != null) {
					// Process the event.
					ProcessEvent(evt);

					if (DEBUG_LINE_PARSED) {
						System.out.print("line parsed: ");
						DumpEvent(evt); // debugging
					}
				}
			}
		}

		// Close the file.
		sc.close();
		if (DEBUG_ROUTER_NOTIFICATIONS)
			System.out.println("ROUTER: File closed.");

		return 0;
	}

	private static void ProcessEvent(Event evt)
	{
		switch (evt.type)
		{
			case EVENT_JOIN:
				for (MulticastSession s : multicastSessions) {
					if (s.address == evt.multiaddr) {
						s.addMember(evt.unihost);
					}
				}
				break;
			case EVENT_QUIT:
				for (MulticastSession s : multicastSessions) {
					if (s.address == evt.multiaddr) {
						s.removeMember(evt.unihost);
					}
				}
				break;
			case EVENT_ADV:
				MulticastSession s = new MulticastSession(evt.multiaddr, evt.TTL, evt.unihost);
				multicastSessions.add(s);
				break;
			case EVENT_UFWD:
				// run Dijkstra's to update the forwarding table (set router as source)
				if (fwdTableDirty) {
					if (DEBUG_COMPUTE_FORWARDING_TABLE)
						System.out.println("ROUTER: re-computing unicast forwarding table with source = " + routerAddress);
					forwardingTable = ComputeForwardingTable(routerAddress);
					fwdTableDirty = false;
				}
				// get the first hop and output to log
				LinkedList<Integer> shortestPath = forwardingTable.getShortestPath(evt.unihost);
				if (DEBUG_DISPLAY_SHORTEST_PATHS)
					System.out.println("shortest path from " + routerAddress + " to " + evt.unihost + ": " + shortestPath);
				ForwardUnicastPacket(evt.timestamp, evt.unihost, shortestPath.getLast());
				break;
			case EVENT_MFWD:
				// delete any multicast session that has expired
				LinkedList<MulticastSession> toDelete = new LinkedList<MulticastSession>();
				for (MulticastSession sess : multicastSessions) {
					if (sess.expirationTime < evt.timestamp) {
						toDelete.add(sess);
						if (DEBUG)
							System.out.println("multicast session " + sess.address + " expired");
					}
				}
				multicastSessions.removeAll(toDelete);

				// check if requested multicast session exists
				MulticastSession matchingSession = null;
				for (MulticastSession sess : multicastSessions) {
					if (evt.multiaddr == sess.address) {
						matchingSession = sess;
					}
				}
				if (matchingSession == null) { // drop the packet since there's no active multicast session with requested address
					DropMulticastPacket(evt.timestamp, evt.multiaddr, evt.unihost);
					break;
				}

				// run Dijkstra's to update the forwarding table (set sender as source)
				if (DEBUG_COMPUTE_FORWARDING_TABLE)
					System.out.println("ROUTER: computing multicast forwarding table with source = " + evt.unihost);
				DijkstraTable MFWD_table = ComputeForwardingTable(evt.unihost);

				// forward the packet by getting the set of hosts that need to receive this packet by reverse-path forwarding
				TreeSet<Integer> listOfDestinations = new TreeSet<Integer>();
				for (Integer n : matchingSession.members) {
					if (n != evt.unihost) { // don't forward back to multicast packet sender
						LinkedList<Integer> path = MFWD_table.getShortestPath(n);
						if (path == null) {
							System.out.println("no path from " + evt.unihost + " to " + n + "?");
						} else {
							path.addFirst(Integer.valueOf(n)); // the last node in shortest path is the intended receiver

							if (DEBUG_DISPLAY_SHORTEST_PATHS)
								System.out.println("shortest path from " + evt.unihost + " to " + n + ": " + path);

							if (path.contains(Integer.valueOf(routerAddress))) {
								// traverse through path to find the router
								boolean routerTraversed = false;
								while (routerTraversed == false) {
									if (path.removeLast() == routerAddress)
										routerTraversed = true;
								}
								Integer d = null;
								try {
									while ((d = path.removeLast()).intValue() == routerAddress) { // continue traversing to find the first link after the router
										continue;
									}
								} catch (NoSuchElementException e) {
									// do nothing
								}
								if (d != null)
									listOfDestinations.add(d);
							}
						}
					}
				}
				// output to log
				if (listOfDestinations.size() > 0)
					ForwardMulticastPacket(evt.timestamp, evt.multiaddr, listOfDestinations);
				else {
					DropMulticastPacket(evt.timestamp, evt.multiaddr, evt.unihost);
					break;
				}

				break;
			case EVENT_INIT:
				// add info into the graph
				g.add(evt.unihost, evt.pairs);
				if (DEBUG_ROUTER_NOTIFICATIONS)
					System.out.println("ROUTER: address set to " + routerAddress);
				if (DEBUG)
					g.print();
				fwdTableDirty = true; /* mark the unicast forwarding table as dirty so we know to re-compute it */
				break;
			case EVENT_LSP:
				// check if this LSP is newer than the last received one before processing it
				Integer lastSeqNo = seqNumbers.get(evt.unihost);
				if (lastSeqNo == null || lastSeqNo < evt.seqno) {
					g.set(evt.unihost, evt.pairs); // process the LSP
					seqNumbers.put(evt.unihost, lastSeqNo); // update the max sequence number seen from this host
				}
				fwdTableDirty = true; /* mark the unicast forwarding table as dirty so we know to re-compute it */
				break;

			default:
				System.out.printf("ERROR: Invalid event parsed. \n");
		}
	}

	private static Event ParseEvent(String line)
	{
		Event e = new Event();
		// Break the line into timestamp and rest of line.
		String[] lineArr = line.split("(\\s+)", 2);
		// Set the timestamp.
		e.timestamp = Integer.parseInt(lineArr[0]);

		// If I, initialization.
		if (lineArr[1].startsWith("I")) {
			String[] tokens = lineArr[1].split("(\\s+)", 3);

			e.unihost = Integer.parseInt(tokens[1]);
			e.pairs = ParseLSPs(tokens[2]);
			e.type = EventType.EVENT_INIT;
			// set this router's address
			routerAddress = e.unihost;
		}
		// If L, link-state packet.
		else if (lineArr[1].startsWith("L")) {
			String[] tokens = lineArr[1].split("(\\s+)", 4);

			e.unihost = Integer.parseInt(tokens[1]);
			e.seqno = Integer.parseInt(tokens[2]);
			e.pairs = ParseLSPs(tokens[3]);
			e.type = EventType.EVENT_LSP;

		} else {
			String[] tokens = line.split("(\\s+)");
			// If F and token count of 4, multicast forwarding event.
			if (tokens[1].equals("F") && tokens.length == 4) {
				e.unihost = Integer.parseInt(tokens[2]);
				e.multiaddr = Integer.parseInt(tokens[3]);
				e.type = EventType.EVENT_MFWD;
				return e;
			}
			// If F and token count of 3, unicast forwarding event.
			else if (tokens[1].equals("F") && tokens.length == 3) {
				e.unihost = Integer.parseInt(tokens[2]);
				e.type = EventType.EVENT_UFWD;
				return e;
			}
			// If J and token count of 4, multicast join event.
			else if (tokens[1].equals("J") && tokens.length == 4) {
				e.unihost = Integer.parseInt(tokens[2]);
				e.multiaddr = Integer.parseInt(tokens[3]);
				e.type = EventType.EVENT_JOIN;
				return e;
			}
			// If Q and token count of 4, multicast quit event.
			else if (tokens[1].equals("Q") && tokens.length == 4) {
				e.unihost = Integer.parseInt(tokens[2]);
				e.multiaddr = Integer.parseInt(tokens[3]);
				e.type = EventType.EVENT_QUIT;
				return e;
			}
			// If A and token count of 5, multicast advertisement.
			else if (tokens[1].equals("A") && tokens.length == 5) {
				e.unihost = Integer.parseInt(tokens[2]);
				e.multiaddr = Integer.parseInt(tokens[3]);
				e.TTL = Integer.parseInt(tokens[4]);
				e.type = EventType.EVENT_ADV;
				return e;
			}
		}

		return e;
	}

	private static ArrayList<Node> ParseLSPs(String pairs)
	{
		Scanner s = new Scanner(pairs);
		String match = "";
		ArrayList<Node> linkStatePairs = new ArrayList<Node>();
		while ((match = s.findInLine("<((\\d*),(\\s*)(\\d*))>")) != null) {
			match = match.substring(1, match.length() - 1);
			String[] temp = match.split(",(\\s*)");
			Node p = new Node(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			linkStatePairs.add(p);
		}
		return linkStatePairs;
	}

	/**
	 * Using Dijkstra's algorithm, we can compute the shortest paths from a host to all other hosts.
	 * 
	 * based on the pseudo-code from "Computer Networking" textbook
	 */
	private static DijkstraTable ComputeForwardingTable(int hostAddress)
	{
		LinkedList<Node> neighborsOfRouter = g.adjacencyList.get(hostAddress);
		LinkedList<Integer> neighborsOfRouterAddresses = Graph.getAddresses(neighborsOfRouter);

		/*
		 * INITIALIZATION
		 */
		// N = all nodes
		HashSet<Node> dijkstraNodes = new HashSet<Node>();
		for (Integer i : g.adjacencyList.keySet()) {
			if (i != hostAddress)
				dijkstraNodes.add(new Node(i, INFINITY));
		}
		DijkstraTable table = new DijkstraTable(dijkstraNodes, hostAddress);
		// N' = {u}
		table.nodesSeen.add(hostAddress);
		// for all nodes v (in N)
		for (Node v : table.nodes) {
			// if v is a neighbor of u
			if (neighborsOfRouterAddresses.contains(v.address)) {
				// then D(v) = c(u,v)
				v.weight = g.getDistanceBetween(hostAddress, v.address);
				v.previous = hostAddress;// v.address;
			} else {
				// else D(v) = infinity
				if (v.address != hostAddress)
					v.weight = INFINITY;
			}
		}

		/*
		 * MAIN LOOP
		 */
		do {
			if (DEBUG_DIJKSTRA) {
				System.out.println("DIJKSTRA TABLE N: " + table.nodes);
				System.out.println("DIJKSTRA TABLE N': " + table.nodesSeen);
				System.out.println();
			}

			// find w not in N' such that D(w) is a minimum
			Node w = new Node(-1, INFINITY);
			for (Node candidate : table.nodes) {
				if (!table.seen(candidate.address)) {
					// if this node is minimum weight, remember it
					if (candidate.weight < w.weight) {
						w = candidate;
					}
				}
			}
			if (DEBUG_DIJKSTRA)
				System.out.println("w: " + w);

			if (w.weight != INFINITY) {
				// add w to N'
				table.nodesSeen.add(w.address);

				// for each neighbor v of w that is not in N':
				for (Node v : g.getNeighbors(w.address)) {
					if (!table.seen(v.address)) {
						int currDistance = table.getDistanceTo(v.address);
						int candidateDistance = w.weight + v.weight;
						if (currDistance == INFINITY || (candidateDistance < INFINITY && Math.min(currDistance, candidateDistance) == candidateDistance)) {
							// update D(v)
							table.setDistanceTo(v.address, candidateDistance, w.address);
							// System.out.println("D(" + v.address + ") now = " + candidateDistance + " via " + w.address);
						}
					}
				}
			}

			if (table.nodes.size() > 1000) {
				double percentage = 100 * ((double) table.nodesSeen.size()) / table.nodes.size();
				System.out.printf("computing forwarding table... N = %d, N' = %d (%.2f%%)\n", table.nodes.size(), table.nodesSeen.size(), percentage);
			}
		} while (table.nodesSeen.size() <= table.nodes.size());

		return table;

	}

	static void ForwardUnicastPacket(int time_now, int destination, int nexthop)
	{
		if (nexthop != -1)
			System.out.printf("FU %d %d %d\n", time_now, destination, nexthop);
		else
			System.out.printf("FU %d %d %s\n", time_now, destination, ", error: host not found in forwarding table!");
	}

	static void DropMulticastPacket(int time_now, int destAddr, int source)
	{
		System.out.printf("DMP %d %d %d\n", time_now, destAddr, source);
	}

	static void ForwardMulticastPacket(int time_now, int destAddr, TreeSet<Integer> ListOfNextHops)
	{
		/* Print the list of forwarded packets. */
		for (Integer n : ListOfNextHops) {
			System.out.printf("FMP %d %d %d\n", time_now, destAddr, n);
		}
	}

	private static void DumpEvent(Event evt)
	{
		switch (evt.type)
		{
			case EVENT_JOIN:
				System.out.printf("JOIN, id = %d, addr = %d\n", evt.unihost, evt.multiaddr);
				break;
			case EVENT_QUIT:
				System.out.printf("QUIT, id = %d, addr = %d\n", evt.unihost, evt.multiaddr);
				break;
			case EVENT_ADV:
				System.out.printf("ADV, id = %d, addr = %d, TTL = %d\n", evt.unihost, evt.multiaddr, evt.TTL);
				break;
			case EVENT_UFWD:
				System.out.printf("UFWD, host = %d\n", evt.unihost);
				break;
			case EVENT_MFWD:
				System.out.printf("MFWD, source = %d, addr = %d\n", evt.unihost, evt.multiaddr);
				break;
			case EVENT_INIT:
			case EVENT_LSP:
				if (evt.type == EventType.EVENT_INIT) {
					System.out.printf("INIT, id = %d, ", evt.unihost);
				} else {
					System.out.printf("LSP, source = %d, seqno = %d, ", evt.unihost, evt.seqno);
				}
				System.out.printf("PAIRS = ");
				for (int i = 0; i < evt.pairs.size(); i++) {
					System.out.printf("<%d, %d> ", evt.pairs.get(i).address, evt.pairs.get(i).weight);
				}
				System.out.printf("\n");
				break;

			default:
				System.out.printf("ERROR: Invalid event parsed. \n");
		}
	}

}
