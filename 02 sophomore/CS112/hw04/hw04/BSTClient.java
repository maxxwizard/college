/*
 * Matthew Huynh (mhuynh)
 * CS112A1 - hw04
 * March 27, 2009
 * 
 * BSTClient.java
 * Java Swing client that prompts user for a JSON string and paints
 * the nodes into a graphical tree.
 */

package hw04;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class BSTClient extends javax.swing.JFrame implements MouseListener, FocusListener
{
    BinarySearchTree bst;
    
    public BSTClient()
    {
        bst = new BinarySearchTree();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1021, 521);
        setTitle("Binary Search Tree display");
        addMouseListener(this);
        addFocusListener(this);
    }
    
    public void paint(Graphics g)
    {               
        //super.paint(g);
        //bst.displayDataArray(g, this.getWidth(), this.getHeight());
        
        //bst.addGraphicsLabVersion(g, this.getWidth(), this.getHeight());
        //setTitle("Binary Search Tree display -- LAB Version");
        
        //< Uncomment following lines after completing code for HW version > 
        // Also comment the previous pair of lines corresponding to the lab version.  
        
        bst.addGraphics(g);
        setTitle("Binary Search Tree display -- HOME WORK version");
    }
    
    public void mouseClicked(MouseEvent arg0) {
		String JSON_str = JOptionPane.showInputDialog(null, 
								"Enter JSON string : ",
								"Enter JSON string", 1);
		bst = new BinarySearchTree();
		bst.bulkInsert(JSON_str);
		bst.printAsText();
		bst.displayTree();
		repaint();
		
	}
    
    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new BSTClient().setVisible(true);
                // test string #1: [10, 5, 2, 7, 1, 3, 6, 8, 15, 12, 17, 11, 13, 16, 18]
                // test string #2: [12, 15, 76, 23, 99]
            }
        });    
    }
    
    // Other mouse listener event handlers blank methods
	public void mouseEntered(MouseEvent arg0) {	}
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent arg0) { }
	public void mouseReleased(MouseEvent arg0) { }
	public void focusGained(FocusEvent arg0) {
		repaint();
	}
	public void focusLost(FocusEvent arg0) { }   
}
