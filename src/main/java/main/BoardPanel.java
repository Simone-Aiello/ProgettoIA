package main;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
	private final int cellDimension = 100;
	private BoardController b;
	private int paddingX = 90;
	private int paddingY = 65;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < b.getBoard().getTABLE_HEIGHT(); i++) {
			for (int j = 0; j < b.getBoard().getTABLE_WIDTH() - 1; j++) {
				int realX = j * cellDimension + paddingX;
				int realY = i * cellDimension + paddingY;
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
		g.setColor(Color.orange);
		for(var p : b.getBoard().getSelectable()) {
			g.fillOval(p.getX() * cellDimension - 29 + paddingX, p.getY() * cellDimension - 29 + paddingY, 58, 58);
		}
		for (int i = 0; i < b.getBoard().getTABLE_HEIGHT(); i++) {
			for (int j = 0; j < b.getBoard().getTABLE_WIDTH(); j++) {
				if (b.getBoard().getCells()[i][j] != null) {
					if (b.getBoard().getCells()[i][j].getType() == Settings.PLAYER_1) {
						g.setColor(Color.LIGHT_GRAY);
					} else {
						g.setColor(Color.BLACK);
					}
					if (b.getBoard().getCells()[i][j].isSelected()) {
						int red = g.getColor().getRed();
						int green = g.getColor().getGreen();
						int blue = g.getColor().getBlue();
						g.setColor(Color.RED);
					}
					g.fillOval(j * cellDimension - 27 + paddingX, i * cellDimension - 27 + paddingY, 54, 54);
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
		g.setColor(new Color(191, 64, 191));
		for (var c : eatChooses.entrySet()) {
			var key = c.getKey();
			int y = key.getLeft();
			int x = key.getRight();
			g.fillOval(x * cellDimension - 27 + paddingX, y * cellDimension - 27 + paddingY, 54, 54);
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
