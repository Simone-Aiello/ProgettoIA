package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        BoardPanel panel = new BoardPanel();
        Board game = new Board();
        BoardController controller = new BoardController(game,panel);
        panel.setController(controller);
        JFrame f = new JFrame();
        f.add(panel);
        f.setSize(1000,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
