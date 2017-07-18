package hw05;

/**
 * Matthew Huynh
 * CS112A1 - hw5
 * April 9, 2009
 *
 * WordList.java
 * A basic LinkedList data structure for holding Word objects.
 */

public class WordList {
	
	public Word head;
	
	public WordList()
	{
		head = null;
	}
	
	public boolean isEmpty()
	{
		return head == null;
	}

	public boolean contains(Word x)
	{
		if (head != null)
		{
			Word current = head;
			
	        while( current.next != null )
	        {
	            if (x.value.equals(current.value))
	            	return true;
	            current = current.next;
	        }
		}
        
		return false;
	}

	public void add(Word x)
	{
		if (head == null)
		{
			head = x;
			//System.out.println("Word " + x.value + " set as head");
		}
		else
		{
			Word current = head;
			
	        while ( current.next != null)
	        {
	        	//System.out.println("current is at " + current.value);
	        	current = current.next;
	        }
	        
	        current.next = x;
	        //System.out.println("Word " + x.value + " set as " + current.value + "'s next");
		}
	}

	public void clear() {
        head = null;
	}
	
}
