
package com.crazy_putting.game.Physics;

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


        public void updateObject(PhysicsGameObject obj, double dt){
            // System.out.println("here");
            if(obj.isFixed()) return;

            if (collided(obj)){
                dealCollision(obj);
                return;
            }

            updateComponents(obj, dt);

        }

    public void updateComponents(PhysicsGameObject obj, double dt){

        state.update(obj);

        Vector3 a = acceleration(state);

        // x(t+h) = x(t) + h*Vx(t) + h^2/2 * Ax;
        // y(t+h) = y(t) + h*Vy(t) + h^2/2 * Ay;
        float newX = (float) ( state.getX() + (dt * state.getVx()) + (dt*a.x * dt / 2) );
        float newY = (float) ( state.getY() + (dt * state.getVy()) + ( dt*a.y * dt / 2) );

        //v(t+h) = v(t) + h*a
        float newVelX = (float) (state.getVx() + dt * a.x );
        float newVelY = (float) (state.getVy() + dt * a.y );

        obj.setPositionX(newX);
        obj.setPositionY(newY);
        obj.updateHeight();
        obj.setVelocityComponents(newVelX, newVelY);

    }


}
