
package com.crazy_putting.game.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;
import com.crazy_putting.game.Others.Velocity;

import java.util.ArrayList;

public class Midpoint extends Physics{

    protected Vector3 curObjectPosition;
    protected Velocity curObjectVelocity;
    protected Vector2 objectAcceleration;
    private State state = new State();

    public Midpoint(){
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

        state.update(obj);
        curObjectVelocity = obj.getVelocity();
        objectAcceleration = acceleration(state);

        updateComponents(obj,curObjectPosition,curObjectVelocity,objectAcceleration,dt);

        curObjectPosition=null;
        curObjectVelocity=null;
        objectAcceleration=null;
    }

    public void updateComponents(PhysicsGameObject obj, Vector3 position, Velocity velocity, Vector2 acceleration,double dt){

        integral(obj, dt);

    }

    /*
    Midpoint
     */

    public Derivative derivative(/*PhysicsGameObject obj, float t,*/ double dt, State s, Derivative d) {
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

    public void integral(PhysicsGameObject obj, /*float t,*/ double dt) {
        State s = state;

        Derivative k1 = derivative(0.0f, s, new Derivative());
        Derivative half_k1 = new Derivative(0.5f*k1.getDx(), 0.5f*k1.getDy(), 0.5f*k1.getDvx(), 0.5f*k1.getDvy());
        Derivative k2 = derivative(dt*0.5f, s, half_k1);

        float dxdt = k2.getDx();
        float dydt = k2.getDy();
        float dvxdt = k2.getDvx();
        float dvydt = k2.getDvy();

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

    public Vector2 acceleration(State s){
        Vector2 gravity = gravityForce(s);
        Vector2 friction = frictionForce(s);
        return new Vector2(friction.x + gravity.x,friction.y + gravity.y);
    }

    /*
    Calculate H
     */

    public Vector2 frictionForce(State s){
        float numeratorX = (-mu * g * s.getVx());
        float numeratorY = (-mu * g * s.getVy());

        float lengthOfVelocityVector = (float) (Math.pow(s.getVx(), 2) + Math.pow(s.getVy(), 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);

        return new Vector2(numeratorX/denominator,numeratorY/denominator);
    }

    /*
    Calculate G
     */

    public Vector2 gravityForce(State s){
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

    public Vector2 partialDerivatives(State s){
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
