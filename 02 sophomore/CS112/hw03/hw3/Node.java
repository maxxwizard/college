package hw3;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw03
 * February 26, 2009
 * 
 * Node.java
 * This is a LinkedList Node class with one data item.
 * Code based on: http://cs-people.bu.edu/tvashwin/cs112_spring09/lab04_files/LinkedList.java
 * 
 * FOR USE AS PART OF A POSTFIX "CALCULATOR"
 */

class Node<T>{
	
	private T nodeData;
	private Node<T> nextNode;
	
	public Node( T newData, Node<T> newNextNode ){
		nodeData = newData;
		nextNode = newNextNode;
	}
	public T getData(){
		return nodeData;
	}
	public Node<T> getNextNode(){
		return nextNode;
	}
	public void setData(T newData){
		nodeData = newData;
	}
	public void setNextNode( Node<T> newNextNode ){
		nextNode = newNextNode;
	}
}