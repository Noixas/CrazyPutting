package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GameManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Others.Velocity;
import com.crazy_putting.game.Physics.Physics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/* TODO
    -save a heightmap as a texture/image file after computing it to reuse next time you choose this course and add to
    courses.txt the name of that file (number of the course ex. 1.jpg)
 */
public class Bot {
    // TODO finish the bot
    public Ball ball;
    private Hole hole;
    private Course course;
    private boolean ballRolledThroughTheHole;
    private float initialX;
    private float initialY;
    private float bestSpeed;
    private float closestDistToHole;
    private LinearFunction lineGoal;
    private LinearFunction lineStartGoal;
    private State currentState;
    private State previousState;
    private Velocity previousVelocity;
    private ArrayList<Node> path;


    public Bot(Ball ball, Hole hole, Course course){
        this.ball = ball.clone();
        Physics.physics.addMovableObject(this.ball);
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

//        int startX = (int)CourseManager.getStartPosition().x;
//        int startY = (int)CourseManager.getStartPosition().y;
//        int goalX = (int)CourseManager.getGoalStartPosition().x;
//        int goalY = (int)CourseManager.getGoalStartPosition().y;
//        int xAbsDiff = Math.abs(startX-goalX);
//        int yAbsDiff = Math.abs(startY-goalY);
//        //System.out.println("X DIFFERENCE: " + xAbsDiff + " Y DIFFERENCE: " + yAbsDiff);
//
//        Map<Node> nodeMap = new Map<Node>(xAbsDiff + 1, yAbsDiff + 1, new ExampleFactory());
//
//
//        if(startX > goalX) {
//            if(startY > goalY){
//                path = (ArrayList<Node>)nodeMap.findPath( xAbsDiff , yAbsDiff , 0, 0); // START X > GOAL X          START Y > GOAL Y
//            }
//            else path = (ArrayList<Node>)nodeMap.findPath(xAbsDiff , 0, 0, yAbsDiff ); // START X > GOAL X           START Y < GOAL Y
//        }
//        else if(startY < goalY){
//            path = (ArrayList<Node>)nodeMap.findPath(0, 0, xAbsDiff , yAbsDiff );      // START X < GOAL X            START Y < GOAL Y
//        }
//        else
//            path = (ArrayList<Node>)nodeMap.findPath(0, yAbsDiff , xAbsDiff , 0);     // START X < GOAL X            START Y > GOAL Y
//
//        System.out.println("START");
//        System.out.print(" (" + startX + ", " + startY + ") -> ");
//        for (int i = 0; i < path.size(); i++) {
//            int nodeCoordinateX = path.get(i).getxPosition();
//            int nodeCoordinateY = path.get(i).getyPosition();
//
//            if(startX > goalX) {
//                // START X > GOAL X
//                // START Y > GOAL Y
//                if(startY > goalY){
//                    nodeCoordinateX += goalX;
//                    nodeCoordinateY += goalY;
//
//                }
//                // START X > GOAL X
//                // START Y < GOAL Y
//                else {
//                    nodeCoordinateX += goalX;
//                    nodeCoordinateY += startY;
//                }
//            }
//            // START X < GOAL X
//            // START Y < GOAL Y
//            else if(startY < goalY){
//                nodeCoordinateX += startX;
//                nodeCoordinateY += startY;
//            }
//            // START X < GOAL X
//            // START Y > GOAL Y
//            else {
//                nodeCoordinateX += startX;
//                nodeCoordinateY += goalY;
//            }
//            path.get(i).setCoordinates(nodeCoordinateX, nodeCoordinateY);
//            if(i!=path.size()-1) System.out.print("(" + path.get(i).getxPosition() + ", " + path.get(i).getyPosition() + ") -> ");
//            else System.out.println("(" + path.get(i).getxPosition() + ", " + path.get(i).getyPosition() + ")");
//
//
//        }
//        System.out.println("DONE");


    }

