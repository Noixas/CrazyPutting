package com.crazy_putting.game.Physics;

public class Derivative {

    private float dx;      // dp/dt = velocity
    private float dy;

    private float dvx;      // dv/dt = acceleration
    private float dvy;

    public Derivative() {
    }

    public Derivative(float dx, float dy, float dvx, float dvy) {
        this.dx = dx;
        this.dy = dy;
        this.dvx = dvx;
        this.dvy = dvy;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public float getDvx() {
        return dvx;
    }

    public void setDvx(float dvx) {
        this.dvx = dvx;
    }

    public float getDvy() {
        return dvy;
    }

    public void setDvy(float dvy) {
        this.dvy = dvy;
    }
}
