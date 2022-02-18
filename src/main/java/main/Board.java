package main;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private int TABLE_HEIGHT = 5;
    private int TABLE_WIDTH = 9;
    private Piece[][] cells = new Piece[TABLE_HEIGHT][TABLE_WIDTH];
    private List<Piece> selectable = new ArrayList<>();
    private Piece currentSelected = null;
    private List<Pair<Integer, Integer>> possibleMoves = new ArrayList<>(8);
    private boolean choosingWhatToEat = false;
    private Map<Pair<Integer,Integer>,List<Piece>> choose = new HashMap<>();
    private int currentPlayer = Settings.PLAYER_1;
    private void init() {
        for (int i = 0; i < TABLE_HEIGHT / 2; ++i) {
            for (int j = 0; j < TABLE_WIDTH; ++j) {
                cells[i][j] = new Piece(i, j, Settings.PLAYER_2);
                cells[TABLE_HEIGHT - 1 - i][j] = new Piece(TABLE_HEIGHT - 1 - i, j, Settings.PLAYER_1);
            }
        }
        int alternation = Settings.PLAYER_2;
        for (int j = 0; j < TABLE_WIDTH; ++j) {
            if (j != TABLE_WIDTH / 2) {
                cells[TABLE_HEIGHT / 2][j] = new Piece(TABLE_HEIGHT / 2, j, alternation);
                alternation = alternation == Settings.PLAYER_1 ? Settings.PLAYER_2 : Settings.PLAYER_1;
            }
        }
        fillSelectable();
        cells[4][6] = null;
        cells[2][3] = null;
    }

    public Board() {
        init();
    }

    public void print() {
        for (int i = 0; i < TABLE_HEIGHT; ++i) {
            for (int j = 0; j < TABLE_WIDTH; ++j) {
                if (cells[i][j] != null) {
                    System.out.print(cells[i][j].getType());
                } else System.out.print(0);
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

    private boolean validPosition(int i, int j) {
        return i >= 0 && i < TABLE_HEIGHT && j >= 0 && j < TABLE_WIDTH;
    }

    private boolean eatSomething(int x, int y, int x1, int y1) {
        boolean eat = false;
        int xdir = x1 - x;
        int ydir = y1 - y;
        int newX = x1 + xdir;
        int newY = y1 + ydir;
        int allX = x - xdir;
        int allY = y - ydir;
        int enemy = currentPlayer == Settings.PLAYER_1 ? Settings.PLAYER_2 : Settings.PLAYER_1;
        if ((validPosition(newX, newY) && cells[newX][newY] != null && cells[newX][newY].getType() == enemy) || (validPosition(allX, allY) && cells[allX][allY] != null && cells[allX][allY].getType() == enemy)) {
            eat = true;
        }
        return eat;
    }

    private void changeTurn(){
        currentPlayer = currentPlayer == Settings.PLAYER_1 ? Settings.PLAYER_2 : Settings.PLAYER_1;
        fillSelectable();
    }
    private boolean shouldEat(int x , int y){
        for (int i = x - 1; i < x + 2 && i < TABLE_HEIGHT; i++) {
            for (int j = y - 1; j < y + 2 && j < TABLE_WIDTH; j++) {
                if (i >= 0 && j >= 0 && (i != x || j != y) && cells[i][j] == null) { //Ho aggiunto solo questo controllo
                    if ((x % 2 != y % 2 && (i == x || j == y)) || x % 2 == y % 2) {
                        if (eatSomething(x, y, i, j)) return true;
                    }
                }
            }
        }
        return false;
    }

    private void fillSelectable(){
        selectable.clear();
        for(int i = 0 ; i<TABLE_HEIGHT;++i){
            for(int j = 0;j<TABLE_WIDTH;++j){
                if(cells[i][j]!=null && cells[i][j].getType()==currentPlayer && shouldEat(i,j)){
                    selectable.add(cells[i][j]);
                }
            }
        }
    }

    private void addPossibleMoves(int x, int y) {
        possibleMoves.clear();
        boolean canEat = false;
        for (int i = x - 1; i < x + 2 && i < TABLE_HEIGHT; i++) {
            for (int j = y - 1; j < y + 2 && j < TABLE_WIDTH; j++) {
                if (i >= 0 && j >= 0 && (i != x || j != y) && cells[i][j] == null) {
                    if ((x % 2 != y % 2 && (i == x || j == y)) || x % 2 == y % 2) {
                        if (eatSomething(x, y, i, j)) {
                            if (!canEat) {
                                possibleMoves.clear();
                                canEat = true;
                            }
                            possibleMoves.add(Pair.of(i, j));
                        } else if (!canEat) {
                            possibleMoves.add(Pair.of(i, j));
                        }
                    }
                }
            }
        }
    }

    public void setCells(Piece[][] cells) {
        this.cells = cells;
    }

    private boolean canMove(int x, int y) {
        var p = Pair.of(x, y);
        return possibleMoves.contains(p);
    }

    private void eat(int newY, int newX, int ydir, int xdir) {
        int enemy = Settings.PLAYER_2;
        //boolean eaten = false;
        if (currentPlayer == Settings.PLAYER_2)  enemy = Settings.PLAYER_1;
        List<Piece> away = new ArrayList<>();
        List<Piece> into = new ArrayList<>();
        // mangio per avvicinamento
        int eatx = newX + xdir;
        int eaty = newY + ydir;
        while (eatx >= 0 && eaty >= 0 && eatx < TABLE_WIDTH && eaty < TABLE_HEIGHT && cells[eaty][eatx] != null && cells[eaty][eatx].getType() == enemy) {
            //cells[eaty][eatx] = null;
            into.add(cells[eaty][eatx]);
            eatx += xdir;
            eaty += ydir;
        }
        // mangio per allontanamento
        eatx = newX - (xdir * 2);
        eaty = newY - (ydir * 2);
        while (eatx >= 0 && eaty >= 0 && eatx < TABLE_WIDTH && eaty < TABLE_HEIGHT && cells[eaty][eatx] != null && cells[eaty][eatx].getType() == enemy) {
            away.add(cells[eaty][eatx]);
            eatx -= xdir;
            eaty -= ydir;
        }
        if(away.isEmpty()){
            for(var p : into){
                cells[p.getY()][p.getX()] = null;
            }
        }
        else if(into.isEmpty()){
            for(var p : away){
                cells[p.getY()][p.getX()] = null;
            }
        }
        else {
            choosingWhatToEat = true;
            Piece headAway = away.get(0);
            Piece headInto = into.get(0);
            choose.put(Pair.of(headAway.getY(),headAway.getX()),away);
            choose.put(Pair.of(headInto.getY(),headInto.getX()),into);
        }
    }

    private void move(int i, int j, int i1, int j1) {
        cells[i1][j1] = new Piece(i1, j1, cells[i][j].getType());
        cells[i][j] = null;
        eat(i1, j1, i1 - i, j1 - j);
        changeTurn();

    }

    public void select(int x, int y) {
        if(choosingWhatToEat){
            var pair = Pair.of(x,y);
            if(choose.get(pair) == null) return;
            var list = choose.get(pair);
            for(var p : list){
                cells[p.getY()][p.getX()] = null;
            }
            choose.clear();
            choosingWhatToEat = false;
            return;
        }
        for (int i = 0; i < TABLE_HEIGHT; i++) {
            for (int j = 0; j < TABLE_WIDTH; j++) {
                if (cells[i][j] != null) {
                    if (i == x && j == y) {
                        if(!selectable.isEmpty() && !selectable.contains(cells[i][j])) {
                            return;
                        }
                        boolean selected = cells[i][j].isSelected() ? false : true;
                        if(cells[i][j].getType() != currentPlayer) return;
                        cells[i][j].setSelected(selected);
                        if (selected) {
                            currentSelected = cells[i][j];
                            addPossibleMoves(i, j);
                        } else possibleMoves.clear();
                    } else {
                        cells[i][j].setSelected(false);
                    }
                } else if (i == x && j == y) {
                    if (canMove(x, y)) {
                        move(currentSelected.getY(), currentSelected.getX(), x, y);
                    }
                    possibleMoves.clear();
                }
            }
        }
    }

    public List<Pair<Integer, Integer>> getPossibleMoves() {
        return possibleMoves;
    }

    public Map<Pair<Integer, Integer>, List<Piece>> getChoose() {
        return choose;
    }
}
