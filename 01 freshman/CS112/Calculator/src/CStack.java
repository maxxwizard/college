import java.util.*;

public class CStack {
	
	public VList stack;

	public CStack()
	{
		stack = new VList();
	}

	public boolean isEmpty()
	{
   		return stack.isEmpty();
    }
	  
	public void push(char var, int value)
	{
		if (stack.isEmpty())
		{
			stack.head = new Node(var, value, null);
		}
		else
		{
			Node oldHead = stack.head;					// retain old head
			stack.head = new Node(var, value, null);	// create new head
			stack.head.next = oldHead;					// set new head's next to old head
		}
	}

	public void push(int v)
	{
		System.out.println("pushing " + v + " onto stack");
		if (stack.isEmpty())
		{
			stack.head = new Node(v, null);
		}
		else
		{
			Node oldHead = stack.head;					// retain old head
			stack.head = new Node(v, null);			// create new head
			stack.head.next = oldHead;					// set new head's next to old head
		}
	}

    public Node pop()
    {
        if (stack.head == null)
        {
            throw new NoSuchElementException(); 
        } else {
            return stack.removeHead();
        }
    }
    
	public int getValue(char var) throws undefinedVariableException
	{
		Node walker = stack.head;
				
		do
		{
			if (walker.var == var)
			{
				return walker.value;
			}
			else
			{
				walker = walker.next;
			}
			
			if (walker == null)
				break;
		} while (walker != null);
		
		throw new undefinedVariableException();
	}
	
	public boolean variableExist(char var)
	{
		Node walker = stack.head;
		
		if (walker == null)
			return false;
		
		do
		{
			if (walker.var == var)
			{
				return true;
			}
			else
			{
				walker = walker.next;
			}
			
			if (walker == null)
				break;
		} while (walker != null);
		
		return false;
	}
	
	public void print()
	{
		System.out.println();
		
		if (stack.isEmpty())
		{
			System.out.println("stack is empty");
			return;
		}
		
		Node walker = stack.head;
		
		System.out.println("stack");
		
		// print nodes
		while (walker.next != null)
		{
			System.out.println("var " + walker.var + ": " + walker.value + " pointing to " + walker.next.var);
			walker = walker.next;
		}
		
		// print last node
		System.out.println("var " + walker.var + ": " + walker.value+ " pointing to " + walker.next);
	}

	public void replace(char var, int result) {
		
		Node walker = stack.head;
		
		do
		{
			if (walker.var == var)
			{
				walker.value = result;
			}
			else
			{
				walker = walker.next;
			}
			
			if (walker == null)
				break;
		} while (walker != null);
		
	}

}
