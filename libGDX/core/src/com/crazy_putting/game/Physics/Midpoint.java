
package com.crazy_putting.game.Physics;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;


public class Midpoint extends Physics{

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
        if(obj.isFixed()) return;

        if (super.collided(obj)){
            super.dealCollision(obj);
            return;
        }

        updateComponents(obj,dt);

    }

    /*
    Midpoint
     */

    public void updateComponents(PhysicsGameObject obj, /*float t,*/ double dt) {
        state.update(obj);

        obj.getPreviousPosition().x = state.getX();
        obj.getPreviousPosition().y = state.getY();

        Derivative k1 = derivative(0.0f, state, new Derivative());
        Derivative half_k1 = new Derivative(0.5f*k1.getDx(), 0.5f*k1.getDy(), 0.5f*k1.getDvx(), 0.5f*k1.getDvy());
        Derivative k2 = derivative(dt*0.5f, state, half_k1);

        float dxdt = k2.getDx();
        float dydt = k2.getDy();
        float dvxdt = k2.getDvx();
        float dvydt = k2.getDvy();

        float x = (float) (state.getX() + dxdt*dt);
        float y = (float) (state.getY() + dydt*dt);

        float Vx = (float) (obj.getVelocity().Vx + dvxdt*dt);
        float Vy = (float) (obj.getVelocity().Vy + dvydt*dt);

        obj.setPositionXYZ(x,y);
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
}
