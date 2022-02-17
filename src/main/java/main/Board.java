package main;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int TABLE_HEIGHT = 5;
    private int TABLE_WIDTH = 9;
    private Piece[][] cells = new Piece[TABLE_HEIGHT][TABLE_WIDTH];
    private List<Pair<Integer,Integer>> currentPossibleMoves = new ArrayList<>(8);
    private void init(){
        for (int i = 0; i < TABLE_HEIGHT / 2; ++i) {
            for (int j = 0; j < TABLE_WIDTH; ++j) {
                cells[i][j] = new Piece(i,j, Settings.PLAYER_2);
                cells[TABLE_HEIGHT - 1 - i][j] = new Piece(TABLE_HEIGHT - 1 - i,j, Settings.PLAYER_1);
            }
        }
        int alternation = Settings.PLAYER_2;
        for (int j = 0; j < TABLE_WIDTH; ++j) {
            if (j != TABLE_WIDTH / 2) {
                cells[TABLE_HEIGHT / 2][j] = new Piece(TABLE_HEIGHT / 2,j,alternation);
                alternation = alternation == Settings.PLAYER_1 ? Settings.PLAYER_2 : Settings.PLAYER_1;
            }
        }
    }
    public Board(){
        init();
    }
    public void print() {
        for (int i = 0; i < TABLE_HEIGHT; ++i) {
            for (int j = 0; j < TABLE_WIDTH; ++j) {
                if(cells[i][j] != null){
                    System.out.print(cells[i][j].getType());
                }
                else System.out.print(0);
            }
            System.out.println();
        }
    }

    public int getTABLE_HEIGHT() {
        return TABLE_HEIGHT;
    }

    public void setTABLE_HEIGHT(int TABLE_HEIGHT) {
        this.TABLE_HEIGHT = TABLE_HEIGHT;
    }

    public int getTABLE_WIDTH() {
        return TABLE_WIDTH;
    }

    public void setTABLE_WIDTH(int TABLE_WIDTH) {
        this.TABLE_WIDTH = TABLE_WIDTH;
    }

    public Piece[][] getCells() {
        return cells;
    }
    private void addPossibleMoves(int i,int j){

    }
    public void setCells(Piece[][] cells) {
        this.cells = cells;
    }
    public void select(int x, int y){
        for(int i = 0; i < TABLE_HEIGHT;i++){
            for(int j = 0; j < TABLE_WIDTH;j++){
               if(cells[i][j] != null){
                    if(i == x && j == y) {
                        boolean selected = cells[i][j].isSelected() ? false : true;
                        cells[i][j].setSelected(selected);
                        if(selected) addPossibleMoves(i,j);
                    }
                   else{
                        cells[i][j].setSelected(false);
                   }
                }
            }
        }
    }
}
