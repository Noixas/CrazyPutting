
package com.crazy_putting.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;
import com.crazy_putting.game.Others.Velocity;

public class RK4 extends Physics{

    private State state = new State();

    public RK4(){
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

    @Override
    public void updateBall(Ball ball, float dt){
        updateObject(ball,dt);
    }

    public void updateObject(PhysicsGameObject obj, double dt){
        // System.out.println("here");
        if(obj.isFixed()) return;

        if (super.collided(obj)){
            super.dealCollision(obj);
            return;
        }

        curObjectPosition = obj.getPosition();

        obj.getPreviousPosition().x = curObjectPosition.x;
        obj.getPreviousPosition().y = curObjectPosition.y;

        state.update(obj);
        curObjectVelocity = obj.getVelocity();
        calculateAcceleration(obj);

        updateComponents(obj,curObjectPosition,curObjectVelocity,objectAcceleration,dt);

        curObjectPosition=null;
        curObjectVelocity=null;
        objectAcceleration=null;
    }

    public void updateComponents(PhysicsGameObject obj, Vector3 position, Velocity velocity, Vector3 acceleration, double dt){

        integral(obj, dt);

    }

    /*
    RK4
     */

    public void integral(PhysicsGameObject obj, /*float t,*/ double dt) {
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
        obj.updateHeight();
        obj.setVelocityComponents(Vx,Vy);
    }

    public Derivative derivative(/*PhysicsGameObject obj, float t,*/ double dt, State s, Derivative d) {
        State s_new = new State();
        s_new.setX( (float) (s.getX() + d.getDx()*dt) );
        s_new.setY( (float) (s.getY() + d.getDy()*dt) );
        s_new.setVx( (float) (s.getVx() + d.getDvx()*dt) );
        s_new.setVy( (float) (s.getVy() + d.getDvy()*dt) );

        float dx = s_new.getVx();
        float dy = s_new.getVy();
        Vector3 a = acceleration(s_new); // t+dt
        float dvx = a.x;
        float dvy = a.y;

        Derivative d_new = new Derivative();
        d_new.setDx(dx); d_new.setDy(dy); d_new.setDvx(dvx); d_new.setDvy(dvy);
        return d_new;
    }

    /*
    Acceleration a = F/m = G + H
     */

    @Override
    public boolean calculateAcceleration(PhysicsGameObject obj) {
        Vector3 gravity = gravityForce(state);
        double grav = Math.sqrt(Math.pow(gravity.x,2)+ Math.pow(gravity.y,2));

        Vector3 friction = frictionForce(state);
        double fric = Math.sqrt(Math.pow(friction.x,2)+ Math.pow(friction.y,2));

        objectAcceleration = new Vector3(friction.x + gravity.x,friction.y + gravity.y,0);

        if(!obj.isMoving() && fric>grav){
            return false;
        }
        return true;
    }

    public Vector3 acceleration(State s){
        Vector3 gravity = gravityForce(s);
        Vector3 friction = frictionForce(s);
        return new Vector3(friction.x + gravity.x,friction.y + gravity.y,0);
    }

    /*
    Calculate H
     */

    public Vector3 frictionForce(State s){
        float numeratorX = (-mu * g * s.getVx());
        float numeratorY = (-mu * g * s.getVy());

        float lengthOfVelocityVector = (float) (Math.pow(s.getVx(), 2) + Math.pow(s.getVy(), 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);

        return new Vector3(numeratorX/denominator,numeratorY/denominator,0);
    }

    /*
    Calculate G
     */

    public Vector3 gravityForce(State s){
        Vector3 partials = partialDerivatives(s);
        float gx = -partials.x * g ;
        float gy = -partials.y * g ;

        partials.x = gx;
        partials.y = gy;

        return partials;

    }

    /*
    Partial Derivatives
     */

    public Vector3 partialDerivatives(State s){
        float x1 =  s.getX() + EPSILON;
        float x2 =  x1 - 2 * EPSILON;
        float yCur = s.getY();

        float partialX = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x2, yCur)) / 2 * EPSILON);

        x1-=EPSILON;
        yCur+=EPSILON;
        float y2 = yCur - 2 * EPSILON;

        float partialY = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x1, y2)) / 2 * EPSILON);

        return new Vector3(partialX,partialY,0);

    }
}
