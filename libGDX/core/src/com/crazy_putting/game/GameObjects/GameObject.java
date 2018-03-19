package com.crazy_putting.game.GameObjects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.Others.Velocity;

import javax.swing.text.Position;

public abstract class GameObject {

    public abstract  Texture getTexture() ;
    public abstract Vector2 getPosition();
    public abstract Velocity getVelocity();
    public abstract float getMass();
    public abstract void setPosition(Vector2 position);
    public abstract void setPositionX(float x);
    public abstract void setPositionY(float y);
    public abstract void setVelocity(float speed, float angle);
    public abstract void setSpeed(float speed);
}
