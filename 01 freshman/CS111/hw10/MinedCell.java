/*
 * A class that represents an mined cell (a Cell with a mine).
 */
public class MinedCell extends Cell {
    
    /*
     * Constructor that uses the superclass's implementation.
     */
    public MinedCell(Minesweeper game, int row, int col) {
        super(game, row, col);
    }
    
    /*
     * Overrides Cell's implementation and returns true instead
     * of false because mined cells will contain a mine.
     */
    public boolean containsMine() {
        return true;
    }
    
    /*
     * Reveals the mined cell and ends the game in a loss.
     */
    public void reveal() {
        super.reveal();
        getGame().displayLoss();
    }
    
    /*
     * Marks the cell and checks if the player has won.
     */
    public void mark() {
        super.mark();
        super.getGame().checkForWin();
    }
    
    public String toString() {
        return "X";
    }
}