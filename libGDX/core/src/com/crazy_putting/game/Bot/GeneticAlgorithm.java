package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Physics.Physics;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm {

    private ArrayList<Ball> allBalls;
    private ArrayList<Ball> children;

    private Hole hole;
    private Course course;
    private Random rand;

    private Vector3 initial_Position;

    private final int POPULATION_SIZE = 500;
    private final double ELITE_RATE = 0.1;
    private final double MUTATION_RATE = 0.25;
    private static final int MAX_ITER = 80;
    private int  nrOfGenerationsProduced;
    private Ball bestBall;

    private static DecimalFormat df2 = new DecimalFormat(".##");
    //private int countRepetitions;
    //private int previousBest;

    public GeneticAlgorithm(Hole hole, Course course){
        // TODO decreaded population size and generations
        this.hole = hole;
        this.course = course;
        this.rand = new Random();
        this.allBalls = new ArrayList<Ball>();
        this.initial_Position = new Vector3();
        nrOfGenerationsProduced = 0;
        createBallObjects();

        run();

        bestBall = allBalls.get(0);
        printBestBall();
    }

    //main method for the algorithm
    private void run(){

        randomizeBallInput();

       // nrOfGenerationsProduced  = MAX_ITER;
        // implement previous best score if the score doesnt change in 15 generation reinitialize weights

        for(int i = 0; i < MAX_ITER;i++){
//            if(countRepetitions>10&&allBalls.get(0).getFitnessValue()>bestFit){
//                randomizeBallInput(course);
//                countRepetitions = 0;
//            }
//            bestFit = (int)allBalls.get(0).getFitnessValue();

            unFixAllTheBall();

            simulateShots();

            Collections.sort(allBalls);

            System.out.println("Generation: " + (i+1) + " The best score is: " + allBalls.get(0).getFitnessValue());

            if(allBalls.get(0).getFitnessValue() == 0){
                System.out.println("Success");

                return;
            }

            children = null;

            allBalls = crossOver();



        }
    }

    public void printBestBall(){
        System.out.println("The best ball is found");
        System.out.println("Speed: " + df2.format(bestBall.getVelocityGA().speed));
        System.out.println("Angle: " + df2.format(bestBall.getVelocityGA().angle));
    }


    private void simulateShots(){

        for(int i = 0; i < POPULATION_SIZE; i++){
            simulateShot(allBalls.get(i));
        }
    }

    private ArrayList<Ball> crossOver(){
        int eliteSize = (int) (POPULATION_SIZE*ELITE_RATE);

        children = new ArrayList<Ball>();


        chooseElite(eliteSize);

        for(int i = eliteSize; i<POPULATION_SIZE;i++){

            //first ball from the elites
            int i1 = (int) (Math.random() * eliteSize);

            int i2 = (int) (Math.random() * eliteSize);

            float angle1 = (int) allBalls.get(i1).getVelocityGA().angle;
            float speed1 = allBalls.get(i1).getVelocityGA().speed;

            float angle2 =  allBalls.get(i2).getVelocityGA().angle;
            float speed2 = allBalls.get(i2).getVelocityGA().speed;

            Ball iterativeBall = allBalls.get(i);


            // Whole arithmetic recombination with random u
            double u = Math.random();
            iterativeBall.setVelocityGA((float)((((1-u)*speed1+u*speed2))/1f),((float)((1-u)*angle1+u*angle2)/1));
            iterativeBall.setVelocity((((float)((1-u)*speed1+u*speed2))/1f),((float)((1-u)*angle1+u*angle2)/1));


            if(rand.nextFloat()<MUTATION_RATE){
                int newAngle = rand.nextInt(361);
                float newSpeed = rand.nextFloat()*course.getMaxSpeed();
                iterativeBall.setVelocityGA((rand.nextFloat()*newSpeed), newAngle);
                iterativeBall.setVelocity((rand.nextFloat()*newSpeed), newAngle);
            }
            iterativeBall.setPosition(course.getStartBall());

            children.add(iterativeBall);
        }

        return children;


    }


    private void chooseElite(int eSize){
        for(int i=0; i<eSize; i++){
            children.add(allBalls.get(i));
        }
    }

    private void simulateShot(Ball b){

        int distance = calcToHoleDistance(b);

        b.setFitnessValue(distance);


        while (Physics.physics.calculateAcceleration(b) && !b.isFixed()){
            if (b.isSlow()) {
                distance = calcToHoleDistance(b);
                if (distance < hole.getRadius() ) {
                    System.out.println("Distance: " + distance);
                    b.setFitnessValue(0);
                    return;
                }
                b.setFitnessValue(distance);
            }
            Physics.physics.updateBall(b,Gdx.graphics.getDeltaTime());
        }

        if (b.isFixed ()) {
            b.setFitnessValue(distance*5);
            return;
        }
    }

    private int calcToHoleDistance(Ball b){
        double xDist = Math.pow(b.getPosition().x - hole.getPosition().x,2);
        double yDist = Math.pow(b.getPosition().y - hole.getPosition().y,2);
        return (int) Math.round(Math.sqrt(xDist + yDist));
    }

    private void randomizeBallInput(){

        if(!this.allBalls.isEmpty()){

            for(Ball ball : allBalls){
                float random = randomFloat();
                float speed = random * course.getMaxSpeed();
                float angle = rand.nextFloat() * 360;
                ball.setVelocityGA(speed, angle);
                ball.setVelocity(speed, angle);
            }
        }
    }


    private void createBallObjects(){
        this.initial_Position = course.getStartBall();
        for(int i = 0 ; i < POPULATION_SIZE; i++){

            Ball addBall = new Ball();

            addBall.setPosition(initial_Position);
            addBall.fix(false);
            allBalls.add(addBall);
        }
    }

    private void unFixAllTheBall(){
        for(Ball someBall : allBalls){
            someBall.fix(false);
        }
    }

    private float randomFloat(){
        float result = rand.nextFloat();
        while(result*course.getMaxSpeed() < 0.51){
            result = rand.nextFloat();
        }
        return result;
    }

    public Ball getBestBall() {
        return bestBall;
    }

    public int getNrOfGenerationsProduced() {
        return nrOfGenerationsProduced;
    }
}
