package com.crazy_putting.game.Physics;

import com.crazy_putting.game.GameObjects.OldGameObject;

public class PhysicsTest {
    
    private static final double g = 9.81;

    //just create a friction coefficient here for now
    private static final float mu = (float) 0.4;


    //list of all moving objects
    //private ArrayList<OldGameObject> movingThings = new ArrayList<OldGameObject>();
    
    

    //the height equation 
    // H = 0.01*X + 0.03*X^2 + 0.2*Y
    private static double partialDerivativeX(OldGameObject obj) {
        return 0.1 + 0.06 * obj.getPosition().x;
    }

    private static double partialDerivativeY(OldGameObject obj) {
        return 0.2;
    }
    
    
    /*
    applying the force from user 
    */
    public static void appliedForce(OldGameObject someObj, float angle, float speed){
        someObj.getVelocity().setAngle(angle);
        someObj.getVelocity().setSpeed(speed);
        
    }
    
    
    
    public static void update(OldGameObject obj, double dt){

        float x = obj.getPosition().x;
        float y = obj.getPosition().y;
        
        
        //calculation of a new X position 
        // x(t + h) = x(t) +hVx(t);
        float newX = (float) (x + (dt * obj.getVelocity().Vx));
        
        //calculation of a new Y position
        // y(t+h) = y(t) + hVy(t);
        float newY = (float) (y + (dt * obj.getVelocity().Vy));
        
        //calculation of a new total velocity of the ball
        // v(t+h) = v(t) + h*F(x,y,vx,vy)/m
         float newSpeedX = (float) (obj.getVelocity().Vx + dt * totalForceX(obj) / obj.getMass());
         float newSpeedY = (float) (obj.getVelocity().Vy + dt * totalForceY(obj) / obj.getMass());

        obj.getVelocity().Vx = newSpeedX;
        obj.getVelocity().Vy = newSpeedY;
       // System.out.println("X: "+newSpeedX);
        obj.setPositionX(newX);
        obj.setPositionY(newY);

    }
    
    /*
    total power that affects the ball is
    F = G + H;
    */

    //Calculation of the Gravitational Force
    //G = -mgh(,x) - mgh(,y)
    private static float gravityForceX(OldGameObject obj) {
        float result = (float) (- obj.getMass() * g * partialDerivativeX(obj));

        return result;
    }

    private static float gravityForceY(OldGameObject obj) {
        float result = (float) (- obj.getMass() * g * partialDerivativeY(obj));
        return result;
    }

    /*
    Calculation of the Force of friction
    H = -(mu)* m* v / ||V||
    V = vx/cos(x)
  */
    private static float frictionForceX(OldGameObject obj) {

        float numerator = (float) (-mu * obj.getMass() * g * obj.getVelocity().Vx);
        float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);

        return numerator / denominator;
    }

    private static float frictionForceY(OldGameObject obj){
        float numerator = (float) (-mu * obj.getMass() * g * obj.getVelocity().Vy);
        float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);

        return numerator / denominator;
    }


    public static float totalForceX(OldGameObject obj){
        return gravityForceX(obj) + frictionForceX(obj);
    }

    public static float totalForceY(OldGameObject obj){
        return gravityForceY(obj) + frictionForceY(obj);
    }
    
    
}
