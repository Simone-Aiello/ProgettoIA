package main;


import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        BoardPanel panel = new BoardPanel();
        Board game = new Board();
        JPanel bottom = new JPanel() {
        	protected void paintComponent(Graphics g) {
        		g.setColor(new Color(193,154,107));
        		g.fillRect(0, 0, this.getWidth(), this.getHeight());
        	};
        }
        ;
        JButton salta = new JButton("Salta");
        bottom.add(salta);
        BoardController controller = new BoardController(game,panel , salta);     
        panel.setController(controller);
        JFrame f = new JFrame();
        f.add(panel , BorderLayout.CENTER);
        f.add(bottom , BorderLayout.SOUTH);
        salta.addMouseListener(controller);
        f.setResizable(false);
        f.setSize(1000,580);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