    /**
     * Compute velocity(speed and angle) for a flat terrain to score a hole-in-one.
     */
    public Velocity computeOptimalVelocity(){
        float angle = 0;
        float speed = 0;
        Velocity newVelocity = new Velocity(angle,speed);
        if(canBallStopInTheHole()){
            ball.setPosition(new Vector3(initialX, initialY,0));
            angle = computeInitialAngle();
            // only for testing
//            angle = 340;
            newVelocity = computeVelocity(angle);
            Gdx.app.log("Log - computed velocity","Speed "+String.valueOf(newVelocity.getSpeed())+" Angle "+String.valueOf(newVelocity.getAngle()));
        }
        return newVelocity;
    }

    private boolean canBallStopInTheHole()  {
//        Ball newBall = ball.clone();
//        newBall.setPosition(new Vector3(hole.getPosition().x,hole.getPosition().y));
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
            ball.setPosition(new Vector3(position[0],position[1],0));
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
//        Node node = path.get(4);
//        Vector3 nodePosition = new Vector3(node.getxPosition(),node.getyPosition(),0);
//        double dist = euclideanDistance(ball.getPosition(),nodePosition);
        double dist = euclideanDistance(ball.getPosition(),hole.getPosition());
        float initialAngle = (float) Math.toDegrees(Math.acos(Math.abs(ball.getPosition().x-hole.getPosition().x)/dist));
        float angle=0;
        if(ball.getPosition().x<hole.getPosition().x&&ball.getPosition().y<hole.getPosition().y){
            angle = initialAngle;
        }
        else if(ball.getPosition().x>hole.getPosition().x&&ball.getPosition().y<hole.getPosition().y){
            angle = 180-initialAngle;
        }
        else if(ball.getPosition().x>hole.getPosition().x&&ball.getPosition().y>hole.getPosition().y){
            angle = 180+initialAngle;
        }
        else if(ball.getPosition().x<hole.getPosition().x&&ball.getPosition().y>hole.getPosition().y){
            angle = 360-initialAngle;
        }
        return angle;
    }

//    public double euclideanDistance2D(Vector3 start, Vector3 goal){
//        double dist = (float) Math.sqrt(Math.pow(start.x-goal.x,2)+Math.pow(start.y-goal.y,2));
//        return dist;
//    }
//    public double euclideanDistance(Vector3 start, Vector3 goal){
//        double dist = (float) Math.sqrt(Math.pow(start.x-goal.x,2)+Math.pow(start.z-goal.y,2));
//        return dist;
//    }

    public double euclideanDistance(Vector3 start, Vector3 goal){
        double dist = (float) Math.sqrt(Math.pow(start.x-goal.x,2)+Math.pow(start.y-goal.y,2)+Math.pow(start.z-goal.z,2));
        return dist;
    }

