package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.GameLogic.GameManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Others.Velocity;
import com.crazy_putting.game.Physics.Physics;
import com.sun.deploy.config.VerboseDefaultConfig;

import javax.sound.sampled.Line;

public class Bot {
    public Ball ball;
    private Hole hole;
    private Course course;
    private boolean ballRolledThroughTheHole;
    private float initialX;
    private float initialY;
    private float bestSpeed;
    private float closestDistToHole;
    // TODO zrob prywatne instance variables dla obu linii
    private LinearFunction lineGoal;
    private LinearFunction lineStartGoal;


    public Bot(Ball ball, Hole hole, Course course){
        this.ball = ball.clone();
        this.initialX = ball.getPosition().x;
        this.initialY = ball.getPosition().y;
        this.hole = hole;
        this.course = course;
        this.bestSpeed = 0;
        this.closestDistToHole = (float) euclideanDistance(ball.getPosition(),course.getGoalPosition());
        this.lineStartGoal = lineStartGoal();
        Gdx.app.log("Log",lineStartGoal.getA()+" "+lineStartGoal.getB());
        this.lineGoal = lineGoal(lineStartGoal);
        Gdx.app.log("Log",lineGoal.getA()+" "+lineGoal.getB());

    }

    /**
     * Compute velocity(speed and angle) for a flat terrain to score a hole-in-one.
     */
    public Velocity computeOptimalVelocity(){
        float angle = 0;
        float speed = 0;
        Velocity newVelocity = new Velocity(angle,speed);
        if(canBallStopInTheHole()){
            ball.setPosition(new Vector2(initialX, initialY));
            angle = computeInitialAngle();
            newVelocity = computeVelocity(angle);
            Gdx.app.log("Log - computed velocity","Speed "+String.valueOf(newVelocity.getSpeed())+" Angle "+String.valueOf(newVelocity.getAngle()));
        }
        return newVelocity;
    }

    private boolean canBallStopInTheHole()  {
//        Ball newBall = ball.clone();
//        newBall.setPosition(new Vector2(hole.getPosition().x,hole.getPosition().y));
//        if(Physics.gravityForceX(newBall)<Physics.frictionForceX(newBall)&&Physics.gravityForceY(newBall)<Physics.frictionForceY(newBall)){
//            return true;
//        }a
        float[][] checkedPositions ={{hole.getPosition().x,hole.getPosition().y},{hole.getPosition().x,hole.getPosition().y-hole.getRadius()},
                {hole.getPosition().x,hole.getPosition().y+hole.getRadius()},{hole.getPosition().x-hole.getRadius(),hole.getPosition().y},
                {hole.getPosition().x+hole.getRadius(),hole.getPosition().y}};
        for (float[] position:checkedPositions){
            float anyAngle = 0;
            float tinySpeed = 0.0001f;
            float speedTolerance = 0.00005f;
            ball.setVelocity(tinySpeed, anyAngle);
            ball.setPosition(new Vector2(position[0],position[1]));
            simulateShot(tinySpeed, speedTolerance);
            if(GameManager.isBallInTheHole(ball,hole)){
                Gdx.app.log("Log","Ball can stop in the hole");
                return true;
            }
        }
        Gdx.app.log("Log","Ball can't stop in the hole");
        ballRolledThroughTheHole = false;
        return false;




    }

    public float computeInitialAngle(){
        double dist = euclideanDistance(ball.getPosition(),course.getGoalPosition());
        float initialAngle = (float) Math.toDegrees(Math.acos(Math.abs(ball.getPosition().x-course.getGoalPosition().x)/dist));
        float angle=0;
        if(ball.getPosition().x<course.getGoalPosition().x&&ball.getPosition().y<course.getGoalPosition().y){
            angle = initialAngle;
        }
        else if(ball.getPosition().x>course.getGoalPosition().x&&ball.getPosition().y<course.getGoalPosition().y){
            angle = 180-initialAngle;
        }
        else if(ball.getPosition().x>course.getGoalPosition().x&&ball.getPosition().y>course.getGoalPosition().y){
            angle = 180+initialAngle;
        }
        else if(ball.getPosition().x<course.getGoalPosition().x&&ball.getPosition().y>course.getGoalPosition().y){
            angle = 360-initialAngle;
        }
        return angle;
    }

    public double euclideanDistance(Vector2 start, Vector2 goal){
        double dist = (float) Math.sqrt(Math.pow(start.x-goal.x,2)+Math.pow(start.y-goal.y,2));
        return dist;
    }

