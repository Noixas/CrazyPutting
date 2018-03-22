package com.crazy_putting.game.GameObjects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.Components.GraphicsComponent;
import com.crazy_putting.game.Others.Velocity;

public abstract class GameObject {
    private GraphicsComponent _graphicComponent;
    public void addGraphicComponent(GraphicsComponent pGC)
    {
        _graphicComponent = pGC;
        _graphicComponent.setOwner(this);
    }
    public abstract  Texture getTexture() ;
    public abstract Vector2 getPosition();
    public abstract Velocity getVelocity();
    public abstract float getMass();
    public abstract void setPosition(Vector2 position);
    public abstract void setPositionX(float x);
    public abstract void setPositionY(float y);
    public abstract void setVelocity(float speed, float angle);
    public abstract void setSpeed(float speed);
    public abstract boolean inTheWater();
    public abstract Vector2 getPreviousPosition();
    public abstract float getSpeed();
    public abstract boolean isFixed();
    public abstract void fix(boolean tf);

}
