/*
 * MinesweeperGUI.java
 * 
 * Computer Science 111, Boston University
 * 
 * A class that serves as a blueprint for the object that will
 * serve as the GUI window of our Minesweeper game.
 * 
 * YOU SHOULD *NOT* CHANGE ANY CODE IN THIS CLASS.
 */

import javax.swing.*;
import java.awt.*;

public class MinesweeperGUI extends JFrame {  
    private int width;
    private int height;
    private int numRows;
    private int numCols;
    private JTextField messageArea;
    private CellButton[][] buttons;
    
    public MinesweeperGUI(int width, int height, Cell[][] cells, int numMines) {
        if (cells == null) {
            throw new IllegalArgumentException("parameter must be non-null");
        }
        this.width = width;
        this.height = height;
        numRows = cells.length;
        numCols = cells[0].length;
        
        UIManager.put("Button.margin", new Insets(0,0,0,0));
        try {
            UIManager.setLookAndFeel(
              UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // do nothing: stick with the default look and feel
        }
        setTitle("Minesweeper 111");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        
        String initMsg = "There are " + numMines + " mines to mark.";
        messageArea = new JTextField(initMsg);
        messageArea.setBackground(Color.WHITE);
        messageArea.setEditable(false);
        messageArea.setHorizontalAlignment(JTextField.CENTER);
        contentPane.add(messageArea, BorderLayout.NORTH);
        
        buttons = new CellButton[numRows][numCols];
        contentPane.add(createMinefield(cells), BorderLayout.CENTER);
    }
    
    /*
     * createMinefield - a private helper method that creates
     * the panel of buttons for the minefield.
     */
    private JPanel createMinefield(Cell[][] cells) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(numRows, numCols));
        
        int buttonWidth = width / numCols;
        int buttonHeight = height / numRows;
        
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                buttons[r][c] = new CellButton(cells[r][c],
                  buttonWidth, buttonHeight);
                panel.add(buttons[r][c]);    
            }
        }
    
        return panel;
    }
    
    /*
     * update - updates the state of the button at the position row, col.
     */
    public void update(int row, int col) {
        buttons[row][col].displayContents();
    }
    
    /*
     * reportWin - displays a message announcing a win and disables
     * all subsequent events.
     */
    public void reportWin() {   
        messageArea.setText("YOU WIN!!!");
        disableEvents();
    }
    
    /*
     * reportWin - displays a message announcing a loss and disables
     * all subsequent events.
     */
    public void reportLoss() {
        messageArea.setText("YOU LOSE :(");   
        disableEvents();
    }
    
    /*
     * disableEvents - a private helper method that disables the
     * ability of the buttons to receive events.
     */
    private void disableEvents() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                buttons[r][c].removeActionListener(buttons[r][c]);
            }
        }
    }
}