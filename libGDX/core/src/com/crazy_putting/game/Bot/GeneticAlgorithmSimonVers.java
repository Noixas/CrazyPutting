package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Physics.UpdatedPhysics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithmSimonVers {

    private ArrayList<Ball> allBalls;
    private Ball[] children;

    private Hole hole;
    private Course course;
    private Ball ball;
    private Random rand;

    public final int POPULATION_SIZE = 100;
    public final double ELITE_RATE = 0.2;
    public final double MUTATION_RATE = 0.25;
    private static final int MAX_ITER = 100;


    public GeneticAlgorithmSimonVers(Ball ball, Hole hole, Course course){

        this.ball = ball;
        this.hole = hole;
        this.course = course;
        this.rand = new Random();
        createBallObjects();

        run();
    }


    private void run(){

        randomizeBallInput();

        for(int i = 0; i < MAX_ITER;i++){
            simulateShots();
        }

    }


    private void simulateShots(){

        for(int i = 0; i < POPULATION_SIZE; i++){
            simulateShot(this.allBalls.get(i));

            Collections.sort(allBalls);

            System.out.println("Generation: " + i + "The best score is: " + allBalls.get(0).getFitnessValue());

            if(allBalls.get(0).getFitnessValue() == 0){
                System.out.println("Success");
                break;
            }

            crossOver();
        }
    }

    private void crossOver(){
        int eliteSize = (int) (POPULATION_SIZE*ELITE_RATE);

        children = new Ball[eliteSize];


        chooseElite();





    }


    private void chooseElite(){
        for(int i=0; i<children.length; i++){
            children[i] = allBalls.get(i);
        }
    }

    private void simulateShot(Ball b){
        while (b.isMoving() && !b.isFixed()){
            UpdatedPhysics.updateBall(b,Gdx.graphics.getDeltaTime());
        }
        if(b.isFixed()){
            b.setFitnessValue(-150);
            return;
        }
        float result = calcToHoleDistance(b);

        if(result < hole.getRadius()){
            b.setFitnessValue(0);
        }
        b.setFitnessValue(result);

    }

    private float calcToHoleDistance(Ball b){
        double xDist = Math.pow(b.getPosition().x - hole.getPosition().x,2);
        double yDist = Math.pow(b.getPosition().y - hole.getPosition().y,2);
        return (float) Math.sqrt(xDist + yDist);
    }

    private void randomizeBallInput(){

        if(!this.allBalls.isEmpty()){
            for(Ball ball : allBalls){
                float speed = rand.nextFloat() * CourseManager.getMaxSpeed();
                float angle = rand.nextInt(361);
                ball.setVelocityGA(speed,angle);
                ball.setVelocity(speed,angle);
            }
        }
    }


    private void createBallObjects(){
        for(int i = 0 ; i < POPULATION_SIZE; i++){
            Ball addBall = ball.clone();
            addBall.fix(false);
            UpdatedPhysics.addMovableObject(addBall);
            allBalls.add(addBall);
        }
    }
}