    public Velocity computeVelocity(float angle){
        // Initial speed, maybe it would be better to replace it with a random float
        // 100 works for course 7
        float speed = 80;
        // true if ball rolled through the hole, but didn't stop there
        ballRolledThroughTheHole = false;
        float speedRate = 0.01f;
        float angleRate = 0.01f;
        this.bestSpeed = 0;
        this.closestDistToHole = (float) euclideanDistance(ball.getPosition(),course.getGoalPosition());
        ArrayList<Ball> balls = new ArrayList<Ball>();
        for(int i=0;i<8;i++){
        // should be closest distance to hole for each simulation
//            if(closestDistToHole>10f){
//                speedRate = 0.1f*(float)(closestDistToHole/euclideanDistance(new Vector2(initialX,initialY),hole.getPosition()));
//                angleRate = 0.1f*(float)(closestDistToHole/euclideanDistance(new Vector2(initialX,initialY),hole.getPosition()));
//            }
//            Gdx.app.log("rates",speedRate+" "+angleRate);
            ball.setVelocity(speed,angle);
            ball.setVelocityGA(speed,angle);
            Gdx.app.log("Start loop","speed: "+speed+" angle: "+angle);
            Vector3 initialPosition = new Vector3();
            initialPosition.x = initialX;
            initialPosition.y = initialY;
            ball.setPosition(initialPosition);
            simulateShot(speed,0.5f);

            // TODO where to put previousVelocity and condition?
            if(GameManager.isBallInTheHole(ball,hole)){
                System.out.println("Found");
                return new Velocity(speed, angle);
            }
            // if the speed was too big
            else{
                float oldAngle = angle;
                float oldSpeed = speed;
                if(previousState!=null&&previousState!=currentState){

                    Gdx.app.log("Average","Previous angle "+previousVelocity.angle+" current angle "+angle+" previous speed "+previousVelocity.speed+ " current speed "+speed);
                    angle = (previousVelocity.angle+angle)/2f;
                    speed = (previousVelocity.speed+speed)/2f;
                    Gdx.app.log("Average result","speed "+speed+" angle "+angle);
                    previousVelocity = new Velocity(oldSpeed,oldAngle);
                    Gdx.app.log("Left or right",String.valueOf(leftRight()==currentState)+" "+leftRight().toString()+" "+currentState.toString());
                }
                else if(currentState==State.COLLIDED){
                    if(leftRight()==State.LEFT){
                        angle -= angleRate*angle;
                    }
                    else{
                        angle += angleRate*angle;
                    }
                }
                else if(ballPassedByHole()||ballRolledThroughTheHole){
                    speed -= speed*speedRate;
                    if(ballRolledThroughTheHole){
                        Gdx.app.log("Log","Ball rolled through the hole");

                    }

                    if(currentState==State.LEFT){
                        angle -= angleRate*angle;
                    }
                    else{
                        Gdx.app.log("Log:","Angle changed");
                        angle += angleRate*angle;
                    }
                    Gdx.app.log("Left or right",String.valueOf(leftRight()==currentState)+" "+leftRight().toString()+" "+currentState.toString());

                }
                else if(speed<course.getMaxSpeed()){
                    speed +=speed*speedRate;
                    System.out.println("isit working");
                    // if crossed the line
//                    angle +=angleRate*angle;
                }

                previousVelocity = new Velocity(oldSpeed,oldAngle);
            }
            // The speed is negligible what means that the bot can't find an optimal velocity
            if(speed<1f){
                Gdx.app.log("Log","The bot can't find an optimal velocity");
                speed = bestSpeed;
                break;
            }
            Gdx.app.log("Log","Current speed and angle"+String.valueOf(speed)+" "+String.valueOf(angle));
            ballRolledThroughTheHole = false;

            Gdx.app.log("Stop loop","previous state "+previousState+" current state "+currentState);
            previousState = currentState;
            if(currentState!=State.COLLIDED){
                ball.setFitnessValue((int)euclideanDistance(ball.getPosition(),hole.getPosition()));
                System.out.println("z "+ball.getPosition().z);
            }
            else{
                ball.setFitnessValue(3000);
                System.out.println("z "+ball.getPosition().z);
            }
            ball.setPosition(initialPosition);
            System.out.println(ball.getPosition().x + " "+ball.getPosition().y+" "+ball.getVelocity().speed+" "+ball.getVelocity().angle);
            balls.add(ball.clone());
        }
        Collections.sort(balls);
        ArrayList<Ball> newBalls = new ArrayList<Ball>();
        // TODO test with example balls, normally a, b, c should be found by the bot
        Ball a = balls.get(0);
        Ball b = balls.get(1);
        Ball c = balls.get(2);
        a.fix(false);
        b.fix(false);
        c.fix(false);
        a.setVelocity(140,132);
        a.setVelocityGA(140,132);
        b.setVelocity(150, 129);
        b.setVelocityGA(150,129);
        c.setVelocity(160, 130);
        c.setVelocityGA(160, 130);
        // end of test
        GeneticAlgorithm ga = new GeneticAlgorithm(hole,course);
        ga.simulateShot(a);
        ga.simulateShot(b);
        ga.simulateShot(c);
        System.out.println(a.getFitnessValue()+" "+b.getFitnessValue()+" "+c.getFitnessValue());
        newBalls.add(a);
        newBalls.add(b);
        newBalls.add(c);

        ga.startSimplex(newBalls);
        return new Velocity(speed, angle);
    }



