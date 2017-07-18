import java.util.*;

/**
 * An event consists of many fields (not all used by all events):
 *    - an integer timestamp
 *    - a  type
 *    - a  host ID
 *    - a  sequence number 
 *    - an array of lsp_pairs 
 *    - a  multicast address  
 *    - a  time-to-live field
 */
public class Event {
	int timestamp;
	EventType type;
	int	unihost;
	int	seqno;
	ArrayList<Node> pairs;
	int	multiaddr;
	int TTL;
	
	public Event()
	{
		
	}
}
