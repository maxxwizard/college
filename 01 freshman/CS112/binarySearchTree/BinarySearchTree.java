/*
 * Matthew Huynh (mhuynh)
 * CS112A1 - hw04
 * March 26, 2008
 * 
 * BinarySearchTree.java
 * Binary search tree class with ability to supply x and y coordinates
 * for external display on a displayWindow.
 */

import java.awt.*;
import java.util.LinkedList;

/**
 * Implements an unbalanced binary search tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class BinarySearchTree
{
    /**
     * Construct the tree.
     */
    public BinarySearchTree( )
    {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( Integer x )
    {
        root = insert( x, root );
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( Integer x )
    {
        root = remove( x, root );
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     * @throws Exception 
     */
    public Integer findMin( ) throws Exception
    {
		if( isEmpty( ) )
			throw new Exception( );
        return findMin( root ).element;
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     * @throws Exception 
     */
    public Integer findMax( ) throws Exception
    {
		if( isEmpty( ) )
			throw new Exception( );
        return findMax( root ).element;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if not found.
     */
    public boolean contains( Integer x )
    {
        return contains( x, root );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( )
    {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( )
    {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }

    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode insert( Integer x, BinaryNode t )
    {
        if( t == null )
            return new BinaryNode( x, null, null );
		
		int compareResult = x.compareTo( t.element );
			
        if( compareResult < 0 )
            t.left = insert( x, t.left );
        else if( compareResult > 0 )
            t.right = insert( x, t.right );
        else
            ;  // Duplicate; do nothing
        return t;
    }
    
    /**
     * Public method to insert multiple integer values into a subtree.
     * @param s a text string in JSON
     */
    public void BulkInsert(String s)
    {
    	// convert JSON string to array of ints
    	String[] temp = s.substring(1,s.length()-1).split(", ");
    	Integer[] values = new Integer[temp.length];
    	for (int i = 0; i < values.length; i++)
    	{
    		values[i] = new Integer(temp[i]);
    	}
    	
    	for (Integer x : values)
    	{
    		insert(x);
    	}
    	
    }

    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode remove( Integer x, BinaryNode t )
    {
        if( t == null )
            return t;   // Item not found; do nothing
			
		int compareResult = x.compareTo( t.element );
			
        if( compareResult < 0 )
            t.left = remove( x, t.left );
        else if( compareResult > 0 )
            t.right = remove( x, t.right );
        else if( t.left != null && t.right != null ) // Two children
        {
            t.element = findMin( t.right ).element;
            t.right = remove( (Integer) t.element, t.right );
        }
        else
            t = ( t.left != null ) ? t.left : t.right;
        return t;
    }
    
    /**
     * Public method to remove all the leaves from a BST.
     */
    public void removeLeafs()
    {
    	removeLeafs(root);
    }

    /**
     * Public method to remove all the leaves from a subtree.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
	public BinaryNode removeLeafs( BinaryNode t )
	{        

        // if node is a leaf, remove it
        if (t.left == null && t.right == null)
        {
        	t = null;
        	return null;
        }
        
        // if left exists, continue down tree
        if (t.left != null)
            t.left = removeLeafs(t.left);

        // if right exists, continue down tree
        if (t.right != null)
            t.right = removeLeafs(t.right);

        return t;
            
    }


    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the subtree.
     * @return node containing the smallest item.
     */
    private BinaryNode findMin( BinaryNode t )
    {
        if( t == null )
            return null;
        else if( t.left == null )
            return t;
        return findMin( t.left );
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the subtree.
     * @return node containing the largest item.
     */
    private BinaryNode findMax( BinaryNode t )
    {
        if( t != null )
            while( t.right != null )
                t = t.right;

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains( Integer x, BinaryNode t )
    {
        if( t == null )
            return false;
			
		int compareResult = x.compareTo( t.element );
			
        if( compareResult < 0 )
            return contains( x, t.left );
        else if( compareResult > 0 )
            return contains( x, t.right );
        else
            return true;    // Match
    }

    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the subtree.
     */
    private void printTree( BinaryNode t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.element );
            printTree( t.right );
        }
    }
    
    /**
     * Public method to assign X coordinates to each node in a tree.
     */
    private void assignX( )
    {
    	counter = 0;
    	computeX(root);
    }
    
    /**
     * Internal method to assign X coordinates to each node in a tree.
     * @param t the node that roots the subtree.
     */
    private void computeX( BinaryNode t )
    {
        if( t != null )
        {
            computeX( t.left );
            counter++;
            t.x = counter * 25;
            computeX( t.right );
        } 
    }
    
    public void printYCoordinates()
    {
    	printYCoordinates(root);
	}
    
    private void printYCoordinates( BinaryNode t )
    {
        if( t != null )
        {
        	printYCoordinates( t.left );
            System.out.println( t.y );
            printYCoordinates( t.right );
        }
    }
    
    /**
     * Public method to print all the nodes' X coordinates through inorder traversal.
     */
    public void printXCoordinates( )
    {
    	printXCoordinates(root);
    }
    
    /**
     * Internal method to print all the nodes' X coordinates through inorder traversal.
     * @param t the node that roots the subtree.
     */
    private void printXCoordinates( BinaryNode t )
    {
        if( t != null )
        {
        	printXCoordinates( t.left );
            System.out.println( t.x );
            printXCoordinates( t.right );
        }
    }
    
    /**
     * Public method to assign X coordinates to each node in a tree.
     */
    private void assignY( )
    {
    	computeY(root);
    }
    /**
     * Internal method to assign Y coordinates to each node in the tree (based on its depth).
     * @param t the current node.
     */
    private void computeY( BinaryNode t )
    {
        if( t != null )
        {
            computeY( t.left );
            t.y = 50 * computeDepth(t, root);
            computeY( t.right );
        } 
    }
    
    /**
     * Internal method to compute the depth of a node in the tree.
     * @param t the node to be found
     * @param current the current node
     */
    private int computeDepth( BinaryNode t, BinaryNode current)
    {
        if( current == null )
            return 0;
			
		int compareResult = t.element.compareTo( current.element );
			
        if( compareResult < 0 )
            return 1 + computeDepth( t, current.left );
        else if( compareResult > 0 )
            return 1 + computeDepth( t, current.right );
        else
            return 1;    // Match
    }
    
    void printAsText( )
    {
    	LinkedList<BinaryNode> q = new LinkedList<BinaryNode>();
    	
    	q.add(root);
    	
    	int level = height(root);
    	
    	// while stack is not empty
    	while (q.peek() != null)
    	{
    		
    		if (height(q.peek()) - level != 0) // if the current node isn't on the same height
    		{
    			System.out.println(); // new line
    			level--; // decrease level
    		}
    		
    		// print padding in front of number based on height of tree
    		printSpace(level*2);
    		
       		// remove first node in queue
    		BinaryNode temp = (BinaryNode) q.remove();

    		// print node
    		System.out.print(temp.element);

    		// print padding between numbers
    		printSpace(2);
    		
    		if (temp.left != null) // if there's a left child, add to queue
    		{
    			q.add(temp.left);
    		}
    		if (temp.right != null) // if there's a right child, add to queue
    		{
    			q.add(temp.right);
    		}
    		
    	}
    	
    	
    }
    
	private void printSpace (int length)
	{
		for ( int i = 0; i < length; i++ )
			System.out.print(" ");
	}

    /**
     * Internal method to compute height of a subtree.
     * @param t the node that roots the subtree.
     */
	@SuppressWarnings("unused")
	private int height( BinaryNode t )
    {
        if( t == null )
			return -1;
		else
			return 1 + Math.max( height( t.left ), height( t.right ) );	
    }
    
    public void displayTree()
    {
    	//call function to compute x coordinate for each node (proportional to inorder traversal number)
    	assignX();
        //call function to compute y coordinate of each node (proportional to depth)
    	assignY();
    	
    	displayWindow w = new displayWindow(this);
    	w.setVisible(true);
            //above code will create and make visible the display window
    }
    
    public void addGraphics(Graphics g)
	{    	
    	addGraphics(g, root, root.x, root.y);
	}
    
    private void addGraphics(Graphics g, BinaryNode t, int x, int y)
    {
    	if (t != null)
    	{
    		addGraphics(g, t.left, t.x, t.y);
    		drawNumberCircle(g, t.x, t.y, 30, t.element);
    		drawLine(g, t.x, t.y, x, y, 15);
    		addGraphics(g, t.right, t.x, t.y);
    	}
    }
    
    private void drawNumberCircle(Graphics g, int x, int y, int dia, int val)
    {
		g.setColor(Color.ORANGE);
		g.fillOval( x, y, dia, dia );
	
		g.setColor(Color.RED);
		g.setFont(new Font("Verdana",Font.BOLD,15));
		if (val >= 10)
			g.drawString( ""+val, x + dia/2 - 12, y + dia/2 +5 );
		else
			g.drawString( " "+val, x + dia/2 - 12, y + dia/2 +5 );
    }

    private void drawLine( Graphics g, int x1, int y1, int x2, int y2, int radius )
	{
		x1 = x1 + radius;
		y1 = y1 + radius;
		x2 = x2 + radius;
		y2 = y2 + radius;
		double dist = Math.sqrt(Math.pow((x1 - x2), 2)+Math.pow((y1 - y2), 2));
		double alpha1 = radius / dist;
		double alpha2 = (dist - radius) / dist;
		int xn1 = (int)(alpha1 * x1 + (1 - alpha1) * x2);
		int yn1 = (int)(alpha1 * y1 + (1 - alpha1) * y2);
		int xn2 = (int)(alpha2 * x1 + (1 - alpha2) * x2);
		int yn2 = (int)(alpha2 * y1 + (1 - alpha2) * y2);
		
		g.drawLine(xn1, yn1, xn2, yn2);
	}
	
    // Basic node stored in unbalanced binary search trees
    private static class BinaryNode
    {
            // Constructors
        BinaryNode( Integer theElement )
        {
            this( theElement, null, null );
        }

        BinaryNode( Integer theElement, BinaryNode lt, BinaryNode rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
        }

        Integer element;            // The data in the node
        BinaryNode left;   // Left child
        BinaryNode right;  // Right child
        int x;
        int y;
    }


      /** The tree root. */
    private BinaryNode root;
    private int counter = 0;
	
	
}
