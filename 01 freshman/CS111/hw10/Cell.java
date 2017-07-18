/*
 * Cell.java
 * 
 * Computer Science 111, Boston University
 * 
 * A class that serves as a blueprint for objects that represent
 * a cell in the game of Minesweeper.
 * 
 * This class will be the superclass of the classes that you will write
 * to represent the cells.  It contains the state and behavior common to
 * all types of cells.  In addition, it provides a basic implementation 
 * of behaviors that are different for different kinds of cells.
 * 
 * YOU SHOULD *NOT* CHANGE ANY CODE IN THIS CLASS.
 */

public class Cell {
    private Minesweeper game;        // the game to which this cell belongs
    private int row;                 // row index
    private int col;                 // column index 
    private boolean isMarked;        // is this cell marked?
    private boolean isRevealed;      // has this cell been revealed?
    private int numAdjMines;         // the number of mines in adjacent cells
    private boolean adjMinesCounted; // has numAdjMines already been counted?
    
    public Cell(Minesweeper game, int row, int col) {
        this.game = game;
        this.row = row;
        this.col = col;
        isMarked = false;
        isRevealed = false;
        
        // Note: numAdjMines is computed as needed the first time that
        // the user calls this cell's numAdjacentMines method.
        // We use adjMinesCounted to keep track of whether numAdjMines
        // has already been computed, so that we won't needlessly
        // compute it again.
        numAdjMines = 0;
        adjMinesCounted = false;
    }
    
    /*
     * getGame - get the Minesweeper object for the game 
     * to which this cell belongs.
     */
    public Minesweeper getGame() {
        return game;
    }
    
    /*
     * getRow - get this cell's row index.
     * The top row has an index of 0.
     */
    public int getRow() {
        return row;
    }
    
    /*
     * getColumn - get this cell's column index.
     * The leftmost column has an index of 0.
     */
    public int getColumn() {
        return col;
    }
    
    /*
     * isMarked - returns true if this cell is currently marked,
     * and false otherwise
     */
    public boolean isMarked() {
        return isMarked;
    }
    
    /*
     * isRevealed - returns true if this cell has been revealed,
     * and false otherwise
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /*
     * containsMine - returns true if this cell contains a mine,
     * and false otherwise
     */
    public boolean containsMine() {
        return false;
    }
    
    /*
     * mark - marks this cell
     */
    public void mark() {
        isMarked = true;
    }

    /*
     * unmark - unmarks this cell
     */
    public void unmark() {
        isMarked = false;
    }
    
    /*
     * reveal - reveals the contents of this cell
     */
    public void reveal() {
        if (isRevealed) {
            return;
        }
        
        isRevealed = true;      
        game.updateDisplay(this);
    }
    
    /*
     * isValid - a method that can be used to determine if the
     * specified indices represent a valid position in the mine field
     */
    public boolean isValid(int row, int col) {
        return (row >= 0 && row < game.numRows() 
                && col >= 0 && col < game.numColumns());
    }
    
    /*
     * numAdjacentMines - returns a count of the number of adjacent
     * cells (if any) that contain a mine.
     */
    public int numAdjacentMines() {
        // If this value has already been computed, just return it.
        if (adjMinesCounted) {
            return numAdjMines;
        }
        
        // Otherwise, examine the adjacent cells and count the number of mines.
        numAdjMines = 0;
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (isValid(r, c))  {
                    Cell neighbor = game.getCell(r, c);
                    if (neighbor.containsMine()) {
                        numAdjMines++;
                    }
                }
            }
        }
        
        adjMinesCounted = true;
        return numAdjMines;
    }
    
    /*
     * toString - returns a String of the form row,col containing
     * the row and column indices of this cell.
     */
    public String toString() {
        return row + "," + col;
    }
}