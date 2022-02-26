package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BoardPanel extends JPanel {
	private final int cellDimension = 101;
	private BoardController b;
	private int paddingX = 90;
	private int paddingY = 65;
	private Image board = null;
	private Image white = null;
	private Image black = null;
	private Image red = null;
	private Image purple = null;
	public BoardPanel() {

		try {		
			board = ImageIO.read(new File("board3.png"));
			white = ImageIO.read(new File("white.png")).getScaledInstance(54, 54, Image.SCALE_SMOOTH);
			black = ImageIO.read(new File("black.png")).getScaledInstance(54, 54, Image.SCALE_SMOOTH);
			red = ImageIO.read(new File("red.png")).getScaledInstance(54, 54, Image.SCALE_SMOOTH);
			purple = ImageIO.read(new File("purple.png")).getScaledInstance(54, 54, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.exit(0);
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			g.drawImage(board.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
		} catch (Exception e) {
			System.exit(0);
		}

		g.setColor(Color.black);
			/*for (int i = 0; i < b.getBoard().getTABLE_HEIGHT(); i++) {
				for (int j = 0; j < b.getBoard().getTABLE_WIDTH() - 1; j++) {
					int realX = j * cellDimension + paddingX;
					int realY = i * cellDimension + paddingY;
					if (i == 0 && j == 0)
						System.out.println(realX + " " + realY);
					if (i != b.getBoard().getTABLE_HEIGHT() - 1) {
						g.drawRect(realX, realY, cellDimension, cellDimension);
						if (i % 2 == j % 2)
							g.drawLine(realX, realY, realX + cellDimension, realY + cellDimension);
					}
					if (i != 0) {
						if (i % 2 == j % 2)
							g.drawLine(realX, realY, realX + cellDimension, realY - cellDimension);
					}

				}
			}
		}*/
		g.setColor(Color.orange);
		for (var p : b.getBoard().getSelectable()) {
			g.fillOval(p.getNormalX() * cellDimension - 31 + paddingX, p.getNormalY() * cellDimension - 31 + paddingY,
					62, 62);
		}
		for (int i = 0; i < b.getBoard().getTABLE_HEIGHT(); i++) {
			for (int j = 0; j < b.getBoard().getTABLE_WIDTH(); j++) {
				if (b.getBoard().getCells()[i][j] != null) {
					if (b.getBoard().getCells()[i][j].isSelected()) {
						g.drawImage(red, j * cellDimension - 27 + paddingX, i * cellDimension - 27 + paddingY, null);
					} else if (b.getBoard().getCells()[i][j].getType() == Settings.PLAYER_1) {

						g.drawImage(white, j * cellDimension - 27 + paddingX, i * cellDimension - 27 + paddingY, null);
					} else {

						g.drawImage(black, j * cellDimension - 27 + paddingX, i * cellDimension - 27 + paddingY, null);
					}

					g.setColor(Color.BLACK);
					if (b.getBoard().getCells()[i][j].getType() == Settings.PLAYER_2)
						g.setColor(Color.WHITE);
					g.setFont(new Font("TimesRoman", Font.PLAIN, 22));
					g.drawString("" + b.getBoard().getCells()[i][j].getIndex(), j * cellDimension - 30 + paddingX + 20,
							i * cellDimension - 27 + paddingY + 30);
				}
			}
		}
		var possibleMove = b.getBoard().getPossibleMoves();
		g.setColor(Color.YELLOW);
		for (int i = 0; i < possibleMove.size(); i++) {
			int y = possibleMove.get(i).getLeft();
			int x = possibleMove.get(i).getRight();
			g.fillOval(x * cellDimension - 27 + paddingX, y * cellDimension - 27 + paddingY, 54, 54);
		}
		var eatChooses = b.getBoard().getChoose();
		for ( var c : eatChooses.entrySet()) {
			var key = c.getKey();
			int y = key.getLeft();
			int x = key.getRight();
			g.drawImage(purple,x * cellDimension - 27 + paddingX, y * cellDimension - 27 + paddingY,null);
			g.drawString("" +c.getValue().get(0).getIndex(), x * cellDimension - 30 + paddingX + 20,
					y * cellDimension - 27 + paddingY + 30);
		}
	}

	public void update() {
		this.repaint();
	}

	public int getCellDimension() {
		return cellDimension;
	}

	public int getPaddingX() {
		return paddingX;
	}

	public int getPaddingY() {
		return paddingY;
	}

	public void setController(BoardController controller) {
		this.addMouseListener(controller);
		b = controller;
	}
}
