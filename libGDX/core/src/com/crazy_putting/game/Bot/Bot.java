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

public class Bot {
    public Ball ball;
    public Hole hole;
    public Course course;
    public boolean ballRolledThroughTheHole;
    public float initialX;
    public float initialY;

    public Bot(Ball ball, Hole hole, Course course){
        this.ball = ball.clone();
        this.initialX = ball.getPosition().x;
        this.initialY = ball.getPosition().y;
        this.hole = hole;
        this.course = course;
    }

    public Velocity computeVelocity(){
        // write logic here
        //if terrain is flat
        double dist = euclideanDistance(ball.getPosition(),course.getGoalPosition());
        Gdx.app.log("Tag", String.valueOf(dist)+" "+ball.getPosition().x+" "+course.getGoalPosition().x);
        float angle = (float) Math.toDegrees(Math.acos(Math.abs(ball.getPosition().x-course.getGoalPosition().x)/dist));
        if(ball.getPosition().x<course.getGoalPosition().x&&ball.getPosition().y<course.getGoalPosition().y){
            angle = angle;
        }
        else if(ball.getPosition().x>course.getGoalPosition().x&&ball.getPosition().y<course.getGoalPosition().y){
            angle = 180-angle;
        }
        else if(ball.getPosition().x>course.getGoalPosition().x&&ball.getPosition().y>course.getGoalPosition().y){
            angle = 180+angle;
        }
        else if(ball.getPosition().x<course.getGoalPosition().x&&ball.getPosition().y>course.getGoalPosition().y){
            angle = 360-angle;
        }
        float speed = computeSpeed(angle);
        Velocity computedVelocity = new Velocity(speed, angle);
        return computedVelocity;

    }

    public double euclideanDistance(Vector2 start, Vector2 goal){
        double dist = (float) Math.sqrt(Math.pow(start.x-goal.x,2)+Math.pow(start.y-goal.y,2));
        return dist;
    }

    public void simulateShot(){
        /*TODO not sure if that's correct (probably not) and also you must specify other conditions when the simulation should end
          (i.e. when the ball is going further and further from the hole?)
        */
        boolean start=true;
        Vector2 initialPosition = new Vector2();
        initialPosition.x = initialX;
        initialPosition.y = initialY;
        ball.setPosition(initialPosition);
        while(ball.isMoving()||start){
            start = false;
            ball.fix(false);

            ball.update(Gdx.graphics.getDeltaTime());
            Physics.update(ball,Gdx.graphics.getDeltaTime());
            if(isBallInTheHole(ball,hole)){
                ballRolledThroughTheHole = true;
                if(ball.isSlow()) {
                    System.out.println("Ball in goal");
                    ball.fix(true);
                    break;
                }
            }
            Gdx.app.log("", String.valueOf(ball.isMoving())+String.valueOf(ballRolledThroughTheHole)+String.valueOf(isBallInTheHole(ball, hole)));
//            System.out.println("Simulate shot "+Gdx.graphics.getDeltaTime()+" "+ball.getPosition().x+" "+ball.getPosition().y);


        }
    }

    public float computeSpeed(float angle){
        float speed = 99;
        // example change rate
        float changeRate = 0.02f;
        System.out.println("computeSpeed");
        ballRolledThroughTheHole = false;
        while(!GameManager.isBallInTheHole(ball,hole)){
            ball.setVelocity(speed,angle);
            simulateShot();
            // TODO check if during the simulation the ball rolled through the whole but was to fast
            if(GameManager.isBallInTheHole(ball,hole)){
                return speed;
            }
            else if(ballRolledThroughTheHole){
                speed -= speed*changeRate;
            }
            else{
                speed += speed*changeRate;
            }
            if(speed<1f){
                System.out.println("Speed to small");
                speed = 100;
                break;
            }
            System.out.println("Current speed"+speed);
        }
        System.out.println("Speed "+speed+" angle "+angle);
        return speed;
    }
    public static boolean isBallInTheHole(Ball ball, Hole hole){
        if(Math.sqrt(Math.pow(ball.getPosition().x -hole.getPosition().x,2) +Math.pow((ball.getPosition().y - hole.getPosition().y),2))< hole.getRadius()){
            return true;
        }
        return false;
    }

}
