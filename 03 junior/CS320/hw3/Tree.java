
import java.util.*;

/**
 * @author Matthew Huynh
 * CS320 - Kfoury
 * Assignment #3
 */
public class Tree {

    public int label; // 0 for leaves, any other int for Nodes
    public Tree left;
    public Tree right;

    // equivalent to Leaf constructor
    public Tree()
    {
        this.label = 0;
        this.left = null;
        this.right = null;
    }

    // equivalent to Node constructor
    public Tree(int i, Tree left, Tree right) throws Exception
    {
        if (i == 0)
            throw new Exception("Node label must not be 0!");
        this.label = i;
        this.left = left;
        this.right = right;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // create a perfect non-degenerate tree with 0 nodes, 1 leaf, height 0
        Tree testTree = new Tree();
        // create a perfect degenerate tree with 1 node, 2 leaves, height 1
        Tree testTree1 = new Tree(1, new Tree(), new Tree());
        // create a imperfect degenerate tree with 2 nodes, 3 leaves, height 2
        Tree testTree2 = new Tree(1, new Tree(), new Tree(2, new Tree(), new Tree()));
        // create a perfect non-degenerate tree with 3 nodes, 4 leaves, height 2
        Tree testTree3 = new Tree(1, new Tree(2, new Tree(), new Tree()), new Tree(3, new Tree(), new Tree()));
        // create a imperfect degenerate tree with 3 nodes, 4 leaves, height 3
        Tree testTree4 = new Tree(1, new Tree(), new Tree(2, new Tree(), new Tree(3, new Tree(), new Tree())));
        // duplicate of testTree4 (used for equals testing)
        Tree testTree5 = new Tree(1, new Tree(), new Tree(2, new Tree(), new Tree(3, new Tree(), new Tree())));

        System.out.println("testTree  | leafCount: " + leafCount(testTree) + " | nodeCount: " + nodeCount(testTree) + " | height: " + height(testTree) + " | perfect: " + perfect(testTree) + "  | degenerate: " + degenerate(testTree) + " | list: " + list(testTree).toString());
        System.out.println("testTree1 | leafCount: " + leafCount(testTree1)+ " | nodeCount: " + nodeCount(testTree1) + " | height: " + height(testTree1) + " | perfect: " + perfect(testTree1) + "  | degenerate: " + degenerate(testTree1) + "  | list: " + list(testTree1).toString());
        System.out.println("testTree2 | leafCount: " + leafCount(testTree2)+ " | nodeCount: " + nodeCount(testTree2) + " | height: " + height(testTree2) + " | perfect: " + perfect(testTree2) + " | degenerate: " + degenerate(testTree2) + "  | list: " + list(testTree2).toString());
        System.out.println("testTree3 | leafCount: " + leafCount(testTree3)+ " | nodeCount: " + nodeCount(testTree3) + " | height: " + height(testTree3) + " | perfect: " + perfect(testTree3) + "  | degenerate: " + degenerate(testTree3) + " | list: " + list(testTree3).toString());
        System.out.println("testTree4 | leafCount: " + leafCount(testTree4)+ " | nodeCount: " + nodeCount(testTree4) + " | height: " + height(testTree4) + " | perfect: " + perfect(testTree4) + " | degenerate: " + degenerate(testTree4) + "  | list: " + list(testTree4).toString());
        System.out.println("testTree3 == testTree4: " + equals(testTree3, testTree4));
        System.out.println("testTree4 == testTree5: " + equals(testTree4, testTree5));
    }

    // @param m a tree to compare
    // @param n the other tree to compare
    public static boolean equals(Tree m, Tree n)
    {
        // if their traversals are the same, they are the same tree
        ArrayList<Integer> l1 = new ArrayList<Integer>(), l2 = new ArrayList<Integer>();
        inorder(m, l1);
        inorder(n, l2);
        return (l1.equals(l2));
    }

    // @param n the root node of the tree
    public static int leafCount(Tree n)
    {
        // check this isn't a dead pointer
        if (n != null)
        {
            // check left and right nodes for null (meaning it's a leaf)
            if (n.left == null && n.right == null)
                return 1;
            // recursively traverse the tree for more leaves
            else
                return leafCount(n.left) + leafCount(n.right);
        }

        return 0;
    }

    // @param n the root node of the tree
    public static int nodeCount(Tree n)
    {
        // check this isn't a dead pointer
        if (n != null)
        {
            // check left and right nodes for not null (meaning it's a node)
            if (n.left != null && n.right != null)
                return 1 + leafCount(n.left) + leafCount(n.right) - 2;
        }

        return 0;
    }

    // @param n the root node of the tree
    public static int height(Tree n)
    {
        if (n != null)
        {
            if (n.left != null && n.right != null)
                return 1 + Math.max(height(n.left), height(n.right));
            else if (n.left != null)
                return 1 + height(n.left);
            else if (n.right != null)
                return 1 + height(n.right);
        }

        return 0;
    }

    // @param n the root node of the tree
    public static boolean perfect(Tree n)
    {
        return (leafCount(n) + nodeCount(n)) == ((Math.pow(2,height(n)+1))-1);
    }

    // @param n the root node of the tree
    public static boolean degenerate(Tree n)
    {
        int nodeCount = nodeCount(n);

        return (nodeCount == 0) ? false : (nodeCount <= height(n));
    }

    // @param n the root node of the tree
    public static ArrayList<Integer> list(Tree n)
    {
        ArrayList<Integer> list = new ArrayList<Integer>();

        if (degenerate(n) == false)
            return list;
        else
            preorder(n, list);

        return list;
    }

    // helper method for list
    public static void preorder(Tree n, ArrayList<Integer> l)
    {
        if (n.label != 0)
            l.add(n.label);
        if (n.left != null)
            preorder(n.left, l);
        if (n.right != null)
            preorder(n.right, l);
    }

    // helper method for equals
    public static void inorder(Tree n, ArrayList<Integer> l)
    {
        if (n.left != null)
            inorder(n.left, l);
        l.add(n.label);
        if (n.right != null)
            inorder(n.right, l);
    }
}