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

public abstract class Physics {

    protected final float g = 9.806f;
    protected float EPSILON = 1;
    protected static float mu;
    protected ArrayList<PhysicsGameObject> movingObjects = new ArrayList<PhysicsGameObject>();

    public static Physics physics;

    /*
    Updating physics
     */

    public abstract void update(double dt);

    /*
    Collision
     */

    public void dealCollision(PhysicsGameObject obj){
        obj.setPosition(CourseManager.getStartPosition());
        obj.fix(true);

        Gdx.app.log("Message","Ball collided");
    }

    public boolean collided(PhysicsGameObject obj ){

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

    public float equation2Points(float dx, float dy, float xValue, float previousX, float previousY) {
        return (dy/dx) * (xValue -  previousX) + previousY;
    }

    public void addMovableObject(PhysicsGameObject obj) {
        movingObjects.add(obj);
    }

    public static void updateCoefficients() {
        mu = CourseManager.getActiveCourse().getFriction();
    }

    public abstract void updateBall(Ball b,float dt);

    public abstract boolean calculateAcceleration(PhysicsGameObject obj);


}
