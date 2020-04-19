
package com.crazy_putting.game.Physics;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;

public class Heuns3 extends Physics{

    public Heuns3(){
        Physics.physics = this;
    }

    /*
    RK4
     */

    public void updateComponents(PhysicsGameObject obj) {
        state.update(obj);

        obj.getPreviousPosition().x = state.getX();
        obj.getPreviousPosition().y = state.getY();

        Derivative k1 = derivative(0.0f, state, new Derivative());
        Derivative half_k1 = new Derivative((1.0f/3.0f)*k1.getDx(), (1.0f/3.0f)*k1.getDy(), (1.0f/3.0f)*k1.getDvx(), (1.0f/3.0f)*k1.getDvy());
        Derivative k2 = derivative(dt*(1.0f/3.0f), state, half_k1);
        Derivative half_k2 = new Derivative((2.0f/3.0f)*k2.getDx(), (2.0f/3.0f)*k2.getDy(), (2.0f/3.0f)*k2.getDvx(), (2.0f/3.0f)*k2.getDvy());
        Derivative k3 = derivative(dt*(2.0f/3.0f), state, half_k2);

        float dxdt = 1.0f / 4.0f * ( k1.getDx() + 3.0f*k3.getDx() );
        float dydt = 1.0f / 4.0f * ( k1.getDy() + 3.0f*k3.getDy() );

        float dvxdt = 1.0f / 4.0f * ( k1.getDvx() + 3.0f*k3.getDvx() );
        float dvydt = 1.0f / 4.0f * ( k1.getDvy() + 3.0f*k3.getDvy() );

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
