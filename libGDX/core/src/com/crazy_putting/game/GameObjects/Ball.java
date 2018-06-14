package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Others.Velocity;
import com.crazy_putting.game.Physics.Physics;

public class Ball extends PhysicsGameObject implements Comparable<Ball> {

    private final float MASS = (float) 0.04593;

    private Vector3 previousPosition;
    private Velocity velocity;
    private Velocity velocityGA;
    private boolean _isFixed;

    private int fitnessValue;

    public Ball(Vector3 pPosition){
        initBall();
        setPosition(pPosition);
    }

    public Ball(){
        initBall();
    }

    private void initBall(){
        previousPosition = new Vector3();
        velocity = new Velocity();
        velocityGA = new Velocity();
        _isFixed=true;
        Physics.physics.addMovableObject(this);
    }

    public int getFitnessValue(){
        return this.fitnessValue;
    }

    public void setFitnessValue(int value){
        this.fitnessValue = value;
    }

    public Velocity getVelocityGA(){
        return velocityGA;
    }

    public void setVelocityGA(float speed, float angle){
        velocityGA.setAngle(angle);
        velocityGA.setSpeed(speed);
    }

    public void setVelocity(float speed, float angle){
        this.velocity.setAngle(angle);
        this.velocity.setSpeed(speed);
        this.velocity.updateVelocityComponents();
    }

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

    public void setPosition(Vector3 position) {
          _position =  new Vector3(position);
       previousPosition = new Vector3(position);
    }

    @Override
    public void setPositionXYZ(float x, float y) {
        _position.x = x;
        _position.y = y;
        this.updateHeight();
    }

    public Velocity getVelocity() {
        return velocity;
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
        this.velocity.speed = (float) Math.sqrt(Math.pow(getVelocity().Vx,2)+Math.pow(getVelocity().Vy,2));
    }

    @Override
    public boolean isSlow() {
        return getSpeed() < 3;
    }


    @Override
    public Ball clone(){
        Ball newBall = new Ball(getPosition());
        //  newBall.setPosition(getPosition());

        newBall.previousPosition = new Vector3(previousPosition);
        newBall._position = _position;
        newBall.setPositionXYZ(getPosition().x,getPosition().y);
        newBall.velocity = new Velocity();
        newBall.velocity.speed = velocity.speed;
        newBall.velocity.angle = velocity.angle;
        newBall.velocity.updateVelocityComponents();
        newBall.velocityGA = new Velocity();
        newBall.velocityGA.speed = velocityGA.speed;
        newBall.velocityGA.angle = velocityGA.angle;
        newBall._isFixed = _isFixed;
        newBall.fitnessValue = fitnessValue;
        return newBall;

    }

    @Override
    public int compareTo(Ball o) {
        return this.fitnessValue - o.getFitnessValue();
    }
}
