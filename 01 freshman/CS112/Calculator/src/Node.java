public class Node {
		
	public int value;
	public char var;
	public Node next;
	
	public Node()
	{
		value = 0;
		var = 0;
		next = null;

	}
	
	public Node(int value, Node next)
	{
		this.value = value;
		this.next = next;
	}
	
	public Node(char var, int value, Node next)
	{
		this.var = var;
		this.value = value;
		this.next = next;
	}
	
	public void addToFront( Node newNode ) // method can be used on an subtype of LinkedListNode
	{
		newNode.next = this.next;         // make the first LinkedList Node equal to the next LinkedList Node to newNode
		this.next = newNode;              // make newNode the first LinkedList Node
	}
	

	
	public Node removeTail()
	{
		Node currentNode = this;
		
		if ( currentNode.next == null )                       
			return null;
		
		while( currentNode.next.next != null ) // traverse list while the Node two steps ahead is not null
		{
			currentNode = currentNode.next;    // keep the next currentNode
		}
		
		Node tailNode = currentNode.next;      // the tailNode is the last node before the null
		currentNode.next = null;
		
		return tailNode;
	}
	

	

		
}
