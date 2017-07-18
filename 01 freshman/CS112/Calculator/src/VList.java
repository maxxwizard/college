public class VList {
	
	public Node head;
	
	public VList()
	{
		head = null;
	}
	
	public boolean isEmpty()
	{
		return head == null;
	}
	
	public Node removeHead()
	{

		Node temp = head;
		head = head.next;
		temp.next = null;
		return temp;
	}
	
	/*
	public Node removeHead()
	{
		Node head = this.next;       // keep the first LinkedList Node in firstNode
		
		if (this.next == null)			// if second node is null, there is only one item, the head
			return this;				// so return the head
		
		// break head off
		newFirstNode = null;
		
		if ( newFirstNode != null )          // if head isn't null i.e. if stack isn't empty
			this.next = newFirstNode.next;   // set the second or next LinkedList Node to the first LinkedList Node
		
		return newFirstNode;
	}
	*/
	
}
