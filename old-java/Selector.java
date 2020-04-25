package gungi;

public class Selector {

    protected enum Selected { board, white_reserve, black_reserve, none };
    private Selected selected;
    private int[] cell, prevCell;
    private GungiPiece selection;
    
    public Selector() {
        cell = new int[2];
    }
    
    public GungiPiece getSelection() {
        return selection;
    }
    
    public void setSelection(GungiPiece selection) {
        this.selection = selection;
    }
    
    public int getIndex() {
        return 9 * cell[1] + cell[0];
    }
    
    public Selected getSelected() {
        return selected;
    }
    
    public int[] getSelectedCell() {
        return cell;
    }
    
    public int[] getPrevSelectedCell() {
        return prevCell;
    }
    
    public int getCellX() {
        return cell[0];
    }
    
    public int getCellY() {
        return cell[1];
    }
    
    public void setSelectedCell(Selected selected, int[] cell) {
        if (this.selected == Selected.board && selected == this.selected) 
            prevCell = this.cell;
        else {
            prevCell = null;
            this.selected = selected;
        }
        
        this.cell = cell;
    }
    
    public void deselect() {
        this.selected = Selected.none;
        this.selection = null;
        this.prevCell = null;
    }
}
