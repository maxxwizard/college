package hw3;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw03
 * February 26, 2009
 * 
 * VList.java
 * This is a LinkedList class with added functionality for use as a data structure for Variables.
 * Code based on: http://cs-people.bu.edu/tvashwin/cs112_spring09/lab04_files/LinkedList.java
 * 
 * FOR USE AS PART OF A POSTFIX "CALCULATOR"
 */

import java.util.*;

public class VList<T> implements Iterable<T> {	
	
	private Node<T> headNode;
	
	// default constructor
	public VList(){
		headNode = null;
	}
	
	// Get an iterator for this list
	public ListIterator<T> iterator(){
		return new ListIterator<T>( headNode );
	}
	
	// The ListIterator class below implements an iterator for the 
	// LinkedList class 
	static class ListIterator<T> implements Iterator<T> {
		
		// Keeps track of the iterator's position in the list
		private Node<T> currentNode;
		
		// The iterator starts off at head of the linked list
		public ListIterator( Node<T> headNode ){
			currentNode = headNode;
		}
		// Are there more nodes ahead of us?
		public boolean hasNext(){
			return ( currentNode != null ); 
		}
		// Output current "node data" and walk to the next node
		public T next(){
			T currentData = currentNode.getData();
			currentNode = currentNode.getNextNode();
			return currentData;
		}
		// Output current "node" and walk to the next node
		public Node<T> nextNode(){
			Node<T> tempNode = currentNode;
			currentNode = currentNode.getNextNode();
			return tempNode;
		}
		// remove not implemented here but implemented in the 
		// LinkedList class above
		public void remove(){
		}
	}
	
	// Add a new node to tail of the list
	public void addToTail( T newNodeData ){
		// Locate the last node in the list so that we can 
		// add newNode after it.
		Node<T> lastNode = null;
		
		// Use the listIterator to walk through the list to find last node
		ListIterator<T> listIterator = this.iterator();
		while(listIterator.hasNext()){
			lastNode = listIterator.nextNode();
		}
		// Create a new node with newNodeData and set it as next node for lastNode
		// *** check whether lastNode is null and do appropriately 
		Node<T> newNode = new Node<T>(newNodeData, null);
		if( lastNode != null )
			lastNode.setNextNode(newNode);
		else
			headNode = newNode; 
	}
	
	// Iterates through the current list to check if a variable with name exists
	public boolean variableExist( char name ) {
		
		ListIterator<T> iter = this.iterator();
		
		while (iter.hasNext()) {
			Variable current = (Variable) iter.next();
			if (current.getName() == name)
				return true;
		}
		
		return false;
	}
	
	// Iterates through the current list to set a variable's value
	public void setVariableValue( char name, int value ) {
		
		ListIterator<T> iter = this.iterator();
		
		while (iter.hasNext()) {
			Variable current = (Variable) iter.next();
			if (current.getName() == name)
				current.setValue(value);
		}
		
	}
	
	// Iterates through the current list to get a variable's value
	public Integer getVariableValue( char name ) throws undefinedVariableException {
		
		ListIterator<T> iter = this.iterator();
		
		while (iter.hasNext()) {
			Variable current = (Variable) iter.next();
			if (current.getName() == name)
				return current.getValue();
		}
		
		throw new undefinedVariableException();
	}
	
}