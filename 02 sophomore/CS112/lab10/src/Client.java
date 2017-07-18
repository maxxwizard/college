package lab10;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*; 
 
public class Client extends javax.swing.JFrame implements 
	MouseInputListener, FocusListener
{
    Cartographer cartographer;
    CityGraph cityGraph;
    public Client() {
    	cartographer = new Cartographer(300);
    	cityGraph = new CityGraph(cartographer);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1240, 720);
        setTitle("Find a path between source and destination cities.");
        getContentPane().setBackground(Color.DARK_GRAY);
        addMouseListener(this); addFocusListener(this); 
        //addMouseMotionListener(this);
    }
    public void paint(Graphics g) {               
        super.paint(g);
        cityGraph.paint(g, this.getWidth(), this.getHeight());
    }
    public void mouseClicked(MouseEvent arg0) {
    	if( cityGraph.mouseClicked(arg0.getX(), arg0.getY()))
    		repaint();
	}
    public void mouseMoved(MouseEvent arg0) {
    	if( cityGraph.mouseOver(arg0.getX(), arg0.getY()))
    		repaint();
    }
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run() {
                new Client().setVisible(true);
            }
        });    
    }
    // Other mouse listener event handlers blank methods
	public void mouseEntered(MouseEvent arg0) {	}
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent arg0) { }
	public void mouseReleased(MouseEvent arg0) { }
	public void focusGained(FocusEvent arg0) { repaint(); }
	public void focusLost(FocusEvent arg0) { }
	public void mouseDragged(MouseEvent arg0) {	}
}