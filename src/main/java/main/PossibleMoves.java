package main;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("mossaPossibile")
public class PossibleMoves {
    @Param(0)
    private int index;
    @Param(1)
    private int y;
    @Param(2)
    private int x;
    @Param(3)
    private int turn = 1;
    public PossibleMoves() {}

    public PossibleMoves(int index, int y, int x) {
        this.index = index;
        this.y = y;
        this.x = x;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
