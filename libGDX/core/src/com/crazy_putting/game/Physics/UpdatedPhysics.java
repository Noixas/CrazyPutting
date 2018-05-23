
package com.crazy_putting.game.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;
import com.crazy_putting.game.Others.Velocity;

import java.util.ArrayList;

public class UpdatedPhysics {

    private static final float g = 9.806f;
    private static float mu;
    private static float EPSILON = 1;
    private static ArrayList<PhysicsGameObject> movingObjects = new ArrayList<PhysicsGameObject>();

    private static Vector3 curObjectPosition;
    private static Velocity curObjectVelocity;
    private static Vector3 objectAcceleration;


    public static void update(double dt){

        if(!movingObjects.isEmpty()){
            for(int i =0;i<movingObjects.size();i++){
                updateObject(movingObjects.get(i),dt);
            }
        }
    }

    public static void updateBall(Ball ball,double dt){
        updateObject(ball,dt);
    }

    private static void updateObject(PhysicsGameObject obj, double dt){
       // System.out.println("here");
        if(obj.isFixed()){
            System.out.println("Ball is fixed");
            return;
        }

        if (collided(obj)){
            dealCollision(obj);
            return;
        }

        curObjectPosition = obj.getPosition();

        obj.getPreviousPosition().x = curObjectPosition.x;
        obj.getPreviousPosition().y = curObjectPosition.y;

        curObjectVelocity = obj.getVelocity();
        objectAcceleration = calculateAcceleration(obj);

        updateComponents(obj,curObjectPosition,curObjectVelocity,objectAcceleration,dt);

        curObjectPosition = null;
        curObjectVelocity = null;
        objectAcceleration = null;
    }

    private static void updateComponents(PhysicsGameObject obj, Vector3 position, Velocity velocity, Vector3 acceleration,double dt){

        // x(t+h) = x(t) + h*Vx(t) + h^2/2 * Ax;
        // y(t+h) = y(t) + h*Vy(t) + h^2/2 * Ay;
        float newX =  (float) (position.x +(dt * velocity.Vx) + (dt*acceleration.x * dt / 2));
        float newY = (float) (position.y + (dt * velocity.Vy) + ( dt*acceleration.y * dt / 2));

        //v(t+h) = v(t) + h*a
        float newVelX = (float) (velocity.Vx + dt * acceleration.x );
        float newVelY = (float) (velocity.Vy + dt * acceleration.y );

        obj.setPositionX(newX);
        obj.setPositionY(newY);

        obj.setVelocityComponents(newVelX, newVelY);


    }

    private static Vector3 calculateAcceleration(PhysicsGameObject obj){
        Vector3 gravity = gravityForce(obj);
        Vector3 friction = frictionForce(obj);
        return new Vector3(friction.x + gravity.x,friction.y + gravity.y,0);
    }


    private static Vector3 frictionForce(PhysicsGameObject obj){
        float numeratorX = (-mu * g * obj.getVelocity().Vx);
        float numeratorY = (-mu * g * obj.getVelocity().Vy);

        float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);


        return new Vector3(numeratorX/denominator,numeratorY/denominator,0);
    }

    private static Vector3 gravityForce(PhysicsGameObject obj){
        Vector3 partials = partialDerivatives(obj);
        float gx = -partials.x * g ;
        float gy = -partials.y * g ;

        partials.x = gx;
        partials.y = gy;

        return partials;

    }

    private static Vector3 partialDerivatives(PhysicsGameObject obj){
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

    private static void dealCollision(PhysicsGameObject obj){
        obj.setPosition(CourseManager.getStartPosition());
        obj.fix(true);

       // Gdx.app.log("Message","Ball collided");
    }

    private static boolean collided(PhysicsGameObject obj ){

        float xCur = obj.getPosition().x;
        float yCur = obj.getPosition().y;

        float xPrev = obj.getPreviousPosition().x;
        float yPrev = obj.getPreviousPosition().y;

        if(xCur > GraphicsManager.WORLD_WIDTH / 2 || xCur < GraphicsManager.WORLD_WIDTH / 2 * (-1) ||
                yCur > GraphicsManager.WORLD_HEIGHT / 2 || yCur < GraphicsManager.WORLD_HEIGHT / 2 * (-1) ){
           System.out.println("out");
            return true;
        }

        float dx = xCur-xPrev;
        float dy = yCur-yPrev;

        for (int i = 1; i < 4; i++){
            float height = CourseManager.calculateHeight(xPrev + dx / i, equation2Points(dx, dy, xPrev + dx / i, xPrev, yPrev));
            if (height < 0){
                //System.out.println("in the water");
                return true;
            }
        }
        return false;
    }

    private static float equation2Points(float dx, float dy, float xValue, float previousX, float previousY) {
        return (dy/dx) * (xValue -  previousX) + previousY;
    }


    public static void addMovableObject(PhysicsGameObject obj) {
        movingObjects.add(obj);
    }

    public static void updateCoefficients() {
        mu = CourseManager.getActiveCourse().getFriction();
    }


}
