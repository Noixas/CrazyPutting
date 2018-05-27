package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Others.Velocity;

public class Ball extends PhysicsGameObject {

    private final float MASS = (float) 0.04593;

    private Vector3 previousPosition;
    //private Vector3 position;
    private Velocity velocity;
    private Texture texture;
    private boolean _isMoving = false;
    private boolean _isFixed;

    public Ball(){
        previousPosition = new Vector3();
        velocity = new Velocity();
        _isFixed=true;
    }
    public Ball(String filename){
        texture = new Texture(filename);
       // position = new Vector3();
        previousPosition = new Vector3();
        velocity = new Velocity();
        _isFixed=true;
        //setVelocity(.1f,90);
    }

//    public Vector3 getPosition() {
//        return position;
//    }
    public Vector3 getPreviousPosition(){
        return previousPosition;
    }


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
        getPosition().x = position.x;
        getPosition().y = position.y;
        this.previousPosition.x = position.x;
        this.previousPosition.y = position.y;

        _position.x = position.x;
        _position.y = position.y;
    }
    public void setPosition(Vector3 position) {
          _position =  new Vector3(position);
       previousPosition = new Vector3(position);
    }
    public void setPositionX(float x){
        _position.x = x;
    }

    public void setPositionY(float y){
        _position.y = y;
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
        this.velocity.updateVelocityComponents();
    }

    public float getMass(){
        return this.MASS;
    }
    public boolean isMoving(float speedTolerance)
    {
        return getSpeed()>speedTolerance;
    }

    public boolean isMoving()
    {
        return getSpeed()>0.5f;
    }

    /**
     * Every delta time checks if the ball is moving or not.
     */
    public void update(float  dt){
        if(getVelocity().getSpeed() < 0.5f){
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
        setSpeed((float)(Math.sqrt(Math.pow(getVelocity().Vx,2)+Math.pow(getVelocity().Vy,2))));
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
        newBall.setPosition(getPosition());
        newBall.previousPosition = new Vector3(previousPosition);
        newBall.velocity = new Velocity();
        newBall.velocity.speed = velocity.speed;
        newBall.velocity.angle = velocity.angle;
        newBall._isFixed = _isFixed;
        return newBall;

    }
}
