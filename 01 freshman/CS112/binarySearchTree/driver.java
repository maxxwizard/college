/*
 * Matthew Huynh (mhuynh)
 * CS112A1 - hw04
 * March 26, 2008
 * 
 * driver.java
 * Driver that repeatedly queries the user for a JSON string corresponding
 * to a sequence of insertions, creates a BST tree resulting from that sequence
 * of insertions, removes the leafs, and launches a window that displays the tree.
 */

import javax.swing.JOptionPane;

public class driver {

    public static void main( String [ ] args ) throws Exception
    {
    	while (true)
    	{
    		String str = JOptionPane.showInputDialog(null, 
    				"Enter JSON string : ",
    				"Enter JSON string", 1);
    		
    		if (str == null || str.equals("") || str.equals(" "))
    		{
    			break;
    		}
    		else
    		{
	    		BinarySearchTree t = new BinarySearchTree();
	    		t.BulkInsert(str);
	    		t.removeLeafs();
	    		t.displayTree();
	    		t.printAsText();
    		}
    		
    	}
    	
    	// test string: [10, 5, 2, 7, 1, 3, 6, 8, 15, 12, 17, 11, 13, 16, 18]
    }  

}
