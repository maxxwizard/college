/*
 * A class that represents an empty cell (a Cell without a mine).
 */
public class EmptyCell extends Cell {
    
    /*
     * Constructor that uses the superclass's implementation.
     */
    public EmptyCell(Minesweeper game, int row, int col) {
        super(game, row, col);
    }
    
    /*
     * Reveals the empty cell and all nearby empty cells.
     */
    public void reveal() {
        
        // reveal the cell
        super.reveal();
        
        // if cell has 0 mines
        if (super.numAdjacentMines() == 0) {
            // position at top-left of 3x3 cell array
            for (int r = super.getRow() - 1; r <= super.getRow() + 1; r++) {
                for (int c = super.getColumn() - 1; c <= super.getColumn() + 1; c++) {
                    // if cell is valid
                    if (isValid(r, c))  {
                        // retrieve it
                        Cell neighbor = super.getGame().getCell(r, c);
                        // if cell doesn't contain a mine, isn't revealed, and isn't marked
                        if (!neighbor.containsMine() && !neighbor.isRevealed() && !neighbor.isMarked()) {
                            // pass it on
                            neighbor.reveal();
                        }
                    }
                }
            }
        }
        
    }
    
    /*
     * Returns the number of mines adjacent to the cell.
     */
    public String toString() {
        Integer numAdjMines = super.numAdjacentMines();
        if (numAdjMines.equals(0))
                return "";
        return numAdjMines.toString();
    }
    
}