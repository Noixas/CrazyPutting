
package com.crazy_putting.game.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;
import com.crazy_putting.game.Others.Velocity;

import java.util.ArrayList;

public class RKDraft {

    private static final float g = 9.806f;
    private static float mu;
    private static float EPSILON = 1;
    private static ArrayList<PhysicsGameObject> movingObjects = new ArrayList<PhysicsGameObject>();

    private static Vector3 curObjectPosition;
    private static Velocity curObjectVelocity;
    private static Vector2 objectAcceleration;

    /*
    Updating physics
     */

    public static void update(double dt){

        if(!movingObjects.isEmpty()){
            for(int i =0;i<movingObjects.size();i++){
                updateObject(movingObjects.get(i),dt);
            }
        }
    }

    private static void updateObject(PhysicsGameObject obj, double dt){
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
        objectAcceleration = calculateAcceleration(obj);

        updateComponents(obj,curObjectPosition,curObjectVelocity,objectAcceleration,dt);

        curObjectPosition=null;
        curObjectVelocity=null;
        objectAcceleration=null;
    }

    private static void updateComponents(PhysicsGameObject obj, Vector3 position, Velocity velocity, Vector2 acceleration,double dt){
        /*
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
        */

        integral(obj, dt);

    }

    /*
    Acceleration a = F/m = G + H
     */

    private static Vector2 calculateAcceleration(PhysicsGameObject obj){
        Vector2 gravity = gravityForce(obj);
        Vector2 friction = frictionForce(obj);
        return new Vector2(friction.x + gravity.x,friction.y + gravity.y);
    }

    /*
    Calculate H
     */

    private static Vector2 frictionForce(PhysicsGameObject obj){
        float numeratorX = (-mu * g * obj.getVelocity().Vx);
        float numeratorY = (-mu * g * obj.getVelocity().Vy);

        float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);


        return new Vector2(numeratorX/denominator,numeratorY/denominator);
    }

    /*
    Calculate G
     */

    private static Vector2 gravityForce(PhysicsGameObject obj){
        Vector2 partials = partialDerivatives(obj);
        float gx = -partials.x * g ;
        float gy = -partials.y * g ;

        partials.x = gx;
        partials.y = gy;

        return partials;

    }

    /*
    Partial Derivatives
     */

    private static Vector2 partialDerivatives(PhysicsGameObject obj){
        float x1 =  obj.getPosition().x + EPSILON;
        float x2 =  x1 - 2 * EPSILON;
        float yCur = obj.getPosition().y;

        float partialX = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x2, yCur)) / 2 * EPSILON);


        x1-=EPSILON;
        yCur+=EPSILON;
        float y2 = yCur - 2 * EPSILON;

        float partialY = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x1, y2)) / 2 * EPSILON);

        return new Vector2(partialX,partialY);

    }

    /*
    Collision
     */

    private static void dealCollision(PhysicsGameObject obj){
        obj.setPosition(CourseManager.getStartPosition());
        obj.fix(true);

        Gdx.app.log("Message","Ball collided");
    }

    private static boolean collided(PhysicsGameObject obj ){

        float xCur = obj.getPosition().x;
        float yCur = obj.getPosition().y;

        float xPrev = obj.getPreviousPosition().x;
        float yPrev = obj.getPreviousPosition().y;

        if(xCur > GraphicsManager.WORLD_WIDTH / 2 || xCur < GraphicsManager.WORLD_WIDTH / 2 * (-1) ||
                yCur > GraphicsManager.WORLD_HEIGHT / 2 || yCur < GraphicsManager.WORLD_HEIGHT / 2 * (-1) ){

            return true;
        }

        float dx = xCur-xPrev;
        float dy = yCur-yPrev;

        for (int i = 1; i < 4; i++){
            float height = CourseManager.calculateHeight(xPrev + dx / i, equation2Points(dx, dy, xPrev + dx / i, xPrev, yPrev));
            if (height < 0){
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

    /*
    RK4
     */

    private static Derivative derivative(PhysicsGameObject obj, /*float t,*/ double dt, Derivative d) {
        float x = (float) (obj.getPosition().x + d.getDx()*dt);
        float y = (float) (obj.getPosition().y + d.getDy()*dt);

        float Vx = (float) (obj.getVelocity().Vx + d.getDvx()*dt);
        float Vy = (float) (obj.getVelocity().Vy + d.getDvy()*dt);

        obj.setPositionX(x);
        obj.setPositionY(y);
        obj.setVelocityComponents(Vx,Vy);

        float dx = Vx;
        float dy = Vy;

        Vector2 a = calculateAcceleration(obj); // t+dt
        float dvx = a.x;
        float dvy = a.y;

        d.setDx(dx); d.setDy(dy); d.setDvx(dvx); d.setDvy(dvy);
        return d;
    }

    private static void integral(PhysicsGameObject obj, /*float t,*/ double dt) {
        Derivative a = derivative(obj, 0.0f, new Derivative());
        Derivative b = derivative(obj, dt*0.5f, a );
        Derivative c = derivative(obj, dt*0.5f, b );
        Derivative d = derivative(obj, dt, c );

        float dxdt = 1.0f / 6.0f * ( a.getDx() + 2.0f * ( b.getDx() + c.getDx() ) + d.getDx() );
        float dydt = 1.0f / 6.0f * ( a.getDy() + 2.0f * ( b.getDy() + c.getDy() ) + d.getDy() );

        float dvxdt = 1.0f / 6.0f * ( a.getDvx() + 2.0f * ( b.getDvx() + c.getDvx() ) + d.getDvx() );
        float dvydt = 1.0f / 6.0f * ( a.getDvy() + 2.0f * ( b.getDvy() + c.getDvy() ) + d.getDvy() );

        float x = (float) (obj.getPosition().x + dxdt*dt);
        float y = (float) (obj.getPosition().y + dydt*dt);

        float Vx = (float) (obj.getVelocity().Vx + dvxdt*dt);
        float Vy = (float) (obj.getVelocity().Vy + dvydt*dt);

        obj.setPositionX(x);
        obj.setPositionY(y);
        obj.setVelocityComponents(Vx,Vy);
    }
}
