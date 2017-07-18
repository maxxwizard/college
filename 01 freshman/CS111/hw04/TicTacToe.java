/*
 * TicTacToe.java
 * 
 * name: Matthew Huynh
 * email: mhuynh@bu.edu
 * 
 * description: This program's main method will draw 3 yellow
 * tic-tac-toe boards filled with pre-defined configurations
 * of red X's and blue O's on a light-blue drawing panel.
 */

import java.awt.*;

public class TicTacToe {
    
    public static void main(String[] args) {
        // initializes the drawing panel and graphics brush
        DrawingPanel panel = new DrawingPanel(400, 325);
        Graphics g = panel.getGraphics();
        
        // sets the background color to light blue
        panel.setBackground(new Color(180,180,255));
        
        // draws the three tic-tac-toe boards with pre-defined configurations
        drawBoard(g,10,10,3,3,60,"X---O---X");
        drawBoard(g,50,200,5,5,24,"X-X--OO---XX--X---OXXO-XO");
        drawBoard(g,200,75,4,4,48,"----O-X--XOX--OO");
    }
    
    /**
     * DRAWS A TIC-TAC-TOE BOARD
     * 
     * - x and y are the coordinates of the upper left corner of the board
     * - rows and cols are how many rows and columns the board has
     * - size is how big each small square that comprise the entire board is
     * - boardConfig is a string that will be parsed to draw the X's and O's
     */
    public static void drawBoard(Graphics g, int x, int y, int rows, int cols, int size, String boardConfig) {
        
        // draws the individual squares row by row
        drawGrid(g, x, y, rows, cols, size);
        
        // draws the X's and O's
        drawBoardConfig(g, x, y, rows, cols, size, boardConfig);
     }
    
    /**
     * DRAWS A TWO-DIMENSIONAL GRID OF YELLOW SQUARES WITH BLACK BORDERS
     * 
     * - grid upper-left corner coordinate: (x, y)
     * - grid dimensions: rows x cols
     * - individual square size: size
     */
    public static void drawGrid(Graphics g, int x, int y, int rows, int cols, int size) {
        
        int tempX = x;
        int tempY = y;
        for (int i = 0; i < rows; i++) {
            tempX = x;
            for (int j = 0; j < cols; j++) {
                // draw yellow square
                g.setColor(Color.YELLOW);
                g.fillRect(tempX, tempY, size, size);
                // draw square's black border
                g.setColor(Color.BLACK);
                g.drawRect(tempX, tempY, size, size);
                
                // move to next square in the row
                tempX = tempX + size;
            }
            // move to the first square in the next row
            tempY = tempY + size;
        }
    }
    
    /**
     * DRAWS THE X'S AND O'S ON A BOARD
     * 
     * - the string boardConfig is parsed by this algorithm:
     *   "-" equates to a blank square
     *   "X" equates to an X
     *   "O" equates to an O
     */
    public static void drawBoardConfig(Graphics g, int x, int y, int rows, int cols, int size, String boardConfig) {
        
        // if boardConfig doesn't works for grid dimensions, throw an exception
        if (boardConfig.length() != (rows*cols))
            throw new IllegalArgumentException("Please make sure your string length matches your grid size.");

        // parse boardConfig and draws the X's and O's
        int currentSquare = 0;
        int tempX = x;
        int tempY = y;
        for (int i = 0; i < rows; i++) {
            tempX = x;
            for (int j = 0; j < cols; j++) {
                // if character is O, then draw a O
                if (boardConfig.charAt(currentSquare) == 'O') {
                    g.setColor(Color.BLUE);
                    g.fillOval(tempX+size/6,tempY+size/6,2*(size/3),2*(size/3));
                    g.setColor(Color.YELLOW);
                    g.fillOval(tempX+size/3,tempY+size/3,size/3,size/3);
                }
                // if character is X, then draw an X
                if (boardConfig.charAt(currentSquare) == 'X') {
                    g.setColor(Color.RED);
                    fillQuad(g,tempX+size/6,tempY+size/6,tempX+size/3,tempY+size/6,tempX+5*(size/6),tempY+5*(size/6),tempX+2*(size/3),tempY+5*(size/6));
                    fillQuad(g,tempX+2*(size/3),tempY+size/6,tempX+5*(size/6),tempY+size/6,tempX+size/3,tempY+5*(size/6),tempX+size/6,tempY+5*(size/6));
                }
                // if character is -, then do nothing
                
                // move to next square in the row
                tempX = tempX + size;
                
                // move the position of the parsing location
                currentSquare++;
            }
            // move to the first square in the next row
            tempY = tempY + size;
        }
    }
    
    /**
     * fillQuad - creates a filled quadrilateral using the
     * Graphics object g.  The corners of the quadrilateral
     * are specified by the other 8 parameters.
     * 
     * You will use this method when drawing an X.
     * 
     * IMPORTANT: The corners must be specified in an order
     * that corresponds to starting at one corner and walking
     * around the perimeter in either direction.
     */
    public static void fillQuad(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        Polygon p = new Polygon();
        p.addPoint(x1, y1);
        p.addPoint(x2, y2);
        p.addPoint(x3, y3);
        p.addPoint(x4, y4);
        g.fillPolygon(p);
    }   
}