package hw3;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw03
 * February 26, 2009
 * 
 * CStack.java
 * This is a array-based stack with a size capacity of 100 Integers. 
 * Code based on: http://cs-people.bu.edu/tvashwin/cs112_spring09/lab04_files/LinkedList.java
 * 
 * FOR USE AS PART OF A POSTFIX "CALCULATOR"
 */

import java.util.*;

public class CStack {
	
	public static final int CAPACITY = 100;
	protected Integer[] S;
	protected int top = -1;
	
	public CStack() {
		S = new Integer[CAPACITY];
	}

	public int size()
	{
   		return (top + 1);
    }
	
	public boolean isEmpty() {
		return (top < 0);
	}
	  
	public void push(Integer element) {
		S[++top] = element;
	}

	public Integer top()
	{
		return S[top];
	}

    public Integer pop()
    {
    	Integer element;
        if (isEmpty())
        	throw new EmptyStackException();
        element = S[top];
        S[top--] = null;
        return element;
    }
    
    public void print() {
    	for (int i = top; i >= 0; i--) {
    		System.out.println("Stack[" + i + "]: " + S[i]);
    	}
    }
  
}
