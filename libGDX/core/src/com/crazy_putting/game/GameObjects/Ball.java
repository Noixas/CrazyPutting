package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.Others.Velocity;

public class Ball extends GameObject{

    private final float MASS = (float) 0.04593;

    private Vector2 position;
    private Velocity velocity;
    private Texture texture;
    private boolean _isMoving = false;

    public Ball(String filename){
        texture = new Texture(filename);
        position = new Vector2();
        velocity = new Velocity();
        setVelocity(.1f,90);
    }
    public Ball(String filename, Vector2 pPosition){
        texture = new Texture(filename);
        position = pPosition;
        velocity = new Velocity();
        setVelocity(1,0);
    }
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPositionX(float x){
        position.x = x;
    }

    public void setPositionY(float y){
        position.y = y;
    }

    public Texture getTexture() {
        return texture;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setSpeed(float speed){
        this.velocity.setSpeed(speed);
    }

    public void setVelocity(float speed, float angle){
        this.velocity.setAngle(angle);
        this.velocity.setSpeed(speed);
    }

    public float getMass(){
        return this.MASS;
    }
    public boolean isMoving()
    {
        return _isMoving;
    }
    public void update(float  dt){
       // System.out.println(getPosition());
        if(getVelocity().getSpeed() < 1)
            _isMoving = false;
        else _isMoving = true;
    }

    

}