    public void simulateShot(float speed, float speedTolerance){
        Gdx.app.log("Log","Simulation started");
        // At first ball is never moving, so without additional premise firstIteration the loop would never start
        boolean firstIteration=true;
        // After each simulation the ball should get its initial position (since we want to restart the shot from the
        // beginning with different speed
        float newClosestDistToHole = (float) euclideanDistance(ball.getPosition(),course.getGoalPosition());
        while(Physics.physics.isGoingToStop(ball)||firstIteration){
            firstIteration = false;
            ball.fix(false);
//            ball.update(Gdx.graphics.getDeltaTime());
            Physics.physics.updateObject(ball,Gdx.graphics.getDeltaTime());
            //&&euclideanDistance(ball.getPosition(),hole.getPosition())>hole.getRadius()
            if(ballPassedByHole()){
                Gdx.app.log("Log","Ball reached goal line");
                currentState = leftRight();
                break;
            }
            if(newClosestDistToHole>(float)euclideanDistance(ball.getPosition(),course.getGoalPosition())){
                newClosestDistToHole = (float) euclideanDistance(ball.getPosition(),course.getGoalPosition());
            }
            if(GameManager.isBallInTheHole(ball,hole)){
                ballRolledThroughTheHole = true;
                currentState = leftRight();
                if(ball.isSlow()) {
                    System.out.println("Ball in goal "+ball.getVelocity().getSpeed()+" angle "+ball.getVelocity().getAngle());
                    ball.fix(true);
                    break;
                }
            }
            // this is a quick and dirty way to check if the ball collided
            if(ball.isFixed()){
                currentState = State.COLLIDED;
                break;
            }
            if(!ball.isMoving(speedTolerance)){
                Gdx.app.log("Log","Ball stopped moving");
                currentState = State.STOPPED;
            }
//            if(!ball.isMoving(speedTolerance)&&!ballPassedByHole()&&!ballRolledThroughTheHole){
//                Gdx.app.log("Log","Ball stopped moving");
//                currentState = State.STOPPED;
//            }
//            if(Physics.physics.collided(ball)){
//                currentState = State.COLLIDED;
//            }
        }
        if(newClosestDistToHole<closestDistToHole){
            closestDistToHole = newClosestDistToHole;
            bestSpeed = speed;
        }

        Gdx.app.log("Log","Simulation completed");
    }
    // TODO change to make it appropriate for all ball/hole configurations
    public boolean ballPassedByHole(){
        if(ball.getPosition().y<hole.getPosition().y){
            return lineGoal.calculate(ball.getPosition().x)<ball.getPosition().y&&euclideanDistance(ball.getPosition(),hole.getPosition())>hole.getRadius();

        }
        else {
            return lineGoal.calculate(ball.getPosition().x)>ball.getPosition().y&&euclideanDistance(ball.getPosition(),hole.getPosition())>hole.getRadius();
        }
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
    public State ballPosition(){
//        assert(lineGoal.intersects(ball.getPosition()));
        if(ball.getPosition().y>hole.getPosition().y){
            return State.ABOVE;
        }
        else{
            return State.BELOW;
        }
    }

    /**
     * Checks if the ball is on the left or right side of the start-goal line
     */
    public State leftRight(){
//        Gdx.app.log("Above or below",ballPosition().toString());
        if(initialX<hole.getPosition().x){
            if(ballPosition()==State.ABOVE){
                return State.LEFT;
            }
            else{
                return State.RIGHT;
            }
        }
        else{
            if(ballPosition()==State.BELOW){
                return State.LEFT;
            }
            else{
                return State.RIGHT;
            }
        }
    }

    public Node equivalentNodeX(Vector3 position, ArrayList<Node> path){
        Node node = new Node(0,0);
        for(int i=0;i<path.size();i++){
            if(path.get(i).getxPosition()==position.x){
                node = path.get(i);
            }
        }
        return node;
    }
    

    public State ballPathPosition(){
//        assert(lineGoal.intersects(ball.getPosition()));
        if(ball.getPosition().y>equivalentNodeX(ball.getPosition(), path).getyPosition()){
            return State.ABOVE_PATH;
        }
        else{
            return State.BELOW_PATH;
        }
    }

    /**
     * Checks if the ball is on the left or right side of the start-goal line
     */
    public State leftRightPath(){
//        Gdx.app.log("Above or below",ballPosition().toString());
        if(initialX<equivalentNodeX(ball.getPosition(), path).getxPosition()){
            if(ballPathPosition()==State.ABOVE_PATH){
                return State.LEFT_PATH;
            }
            else{
                return State.RIGHT_PATH;
            }
        }
        else{
            if(ballPosition()==State.BELOW_PATH){
                return State.LEFT_PATH;
            }
            else{
                return State.RIGHT_PATH;
            }
        }
    }
}
