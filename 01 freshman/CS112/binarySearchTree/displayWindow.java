/*
 * Matthew Huynh (mhuynh)
 * CS112A1 - hw04
 * March 26, 2008
 * 
 * displayWindow.java
 * The Java Swing display window that acts as the canvas for drawing
 * the binary search tree on.
 */

import java.awt.Graphics;

public class displayWindow extends javax.swing.JFrame
{	
	
	private static final long serialVersionUID = 1L;
	
	BinarySearchTree tree;
	//tree that it is displaying
	
	public displayWindow(BinarySearchTree tree)
	{
		this.tree = tree;
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            //closing this window will terminate the program that launched it, but it is okay to have multiple
            //windows open
		setSize(500,1000);
		setTitle("DisplayWindow");
	}

	public void paint(Graphics g)
	{                
		super.paint(g);
		tree.addGraphics(g);
		//you will need to implement the addGraphics function in your BST class
		
	}

}