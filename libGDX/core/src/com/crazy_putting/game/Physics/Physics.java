package com.crazy_putting.game.Physics;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;
import com.crazy_putting.game.Others.MultiplayerSettings;

import java.util.ArrayList;

public abstract class Physics {

    protected final float g = 9.806f;
    protected float EPSILON = 1;
    protected static float mu;
    float dt = 0.01666f;

    protected ArrayList<PhysicsGameObject> movingObjects = new ArrayList<PhysicsGameObject>();

    protected State state = new State();

    public static Physics physics = new RK4();

    /*
    Updating physics
     */

    public void update(){
        float dt = 0.01666f;
        if(!movingObjects.isEmpty()){
            for (PhysicsGameObject movingObject : movingObjects) {
                updateObject(movingObject);
            }
        }
    }

    public void updateObject(PhysicsGameObject obj){
        if(obj.isFixed()) return;

        if (collided(obj)){
            dealCollision(obj);
            return;
        }
        updateComponents(obj);
    }

    public abstract void updateComponents(PhysicsGameObject obj);

    /*
    other
     */

    public void addMovableObject(PhysicsGameObject obj) {
        movingObjects.add(obj);
    }

    public void addMovableObject(PhysicsGameObject[] obj) {
        for (int i=0; i<obj.length; i++)
            movingObjects.add(obj[i]);
    }

    public void removeMovableObject(PhysicsGameObject obj){
        movingObjects.remove(obj);
    }

    public static void updateCoefficients() {
        mu = CourseManager.getActiveCourse().getFriction();
    }


    private float equation2Points(float dx, float dy, float xValue, float previousX, float previousY) {
        return (dy/dx) * (xValue -  previousX) + previousY;
    }


    /*
    Collision
     */

    void dealCollision(PhysicsGameObject obj){
        // For multiple players
        if (MultiplayerSettings.PlayerAmount > 1) {
            for (int i = 0; i < movingObjects.size(); i++) {
                // TODO change for mazelike courses
                PhysicsGameObject ball = movingObjects.get(i);
                ball.setPosition(ball.getStartPosition());
                ball.fix(true);
                ball.setVelocity(0.00001f, 0.000001f);
            }
        }
        // For single player
        else {
            // TODO fix for maze-like courses
            obj.setPosition(CourseManager.getStartPosition(0));
            obj.fix(true);
            obj.setVelocity(0.00001f, 0.000001f);
        }
//        Gdx.app.log("Message","Ball collided");

    }

    public boolean collided(PhysicsGameObject obj ){
        state.update(obj);

        float xCur = state.getX();
        float yCur = state.getY();


        float xPrev = obj.getPreviousPosition().x;
        float yPrev = obj.getPreviousPosition().y;


        if(xCur > CourseManager.getCourseDimensions().x/ 2 || xCur < CourseManager.getCourseDimensions().x / 2 * (-1) ||
                yCur > CourseManager.getCourseDimensions().y / 2 || yCur < CourseManager.getCourseDimensions().y / 2 * (-1) ){
            //System.out.println("Out of the world "+xCur+" "+yCur);
            return true;
        }

        float dx = xCur-xPrev;
        float dy = yCur-yPrev;

        if(dx==0||dy==0){
            return false;
        }
        if(CourseManager.getActiveCourse().checkObstaclesAt(new Vector3(xCur,yCur,0)));
        for (int i = 1; i < 5; i++){

            float height = CourseManager.calculateHeight(xPrev + dx / i, equation2Points(dx, dy, xPrev + dx / i, xPrev, yPrev));

            if (height < -1){//Changed to -1 fro 0 by Rodrigo since in splines I use -1 so when we create  a new spline terrain the whole terrain is not water
                //System.out.println("In the water " + height);
                return true;
            }
        }
        return false;
    }


        /*
    Acceleration a = F/m = G + H
     */

    public boolean isGoingToStop(PhysicsGameObject obj) {
        state.update(obj);

        Vector3 gravity = gravityForce(state);
        double grav = Math.sqrt(Math.pow(gravity.x,2)+ Math.pow(gravity.y,2));

        Vector3 friction = frictionForce(state);
        double fric = Math.sqrt(Math.pow(friction.x,2)+ Math.pow(friction.y,2));
        return obj.isSlow()&&fric>grav;
    }

    public Vector3 acceleration(State s){
        return new Vector3(frictionForce(s).x + gravityForce(s).x,frictionForce(s).y + gravityForce(s).y,0);
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
        float x2 =  x1 - EPSILON;
        float yCur = s.getY();
        float slopeScaleCoeff = 1f;
        float partialX = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x2, yCur)) / EPSILON);

        x1-=EPSILON;
        yCur+=EPSILON;
        float y2 = yCur -EPSILON;

        float partialY = ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x1, y2)) / EPSILON);

        return new Vector3(slopeScaleCoeff * partialX,slopeScaleCoeff * partialY,0);

    }

}