    public Velocity computeVelocity(float angle){
        // Initial speed, maybe it would be better to replace it with a random float
        float speed = 100;
        float speedRate = 0.1f;
        float angleRate = 0.1f;
        // true if ball rolled through the hole, but didn't stop there
        ballRolledThroughTheHole = false;

        this.bestSpeed = 0;
        this.closestDistToHole = (float) euclideanDistance(ball.getPosition(),course.getGoalPosition());
        while(!GameManager.isBallInTheHole(ball,hole)){
            ball.setVelocity(speed,angle);
            Gdx.app.log("Debug","speed: "+speed+" angle: "+angle);
            Vector2 initialPosition = new Vector2();
            initialPosition.x = initialX;
            initialPosition.y = initialY;
            ball.setPosition(initialPosition);
            simulateShot(speed,0.5f);
            if(GameManager.isBallInTheHole(ball,hole)){
                return new Velocity(speed, angle);
            }
            // if the speed was too big
            else if(ballRolledThroughTheHole){
                Gdx.app.log("Log","Ball rolled through the hole");
                speed -= speed*speedRate;
            }
            else{
                if(ballPassedByHole()){
                    if(leftRight()==Direction.LEFT){
                        angle -= angleRate*angle;
                    }
                    else{
                        angle += angleRate*angle;
                    }
                    speed -= speed*speedRate;
                }
                else{
                    speed +=speed*speedRate;
                }

//                TODO not sure how to deal with that yet
            }
            // The speed is negligible what means that the bot can't find an optimal velocity
            if(speed<1f){
                Gdx.app.log("Log","The bot can't find an optimal velocity");
                speed = bestSpeed;
                break;
            }
            Gdx.app.log("Log","Current speed "+String.valueOf(speed));
            ballRolledThroughTheHole = false;
        }
        return new Velocity(speed, angle);
    }



    public void simulateShot(float speed, float speedTolerance){
        //TODO specify other conditions when the simulation should end
        //(i.e. when the ball is going further and further from the hole?)


        // At first ball is never moving, so without additional premise firstIteration the loop would never start
        boolean firstIteration=true;
        // After each simulation the ball should get its initial position (since we want to restart the shot from the
        // beginning with different speed
        float newClosestDistToHole = (float) euclideanDistance(ball.getPosition(),course.getGoalPosition());
        while(ball.isMoving(speedTolerance)||firstIteration){
            firstIteration = false;
            ball.fix(false);
            ball.update(Gdx.graphics.getDeltaTime());
            Physics.update(ball,Gdx.graphics.getDeltaTime());
            //&&euclideanDistance(ball.getPosition(),hole.getPosition())>hole.getRadius()
            // TODO don't think that work for a general case
            if(ballPassedByHole()){
                Gdx.app.log("Log","Ball reached goal line");
                break;
            }
            if(newClosestDistToHole>(float)euclideanDistance(ball.getPosition(),course.getGoalPosition())){
                newClosestDistToHole = (float) euclideanDistance(ball.getPosition(),course.getGoalPosition());
            }
            if(GameManager.isBallInTheHole(ball,hole)){
                ballRolledThroughTheHole = true;
                if(ball.isSlow()) {
                    System.out.println("Ball in goal "+ball.getVelocity().getSpeed()+" angle "+ball.getVelocity().getAngle());
                    ball.fix(true);
                    break;
                }
            }
            // this is a quick and dirty way to check if the ball collided
            if(ball.isFixed()){
                break;
            }
        }
        if(newClosestDistToHole<closestDistToHole){
            closestDistToHole = newClosestDistToHole;
            bestSpeed = speed;
        }
        Gdx.app.log("Log","Simulation completed");
    }

    public boolean ballPassedByHole(){
        return lineGoal.calculate(ball.getPosition().x)<ball.getPosition().y&&euclideanDistance(ball.getPosition(),hole.getPosition())>hole.getRadius();
    }

    public static boolean isBallInTheHole(Ball ball, Hole hole){
        if(Math.sqrt(Math.pow(ball.getPosition().x -hole.getPosition().x,2) +Math.pow((ball.getPosition().y - hole.getPosition().y),2))< hole.getRadius()){
            return true;
        }
        return false;
    }

    public LinearFunction lineStartGoal(){
        float a = (ball.getPosition().y-hole.getPosition().y)/(ball.getPosition().x-hole.getPosition().x);
        float b = ball.getPosition().y-a*ball.getPosition().x;
        return new LinearFunction(a,b);
    }

    public LinearFunction lineGoal(LinearFunction line){
        float a = -1/line.getA();
        float b = hole.getPosition().y-a*hole.getPosition().x;
        return  new LinearFunction(a,b);
    }

    /**
     * Checks in 2D if ball is above goal or below goal
     */
    public Direction ballPosition(){
        assert(lineGoal.intersects(ball.getPosition()));
        if(ball.getPosition().y>hole.getPosition().y){
            return Direction.ABOVE;
        }
        else{
            return Direction.BELOW;
        }
    }

    /**
     * Checks if the ball is on the left or right side of the start-goal line
     */
    // TODO change above, below, left, right to an enum
    public Direction leftRight(){
        if(ball.getPosition().x<hole.getPosition().x){
            if(ballPosition()==Direction.ABOVE){
                return Direction.LEFT;
            }
            else{
                return Direction.RIGHT;
            }
        }
        else{
            if(ballPosition()==Direction.BELOW){
                return Direction.LEFT;
            }
            else{
                return Direction.RIGHT;
            }
        }
    }
}
