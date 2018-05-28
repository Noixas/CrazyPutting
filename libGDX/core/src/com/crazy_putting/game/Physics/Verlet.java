
package com.crazy_putting.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;
import com.crazy_putting.game.Others.Velocity;


public class Verlet extends Physics{

    public Verlet(){
        Physics.physics = this;
    }

    /*
    Updating physics
     */

    public void update(double dt){

        if(!movingObjects.isEmpty()){
            for(int i =0;i<movingObjects.size();i++){
                updateObject(movingObjects.get(i),dt);
            }
        }
    }

    public void updateBall(Ball ball, float dt){
        updateObject(ball,dt);
    }

    public void updateObject(PhysicsGameObject obj, double dt){
        // System.out.println("here");
        if(obj.isFixed()) return;

        if (collided(obj)){
            dealCollision(obj);
            return;
        }

        curObjectPosition = obj.getPosition();

        obj.getPreviousPosition().x = curObjectPosition.x;
        obj.getPreviousPosition().y = curObjectPosition.y;

        curObjectVelocity = obj.getVelocity();
        calculateAcceleration(obj);

        updateComponents(obj,curObjectPosition,curObjectVelocity,objectAcceleration,dt);

        curObjectPosition=null;
        curObjectVelocity=null;
        objectAcceleration=null;
    }

    public void updateComponents(PhysicsGameObject obj, Vector3 position, Velocity velocity, Vector3 acceleration, double dt){
        // x(t+h) = x(t) + h*Vx(t) + h^2/2 * Ax;
        // y(t+h) = y(t) + h*Vy(t) + h^2/2 * Ay;
        float newX =  (float) (position.x +(dt * velocity.Vx) + (dt*acceleration.x * dt / 2));
        float newY = (float) (position.y + (dt * velocity.Vy) + ( dt*acceleration.y * dt / 2));

        //v(t+h) = v(t) + h*a
        float newVelX = (float) (velocity.Vx + dt * acceleration.x );
        float newVelY = (float) (velocity.Vy + dt * acceleration.y );

        obj.setPositionX(newX);
        obj.setPositionY(newY);
        obj.updateHeight();
        obj.setVelocityComponents(newVelX, newVelY);


    }

    public boolean calculateAcceleration(PhysicsGameObject obj){
            Vector3 gravity = gravityForce(obj);
            double grav = Math.sqrt(Math.pow(gravity.x,2)+ Math.pow(gravity.y,2));

            Vector3 friction = frictionForce(obj);
            double fric = Math.sqrt(Math.pow(friction.x,2)+ Math.pow(friction.y,2));

            objectAcceleration = new Vector3(friction.x + gravity.x,friction.y + gravity.y,0);

            if(!obj.isMoving() && fric>grav){
                return false;
            }
            return true;
        }



    public Vector3 frictionForce(PhysicsGameObject obj){
        float numeratorX = (-mu * g * obj.getVelocity().Vx);
        float numeratorY = (-mu * g * obj.getVelocity().Vy);

        float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);


        return new Vector3(numeratorX/denominator,numeratorY/denominator,0);
    }

    public Vector3 gravityForce(PhysicsGameObject obj){
        Vector3 partials = partialDerivatives(obj);
        float gx = -partials.x * g ;
        float gy = -partials.y * g ;

        partials.x = gx;
        partials.y = gy;

        return partials;

    }

    public Vector3 partialDerivatives(PhysicsGameObject obj){
        float x1 =  obj.getPosition().x + EPSILON;
        float x2 =  x1 - 2 * EPSILON;
        float yCur = obj.getPosition().y;

        float partialX = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x2, yCur)) / 2 * EPSILON);


        x1-=EPSILON;
        yCur+=EPSILON;
        float y2 = yCur - 2 * EPSILON;

        float partialY = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x1, y2)) / 2 * EPSILON);

        return new Vector3(partialX,partialY,0);

    }


}
