package main;

import it.unical.mat.embasp.languages.asp.AnswerSets;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private int TABLE_HEIGHT = 5;
    private int TABLE_WIDTH = 9;
    private Pair<Integer, Integer> lastDirection = null;
    private Piece[][] cells = new Piece[TABLE_HEIGHT][TABLE_WIDTH];
    private List<Piece> selectable = new ArrayList<>();
    private Piece currentSelected = null;
    private List<Pair<Integer, Integer>> possibleMoves = new ArrayList<>(8);
    private boolean choosingWhatToEat = false;
    private boolean stoppable = false;
    private Map<Pair<Integer,Integer>,List<Piece>> choose = new HashMap<>();
    private int currentPlayer = Settings.PLAYER_1;
    private void init() {
        int blackIndex = 1;
        int whiteIndex = 23;
        for (int i = 0; i < TABLE_HEIGHT / 2; ++i) {
            for (int j = 0; j < TABLE_WIDTH; ++j) {
                cells[i][j] = new Piece(i, j, Settings.PLAYER_2,blackIndex++);
                cells[TABLE_HEIGHT - 1 - i][j] = new Piece(TABLE_HEIGHT - 1 - i, j, Settings.PLAYER_1,whiteIndex++);
            }
        }
        int alternation = Settings.PLAYER_2;
        for (int j = 0; j < TABLE_WIDTH; ++j) {
            if (j != TABLE_WIDTH / 2) {
                cells[TABLE_HEIGHT / 2][j] = new Piece(TABLE_HEIGHT / 2, j, alternation, alternation == Settings.PLAYER_2 ? blackIndex++ : whiteIndex++);
                alternation = alternation == Settings.PLAYER_1 ? Settings.PLAYER_2 : Settings.PLAYER_1;
            }
        }
        //cells[1][5] = null;
        fillSelectable();
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
    public List<PossibleMoves> IAMoves(){
        List<PossibleMoves> possibleAIMoves = new ArrayList<>();
        boolean canEat = false;
        for(Piece p : selectable){
            int x = p.getNormalY();
            int y = p.getNormalX();
            for (int i = x - 1; i < x + 2 && i < TABLE_HEIGHT; i++) {
                for (int j = y - 1; j < y + 2 && j < TABLE_WIDTH; j++) {
                    if (i >= 0 && j >= 0 && (i != x || j != y) && cells[i][j] == null) {
                        if ((x % 2 != y % 2 && (i == x || j == y)) || x % 2 == y % 2) {
                            if (eatSomething(x, y, i, j) && (lastDirection == null || (!lastDirection.equals(Pair.of(i-x,j-y)) && !lastDirection.equals(Pair.of(x-i,y-j))))) {
                                if (false) {
                                    possibleAIMoves.clear();
                                    canEat = true;
                                }
                                possibleAIMoves.add(new PossibleMoves(p.getIndex(),i + 1,j + 1));
                            } else if (!canEat) {
                                possibleAIMoves.add(new PossibleMoves(p.getIndex(),i + 1,j + 1));
                            }
                        }
                    }
                }
            }
        }
        System.out.println(possibleAIMoves.size());
        return possibleAIMoves;
    }
    private List<Piece> currentPiecesPositions(){
        List<Piece> p = new ArrayList<>();
        for(int i = 0; i < TABLE_HEIGHT;i++){
            for(int j = 0; j < TABLE_WIDTH;j++){
                if (cells[i][j] != null){
                    p.add(cells[i][j]);
                }
            }
        }
        return p;
    }
    public void changeTurn(){
        currentPlayer = currentPlayer == Settings.PLAYER_1 ? Settings.PLAYER_2 : Settings.PLAYER_1;
        lastDirection = null;
        stoppable = false;
        possibleMoves.clear();
        if(currentSelected != null && cells[currentSelected.getNormalY()][currentSelected.getNormalX()] != null && currentSelected!= null) {
            cells[currentSelected.getNormalY()][currentSelected.getNormalX()].setSelected(false);
        }
        currentSelected = null;
        fillSelectable();
        if(currentPlayer == Settings.PLAYER_2){
            List<PossibleMoves> moves = IAMoves();
            List<Piece> pieces = currentPiecesPositions();
            Rocco.getInstance().addFacts(moves,pieces);
            Rocco.getInstance().startIA();
        }
    }
    private boolean shouldEat(int x , int y){
        for (int i = x - 1; i < x + 2 && i < TABLE_HEIGHT; i++) {
            for (int j = y - 1; j < y + 2 && j < TABLE_WIDTH; j++) {
                if (i >= 0 && j >= 0 && (i != x || j != y) && cells[i][j] == null) { //Ho aggiunto solo questo controllo
                    if ((x % 2 != y % 2 && (i == x || j == y)) || x % 2 == y % 2) {
                        if (eatSomething(x, y, i, j) && (lastDirection == null || (!lastDirection.equals(Pair.of(i-x,j-y)) && !lastDirection.equals(Pair.of(x-i,y-j))))) {
                        	return true;
                        }
                        }
                        //&& (lastDirection == null || lastDirection.equals(Pair.of(i-x,j-y)) || lastDirection.equals(Pair.of(x-i,y-j))
                    }
                }
            }
        return false;
    }
    
    private boolean moveable(int x , int y) {
    	  for (int i = x - 1; i < x + 2 && i < TABLE_HEIGHT; i++) {
              for (int j = y - 1; j < y + 2 && j < TABLE_WIDTH; j++) {
                  if (i >= 0 && j >= 0 && (i != x || j != y) && cells[i][j] == null) {
                    return true;
                  }
              }
          }
    	  return false;
    }
    
    private void fillSelectable(){
        selectable.clear();
        List<Piece> moveable = new ArrayList<Piece>();
        for(int i = 0 ; i<TABLE_HEIGHT;++i){
            for(int j = 0;j<TABLE_WIDTH;++j){
                if(cells[i][j]!=null && cells[i][j].getType()==currentPlayer ){
                	if(shouldEat(i,j))
                		selectable.add(cells[i][j]);
                	if(selectable.isEmpty()) {
                		if(moveable(i, j)) {
                			moveable.add(cells[i][j]);
                		}
                	}
                }
            }
        }
        if(selectable.isEmpty()) {
        	selectable = moveable;
        }
        
    }

    private void addPossibleMoves(int x, int y) {
        possibleMoves.clear();
        boolean canEat = false;
        for (int i = x - 1; i < x + 2 && i < TABLE_HEIGHT; i++) {
            for (int j = y - 1; j < y + 2 && j < TABLE_WIDTH; j++) {
                if (i >= 0 && j >= 0 && (i != x || j != y) && cells[i][j] == null) {
                    if ((x % 2 != y % 2 && (i == x || j == y)) || x % 2 == y % 2) {
                        if (eatSomething(x, y, i, j) && (lastDirection == null || (!lastDirection.equals(Pair.of(i-x,j-y)) && !lastDirection.equals(Pair.of(x-i,y-j))))) {
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

    private boolean eat(int newY, int newX, int ydir, int xdir) {
        int enemy = Settings.PLAYER_2;
        boolean eaten = false;
        if (currentPlayer == Settings.PLAYER_2)  enemy = Settings.PLAYER_1;
        List<Piece> away = new ArrayList<>();
        List<Piece> into = new ArrayList<>();
        // mangio per avvicinamento
        int eatx = newX + xdir;
        int eaty = newY + ydir;
        while (eatx >= 0 && eaty >= 0 && eatx < TABLE_WIDTH && eaty < TABLE_HEIGHT && cells[eaty][eatx] != null && cells[eaty][eatx].getType() == enemy) {
            into.add(cells[eaty][eatx]);
            eatx += xdir;
            eaty += ydir;
            eaten = true;
        }
        // mangio per allontanamento
        eatx = newX - (xdir * 2);
        eaty = newY - (ydir * 2);
        while (eatx >= 0 && eaty >= 0 && eatx < TABLE_WIDTH && eaty < TABLE_HEIGHT && cells[eaty][eatx] != null && cells[eaty][eatx].getType() == enemy) {
            away.add(cells[eaty][eatx]);
            eatx -= xdir;
            eaty -= ydir;
            eaten = true;
        }
        
        if(eaten) {
        	lastDirection = Pair.of(ydir , xdir);
        }
        
        if(away.isEmpty()){
            for(var p : into){
                cells[p.getNormalY()][p.getNormalX()] = null;
            }
        }
        else if(into.isEmpty()){
            for(var p : away){
                cells[p.getNormalY()][p.getNormalX()] = null;
            }
        }
        else {
            choosingWhatToEat = true;
            Piece headAway = away.get(0);
            Piece headInto = into.get(0);
            choose.put(Pair.of(headAway.getNormalY(),headAway.getNormalX()),away);
            choose.put(Pair.of(headInto.getNormalY(),headInto.getNormalX()),into);
        }
        return eaten;
    }

    public void move(int i, int j, int i1, int j1) {
        cells[i1][j1] = new Piece(i1, j1, cells[i][j].getType(),cells[i][j].getIndex());
        cells[i][j] = null;  
    }

    public void select(int x, int y) {
        if(choosingWhatToEat){
            var pair = Pair.of(x,y);
            if(choose.get(pair) == null) return;
            var list = choose.get(pair);
            for(var p : list){
                cells[p.getNormalY()][p.getNormalX()] = null;
            }
            choose.clear();
            choosingWhatToEat = false;
            if(!stoppable)
            	changeTurn();
            //currentSelected = cells[x][y];
            //fillSelectable();
            //changeTurn();
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
                        move(currentSelected.getNormalY(), currentSelected.getNormalX(), x, y);
                        if(!eat(x, y, x - currentSelected.getNormalY(), y - currentSelected.getNormalX())) changeTurn();
						else if (shouldEat(x,y)) {
                        	currentSelected = cells[x][y];
                        	selectable.clear();
                        	selectable.add(currentSelected);
                        	stoppable = true;
                        }
                        else if(!choosingWhatToEat) changeTurn();
                        else selectable.clear();
                    }
                    possibleMoves.clear();
                }
            }
        }
    }
    
    public List<Piece> getSelectable() {
		return selectable;
	}
    

    public List<Pair<Integer, Integer>> getPossibleMoves() {
        return possibleMoves;
    }

    public Map<Pair<Integer, Integer>, List<Piece>> getChoose() {
        return choose;
    }
    
    public boolean isStoppable() {
		return stoppable;
	}

    public int getCurrentPlayer() {
		return currentPlayer;
	}
    

    public void remove(int index) {
        for(int i = 0; i < TABLE_HEIGHT;i++){
            for(int j = 0; j < TABLE_WIDTH;j++){
                if(cells[i][j] != null && cells[i][j].getIndex() == index){
                    cells[i][j] = null;
                    return;
                }
            }
        }
    }
}
