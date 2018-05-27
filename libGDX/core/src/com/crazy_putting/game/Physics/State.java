package com.crazy_putting.game.Physics;

import com.crazy_putting.game.GameObjects.PhysicsGameObject;

public class State {

    private float x;
    private float y;
    private float vx;
    private float vy;

    public void update(PhysicsGameObject obj) {
        this.x = obj.getPosition().x;
        this.y = obj.getPosition().y;
        this.vx = obj.getVelocity().Vx;
        this.vy = obj.getVelocity().Vy;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }
}
