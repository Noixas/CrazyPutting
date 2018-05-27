
package com.crazy_putting.game.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;
import com.crazy_putting.game.Others.Velocity;

import java.util.ArrayList;

public class UpdatedPhysics3 {

    private static final float g = 9.806f;
    private static float mu;
    private static float EPSILON = 1;
    private static ArrayList<PhysicsGameObject> movingObjects = new ArrayList<PhysicsGameObject>();

    private static Vector3 curObjectPosition;
    private static Velocity curObjectVelocity;
    private static Vector2 objectAcceleration;
    private static State state = new State();

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

        state.update(obj);
        curObjectVelocity = obj.getVelocity();
        objectAcceleration = acceleration(state);

        updateComponents(obj,curObjectPosition,curObjectVelocity,objectAcceleration,dt);

        curObjectPosition=null;
        curObjectVelocity=null;
        objectAcceleration=null;
    }

    private static void updateComponents(PhysicsGameObject obj, Vector3 position, Velocity velocity, Vector2 acceleration,double dt){

        integral(obj, dt);

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

    private static Derivative derivative(/*PhysicsGameObject obj, float t,*/ double dt, State s, Derivative d) {
        State s_new = new State();
        s_new.setX( (float) (s.getX() + d.getDx()*dt) );
        s_new.setY( (float) (s.getY() + d.getDy()*dt) );
        s_new.setVx( (float) (s.getVx() + d.getDvx()*dt) );
        s_new.setVy( (float) (s.getVy() + d.getDvy()*dt) );

        float dx = s_new.getVx();
        float dy = s_new.getVy();
        Vector2 a = acceleration(s_new); // t+dt
        float dvx = a.x;
        float dvy = a.y;

        Derivative d_new = new Derivative();
        d_new.setDx(dx); d_new.setDy(dy); d_new.setDvx(dvx); d_new.setDvy(dvy);
        return d_new;
    }

    private static void integral(PhysicsGameObject obj, /*float t,*/ double dt) {
        State s = state;

        Derivative k1 = derivative(0.0f, s, new Derivative());
        Derivative half_k1 = new Derivative(0.5f*k1.getDx(), 0.5f*k1.getDy(), 0.5f*k1.getDvx(), 0.5f*k1.getDvy());
        Derivative k2 = derivative(dt*0.5f, s, half_k1);
        Derivative half_k2 = new Derivative(0.5f*k2.getDx(), 0.5f*k2.getDy(), 0.5f*k2.getDvx(), 0.5f*k2.getDvy());
        Derivative k3 = derivative(dt*0.5f, s, half_k2);
        Derivative k4 = derivative(dt, s, k3);

        float dxdt = 1.0f / 6.0f * ( k1.getDx() + 2.0f*k2.getDx() + 2.0f*k3.getDx() + k4.getDx() );
        float dydt = 1.0f / 6.0f * ( k1.getDy() + 2.0f*k2.getDy() + 2.0f*k3.getDy() + k4.getDy() );

        float dvxdt = 1.0f / 6.0f * ( k1.getDvx() + 2.0f*k2.getDvx() + 2.0f*k3.getDvx() + k4.getDvx() );
        float dvydt = 1.0f / 6.0f * ( k1.getDvy() + 2.0f*k2.getDvy() + 2.0f*k3.getDvy() + k4.getDvy() );

        float x = (float) (s.getX() + dxdt*dt);
        float y = (float) (s.getY() + dydt*dt);

        float Vx = (float) (obj.getVelocity().Vx + dvxdt*dt);
        float Vy = (float) (obj.getVelocity().Vy + dvydt*dt);

        obj.setPositionX(x);
        obj.setPositionY(y);
        obj.setVelocityComponents(Vx,Vy);
    }

    /*
    Acceleration a = F/m = G + H
     */

    private static Vector2 acceleration(State s){
        Vector2 gravity = gravityForce(s);
        Vector2 friction = frictionForce(s);
        return new Vector2(friction.x + gravity.x,friction.y + gravity.y);
    }

    /*
    Calculate H
     */

    private static Vector2 frictionForce(State s){
        float numeratorX = (-mu * g * s.getVx());
        float numeratorY = (-mu * g * s.getVy());

        float lengthOfVelocityVector = (float) (Math.pow(s.getVx(), 2) + Math.pow(s.getVy(), 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);

        return new Vector2(numeratorX/denominator,numeratorY/denominator);
    }

    /*
    Calculate G
     */

    private static Vector2 gravityForce(State s){
        Vector2 partials = partialDerivatives(s);
        float gx = -partials.x * g ;
        float gy = -partials.y * g ;

        partials.x = gx;
        partials.y = gy;

        return partials;

    }

    /*
    Partial Derivatives
     */

    private static Vector2 partialDerivatives(State s){
        float x1 =  s.getX() + EPSILON;
        float x2 =  x1 - 2 * EPSILON;
        float yCur = s.getY();

        float partialX = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x2, yCur)) / 2 * EPSILON);

        x1-=EPSILON;
        yCur+=EPSILON;
        float y2 = yCur - 2 * EPSILON;

        float partialY = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x1, y2)) / 2 * EPSILON);

        return new Vector2(partialX,partialY);

    }
}
