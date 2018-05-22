package com.crazy_putting.game.Physics;

import com.crazy_putting.game.GameObjects.OldGameObject;

/** Physics class for the eqn: Sin(x)+y^2*/

public class PhysicsWithSin {

    private static final double g = 9.81;

    //just create a friction coefficient here for now
    private static final float mu = (float) 0.4;

    /** Calculates the partial derivative of x.
     * @param obj
     * @return double
     */
    private static double partialDerivativeX(OldGameObject obj) {
        return Math.cos(obj.getPosition().x);
    }

    /** Calculates the partial derivative of y.
     * @param obj
     * @return double
     */


    private static double partialDerivativeY(OldGameObject obj) {
        return 2*obj.getPosition().y;
    }


    /** applying the force from user
     * @param someObj, angle, speed
    */
    public static void appliedForce(OldGameObject someObj, float angle, float speed){
        someObj.getVelocity().setAngle(angle);
        someObj.getVelocity().setSpeed(speed);

    }

    /** Updates the coordinates (position) and velocity of the object.
     * @param obj, dt
     */



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

    /**Calculation of the Gravitational Force
     *G = -mgh(,x)
     * @return float
     * @param obj
     */
    private static float gravityForceX(OldGameObject obj) {
        float result = (float) (- obj.getMass() * g * partialDerivativeX(obj));

        return result;
    }
    /**Calculation of the Gravitational Force
     *G = - mgh(,y)
     * @return float
     * @param obj
     */

    private static float gravityForceY(OldGameObject obj) {
        float result = (float) (- obj.getMass() * g * partialDerivativeY(obj));
        return result;
    }

    /**
     *Calculation of the Force of friction
     *H = -(mu)* m* g* v / ||V||
     *V = vx/cos(x)
     * @param obj
     * @return float
     */
    private static float frictionForceX(OldGameObject obj) {

        float numerator = (float) (-mu * obj.getMass() * g * obj.getVelocity().Vx);
        float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);

        return numerator / denominator;
    }

    /**
     *Calculation of the Force of friction
     *H = -(mu)* m* g* v / ||V||
     *V = vy/sin(x)
     * @param obj
     * @return float
     */

    private static float frictionForceY(OldGameObject obj){
        float numerator = (float) (-mu * obj.getMass() * g * obj.getVelocity().Vy);
        float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);

        return numerator / denominator;
    }

    /**
     * Calculates total force acting on object in x direction.
     * @param obj
     * @return float
     */


    public static float totalForceX(OldGameObject obj){
        return gravityForceX(obj) + frictionForceX(obj);
    }

    /**
     * Calculates total force acting on object in y direction.
     * @param obj
     * @return float
     */

    public static float totalForceY(OldGameObject obj){
        return gravityForceY(obj) + frictionForceY(obj);
    }


}