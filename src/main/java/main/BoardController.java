package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardController extends MouseAdapter {
    private Board board;
    private BoardPanel panel;

    public BoardController(Board board, BoardPanel panel) {
        this.board = board;
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        int x = e.getX();
        int y  = e.getY();
        for(int i = 0; i<board.getTABLE_HEIGHT();++i){
            for(int j = 0; j<board.getTABLE_WIDTH();++j){
                int coordY = i*panel.getCellDimension() + panel.getPaddingY();
                int coordX = j*panel.getCellDimension() + panel.getPaddingX();
                if(x<=coordX+27 && x>=coordX-27 && y<=coordY+27 && y>=coordY-27){
                    board.select(i, j);
                    panel.update();
                    return;
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public BoardPanel getPanel() {
        return panel;
    }

    public void setPanel(BoardPanel panel) {
        this.panel = panel;
    }
}
