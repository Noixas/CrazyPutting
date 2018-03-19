package com.crazy_putting.game.Physics;

import com.crazy_putting.game.GameObjects.GameObject;

import java.util.ArrayList;

public class PhysicsTest {
    
    private final double g = 9.81;

    //just create a friction coefficient here for now
    private final float mu = (float) 0.4;


    //list of all moving objects
    private ArrayList<GameObject> movingThings = new ArrayList<GameObject>();
    
    
    private GameObject ball;

    
    //the height equation 
    // H = 0.01*X + 0.03*X^2 + 0.2*Y
    private double partialDerivativeX() {
        return 0.1 + 0.06 * ball.getPosition().x;
    }

    private double partialDerivativeY() {
        return 0.2;
    }
    
    
    /*
    applying the force from user 
    */
    public void appliedForce(GameObject someObj, float angle, float speed){
        someObj.getVelocity().setAngle(angle);
        someObj.getVelocity().setSpeed(speed);
        
    }
    
    
    
    public void update(GameObject obj, double dt){
        this.ball = obj;
        
        float x = obj.getPosition().x;
        float y = obj.getPosition().y;
        
        
        //calculation of a new X position 
        // x(t + h) = x(t) +hVx(t);
        float newX = (float) (x + (dt * ball.getVelocity().Vx));
        
        //calculation of a new Y position
        // y(t+h) = y(t) + hVy(t);
        float newY = (float) (y + (dt * ball.getVelocity().Vy));
        
        //calculation of a new total velocity of the ball
        // v(t+h) = v(t) + h*F(x,y,vx,vy)/m
         float newSpeed = (float) (this.ball.getVelocity().speed + dt * totalForce() / this.ball.getMass());

        ball.setSpeed(newSpeed);
        ball.setPositionX(newX);
        ball.setPositionY(newY);
        
        this.ball=null;
    }
    
    /*
    total power that affects the ball is
    F = G + H;
    */

    //Calculation of the Gravitational Force
    //G = -mgh(,x) - mgh(,y)
    private float gravityForce() {
        float result = (float) (- ball.getMass() * g * partialDerivativeX());
        result -= ball.getMass() * g * partialDerivativeY();

        return result;
    }

    /*
    Calculation of the Force of friction
    H = -(mu)* m* v / ||V||
    V = vx/cos(x)
     */
    
    private float frictionForce(){
        
        float numerator = (float) (- mu * ball.getMass() * g * ball.getVelocity().speed);
        float lengthOfVelocityVector = (float) (Math.pow((double) ball.getVelocity().Vx, 2) + Math.sqrt(Math.pow((double) ball.getVelocity().Vy, 2)));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);
        
        return numerator/denominator;
    }
    
    public float totalForce(){
        return gravityForce() + frictionForce();
    }
    
    
}
