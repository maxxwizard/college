/*
 * CellButton.java
 * 
 * Computer Science 111, Boston University
 * 
 * A class that serves as a blueprint for buttons that represent
 * a cell in the GUI for a game of Minesweeper.
 * 
 * YOU SHOULD *NOT* CHANGE ANY CODE IN THIS CLASS.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CellButton extends JButton implements ActionListener {
    // The Cell associated with this button.
    private Cell cell;
    
    public CellButton(Cell cell, int width, int height) {
        this.cell = cell;
        setPreferredSize(new Dimension(width, height));
        addActionListener(this);
    }
    
    /*
     * getCell - returns the Cell object associated with this button.
     * Note that the returned object may have an actual type that
     * is a subclass of Cell.
     */
    public Cell getCell() {
        return cell;
    }
    
    /*
     * shiftPressed - a private helper method that returns true
     * if the Shift key was pressed when the button-click event 
     * specified by the parameter occurred, and false otherwise.
     */
    private boolean shiftPressed(ActionEvent event) {    
        return (event.getModifiers() & ActionEvent.SHIFT_MASK) != 0;
    }
    
    /*
     * actionPerformed - the event-handler method for button-click
     * events associated with this button.  It is called when the
     * button is clicked by the user.
     */
    public void actionPerformed(ActionEvent event) {
        // If the corresponding cell has already been revealed,
        // this button doesn't respond to any subsequent button clicks.
        if (cell.isRevealed()) {
            return;
        }
        
        if (shiftPressed(event)) {
            // The Shift key was pressed, so mark or unmark the cell.
            if (cell.isMarked()) {
                setForeground(Color.BLACK);
                setText(null);
                cell.unmark();
            } else {
                setForeground(Color.RED);
                setText("V");
                cell.mark();
            }
        } else if (!cell.isMarked()) {
            cell.reveal();
        }
    }
    
    /*
     * displayContents - display the contents of the cell when it is revealed.
     * This method is called by the update method in MinesweeperGUI.
     */
    public void displayContents() {
        if (cell.isMarked() || !cell.isRevealed()) {
            return;
        }
        
        // Change the background color, based on whether the
        // cell contains a mine.
        if (cell.containsMine()) {
            setBackground(Color.RED);
        } else {
            setBackground(Color.LIGHT_GRAY);
        }
        
        // Display the text associated with this cell,
        // which we obtain using its toString method.
        setText(cell.toString());
    }
}
