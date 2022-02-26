package main;


import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        BoardPanel panel = new BoardPanel();
        panel.setLayout(new BorderLayout());
        Board game = new Board();
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        JButton salta = new JButton("Salta");
        bottom.add(salta);
        BoardController controller = new BoardController(game,panel , salta);     
        panel.setController(controller);
        JFrame f = new JFrame("FANORON-AI");
        f.add(panel);
        panel.setFocusable(true);
        panel.requestFocus();
        panel.add(bottom , BorderLayout.SOUTH);
        salta.addMouseListener(controller);
        f.setResizable(false);
        f.setSize(1000,580);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        panel.repaint();
    }
}
