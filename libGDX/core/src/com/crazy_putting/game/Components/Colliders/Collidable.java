package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Others.Velocity;

public interface Collidable {
    float getMass();
    Vector3 getPosition();
    void setPosition(Vector3 position);
    boolean isStatic();
    void makeStatic();
    void makeNonStatic();
    Velocity getVelocity();
    void setVeloctiy(Velocity velocity);
    float getInverseMass();
}
