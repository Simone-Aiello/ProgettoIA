package main;

import java.awt.*;

public class Piece {
    private int x;
    private int y;
    private int type;
    private Rectangle hitbox;
    private int hitboxWidth = 54;
    private int hitboxHeight = 54;
    boolean selected;
    public Piece(int y, int x, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        hitbox = new Rectangle(x,y,hitboxWidth,hitboxHeight);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
