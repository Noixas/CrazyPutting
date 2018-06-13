package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector3;

public class Hole extends GameObject {
  //  private Vector3 position;
    private int radius;

    public Hole(int radius, Vector3 position){
        this.radius = radius;
        setPosition(position);
    }

    public void setPosition(Vector3 position) {
        _position = new Vector3(position);
    }

    public int getRadius() {
        return radius;
    }

}
