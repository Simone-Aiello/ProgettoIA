package main;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import java.awt.*;

@Id("posizione")
public class Piece {
    @Param(0)
    private int index;
    @Param(2)
    private int x;
    @Param(1)
    private int y;
    private int type;
    private Rectangle hitbox;
    private int hitboxWidth = 54;
    private int hitboxHeight = 54;
    boolean selected;

    public Piece() {}

    public Piece(int y, int x, int type, int index) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.index = index;
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
    public int getNormalX(){
        return x;
    }
    public int getNormalY(){
        return y;
    }
    public int getX() {
        return x + 1;
    }

    public int getY() {
        return y + 1;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
