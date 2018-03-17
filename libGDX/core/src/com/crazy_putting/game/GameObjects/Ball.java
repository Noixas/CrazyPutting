package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;

public class Ball extends GameObject{
    private int x;
    private int y;
    private Texture texture;

    public Ball(String filename){
        texture = new Texture(filename);
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Texture getTexture() {
        return texture;
    }

}
