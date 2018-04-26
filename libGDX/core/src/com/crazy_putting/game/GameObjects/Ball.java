package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.Others.Velocity;

public class Ball extends GameObject{

    private final float MASS = (float) 0.04593;

    private Vector2 previousPosition;
    private Vector2 position;
    private Velocity velocity;
    private Texture texture;
    private boolean _isMoving = false;
    private boolean _isFixed;

    public Ball(){

    }
    public Ball(String filename){
        texture = new Texture(filename);
        position = new Vector2();
        previousPosition = new Vector2();
        velocity = new Velocity();
        _isFixed=true;
        //setVelocity(.1f,90);
    }
    public Ball(String filename, Vector2 pPosition){
        texture = new Texture(filename);
        position = pPosition;
        velocity = new Velocity();
        _isFixed=true;
        //setVelocity(1,0);
    }
    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getPreviousPosition(){ return previousPosition; }


    @Override
    public float getSpeed() {
        float result = (float) Math.sqrt(Math.pow(velocity.Vx,2) + Math.pow(velocity.Vy,2));
        return result;
    }

    @Override
    public boolean isFixed() {
        return this._isFixed;
    }


    public void setPosition(Vector2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.previousPosition.x = position.x;
        this.previousPosition.y = position.y;
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
        return getSpeed()>0.5;
    }
    public void update(float  dt){
       // System.out.println(getPosition());
        if(getVelocity().getSpeed() < 1f){
            _isMoving = false;
        }
        else _isMoving = true;
    }

    public void fix(boolean tf){
        if(tf){
            this.velocity.Vx = (float) 0.01;
            this.velocity.Vy = (float) 0.01;
        }
        this._isFixed=tf;
    }


    public void setVelocityComponents(float Vx, float Vy){
        this.velocity.Vx = Vx;
        this.velocity.Vy = Vy;
        setSpeed((float)Math.sin(Math.toRadians(velocity.angle)/Vy));
    }

    @Override
    public boolean isSlow() {
        return getSpeed() < 1;
    }


    @Override
    public boolean inTheWater() {
        return false;
    }


    @Override
    public Ball clone(){
        Ball newBall = new Ball();
        newBall.texture = texture;
        newBall.position = new Vector2();
        newBall.position.x = position.x;
        newBall.position.y = position.y;
        newBall.previousPosition = new Vector2();
        newBall.previousPosition.x = previousPosition.x;
        newBall.previousPosition.y = previousPosition.y;
        newBall.velocity = new Velocity();
        newBall.velocity.speed = velocity.speed;
        newBall.velocity.angle = velocity.angle;
        newBall._isFixed = _isFixed;
        return newBall;

    }
}
