package main;

import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.AnswerSets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import javax.print.attribute.standard.CopiesSupported;
import javax.swing.JButton;

public class BoardController extends MouseAdapter implements Callback {
    private Board board;
    private BoardPanel panel;
    private JButton salta; 
    private long currentTime;

    public BoardController(Board board, BoardPanel panel, JButton salta ) {
        this.board = board;
        this.panel = panel;
        this.salta = salta;
        currentTime = System.currentTimeMillis();
        salta.setVisible(false);
        AIPlayer.getInstance().init("lib/dlv2.exe","resources/turni.txt",this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        currentTime = System.currentTimeMillis();
        if(e.getSource() instanceof JButton) {
            board.changeTurn();
            salta.setVisible(false);
        }
        else if(board.getCurrentPlayer()==Settings.PLAYER_1){
            int x = e.getX();
            int y = e.getY();
            for (int i = 0; i < board.getTABLE_HEIGHT(); ++i) {
                for (int j = 0; j < board.getTABLE_WIDTH(); ++j) {
                    int coordY = i * panel.getCellDimension() + panel.getPaddingY();
                    int coordX = j * panel.getCellDimension() + panel.getPaddingX();
                    if (x <= coordX + 27 && x >= coordX - 27 && y <= coordY + 27 && y >= coordY - 27) {
                        board.select(i, j);
                        if (board.isStoppable())
                            salta.setVisible(true);
                        else salta.setVisible(false);
                        panel.update();
                        return;
                    }
                }
            }
        }
        panel.update();
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

    @Override
    public void callback(Output output) {
    	if(System.currentTimeMillis()-currentTime<500)
			try {
				Thread.sleep(500-System.currentTimeMillis()+currentTime);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
        AnswerSets as = (AnswerSets) output;
        var answerSets = as.getOptimalAnswerSets();
        Random r = new Random();
        if(answerSets.isEmpty()) {
        	System.out.println("Partita finita");
        	return;
        }
        int index = r.nextInt(answerSets.size());
        var optimalAs = answerSets.get(index);
        List<String[]> moves = new ArrayList<>();
        List<String[]> eat = new ArrayList<>();
        for(String s : optimalAs.getAnswerSet()){
            //System.out.println(s);
            if(s.contains("mossa(")){
                s = s.replace("mossa","");
                s = s.replace("(","");
                s = s.replace(")","");
                String[] splitted = s.split(",");
                if(Integer.parseInt(splitted[6])==1)
                	moves.add(splitted);
            }
            if(s.contains("celleMangiate")){
                s = s.replace("celleMangiate(","");
                s = s.replace("(","");
                s = s.replace(")","");
                String [] split = s.split(",");
                eat.add(split);
                if(Integer.parseInt(split[2])==1)
                	eat.add(split);
            }
        }
        for(String[] m : moves){
            int y = Integer.parseInt(m[1]);
            int x = Integer.parseInt(m[2]);
            int y1 = Integer.parseInt(m[3]);
            int x1 = Integer.parseInt(m[4]);
            int time = Integer.parseInt(m[5]);
            board.move(y - 1, x -1 , y1 - 1,x1 - 1);
            for(String[] e : eat){
                if(e[1].equals(""+time)){
                    board.remove(Integer.parseInt(e[0]));
                }
            }
            try {
                panel.update();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        board.changeTurn();
        panel.update();

    }
}
