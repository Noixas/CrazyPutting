package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector3;

public class Hole extends GameObject {
  //  private Vector3 position;
    private int radius;

    public Hole(int radius){
        this.radius = radius;
    }

    public void setPosition(Vector3 position) {
        _position = new Vector3(position);
    }

    public void setPositionX(float x){
        _position.x = x;
    }

    public void setPositionY(float y){
        _position.y = y;
    }

    public int getRadius() {
        return radius;
    }

}
