package com.crazy_putting.game.Others;

import com.crazy_putting.game.Physics.State;

public class Noise2 {
    State a = new State();
    float x= a.getX();
    float y= a.getY();
    float vx= a.getVx();
    float vy= a.getVy();

    public void noise(float n1, float n2, float n3, float n4) {

        if (Math.random() <= 0.5) {
            a.setX(x+n1);
            a.setY(y+n2);
            a.setVx(vx+n3);
            a.setVy(vy+n4);
            }
            else{
            a.setX(x-n1);
            a.setY(y-n2);
            a.setVx(vx-n3);
            a.setVy(vy-n4);
        }
    }
}