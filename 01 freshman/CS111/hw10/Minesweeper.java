/*
 * Minesweeper.java
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
 */

import javax.swing.*;
import java.util.*;

public class Minesweeper {
    // dimensions for the panel in which the mine field will be displayed
    public static final int WIDTH = 270;
    public static final int HEIGHT = 270;
    
    // dimensions of the grid of buttons for the cells
    public static final int NUM_COLS = 9;
    public static final int NUM_ROWS = 9;
    
    // the number of mines that will be placed on the mine field
    public static final int NUM_MINES = 10;
    
    // random-number generator
    private static Random rand;

    private static Cell[][] cells;           // the cells in the mine field
    private MinesweeperGUI display;   // the GUI display for the game
    
    public static void main(String[] args) {
        if (args.length > 0) {
            rand = new Random(Integer.parseInt(args[0]));
        } else {
            rand = new Random();
        }
        
        Minesweeper game = new Minesweeper();
        game.play();
   
    }

    public Minesweeper() {
        // Create the array of cells and fill it 
        // initially with empty cells.
        cells = new Cell[NUM_ROWS][NUM_COLS];
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                cells[r][c] = new EmptyCell(this, r, c);    
            }
        }
        
        plantMines();
    }

    /*
     * play - begin the play of the game
     */
    public void play() {
        display = new MinesweeperGUI(WIDTH, HEIGHT, cells, NUM_MINES);
        display.pack();
        display.setVisible(true);
        
        // That's it.  The rest of the game takes place
        // in response to button clicks.
    }
    
    /*
     * getCell - returns the object for the cell at position row,col
     * in the mine field.
     */
    public Cell getCell(int row, int col) {
        return cells[row][col];
    }
    
    /*
     * numRows - returns the number of rows in the mine field.
     */
    public int numRows() {
        return NUM_ROWS;
    }
    
    /*
     * numColumns - returns the number of columns in the mine field.
     */
    public int numColumns() {
        return NUM_COLS;
    }
    
    /*
     * updateDisplay - updates the display of the game to
     * reflect the changes to the cell passed in as a parameter.
     */
    public void updateDisplay(Cell changedCell) {
        int row = changedCell.getRow();
        int col = changedCell.getColumn();
        display.update(row, col);
    }
    
    /*
     * displayLoss - updates the cells and the display to reflect a loss.
     */
    public void displayLoss() {
        // Reveal all unrevealed mines.
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                Cell cell = cells[r][c];
                if (cell.containsMine() && !cell.isRevealed()) {
                    cell.reveal();
                }
            }
        }
        
        display.reportLoss();
    }
    
    /*
     * plantMines - a private helper method that puts NUM_MINES mines
     * in the mine field (i.e., in the cells array).
     */
    private void plantMines() {
    
        int minesPlanted = 0;
        
        while (minesPlanted < NUM_MINES) {
	    int randomRow = rand.nextInt(NUM_ROWS);
            int randomCol = rand.nextInt(NUM_COLS);
            
            if (cells[randomRow][randomCol].containsMine() == false) {
                cells[randomRow][randomCol] = new MinedCell(this, randomCol, randomRow);
                minesPlanted++;
            }
        }
        
    }
    
    /*
     * checkForWin - determines if the game has been won -- i.e.,
     * if NUM_MINES mines have been marked.
     * 
     * If the game has been won, this method asks the display
     * to report the win.
     */
    public void checkForWin() {
        int numMarkedMines = 0;
        
        for (int r = 0; r < NUM_ROWS; r++) {
           for (int c = 0; c < NUM_COLS; c++) {
               Cell cell = cells[r][c];
               if (cell.containsMine() && cell.isMarked()) {
                   numMarkedMines++;
               }
           }
        }   
        
        if (numMarkedMines == NUM_MINES) {
            display.reportWin();
        }
    }
}