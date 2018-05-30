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
    private ArrayList<Ball> firstIteration;
    private ArrayList<Ball> children;

    private Hole hole;
    private Course course;
    private Random rand;

    private Vector3 initial_Position;

    public final int POPULATION_SIZE = 200;
    private final double ELITE_RATE = 0.1;
    private final double MUTATION_RATE = 0.3;
    private static final int MAX_ITER = 80;
    public int  nrOfGenerationsProduced;
    public int fails;
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
        this.firstIteration = new ArrayList<Ball>();
        this.initial_Position = new Vector3();
        fails=0;

        createBallObjects();
        nrOfGenerationsProduced = run();


        bestBall = allBalls.get(0);
        printBestBall();
    }

    //main method for the algorithm
    public int run(){

        //create 5xPopulationSize list
        randomizeBallInput();

        unFixFirstBalls();
        //simulates for bigger list all the balls
        simulateFirstShots();

        //sort them by their distance result
        Collections.sort(firstIteration);

        //take only the best ones in our original population
        createPopulation();

        for(int i = 0; i < MAX_ITER;i++){

            unFixAllTheBall();

            simulateShots();

            Collections.sort(allBalls);



            System.out.println("Generation: " + (i+1) + " The best score is: " + allBalls.get(0).getFitnessValue());

            if(allBalls.get(0).getFitnessValue() == 0){
                System.out.println("Success");

                return i+1;
            }

            children = null;

            allBalls = crossOver();

        }
        fails++;
        return 0;
    }

    public void printBestBall(){
        System.out.println("The best ball that is found");
        System.out.println("Speed: " + df2.format(bestBall.getVelocityGA().speed));
        System.out.println("Angle: " + df2.format(bestBall.getVelocityGA().angle));

    }

    private void createPopulation(){
        for(int i=0; i<POPULATION_SIZE; i++){
            allBalls.add(firstIteration.get(i));
        }
    }

    private void simulateFirstShots(){
        for(int i = 0; i < firstIteration.size(); i++){
            simulateShot(firstIteration.get(i));
        }
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

        for(int i = eliteSize; i<POPULATION_SIZE;i++) {

            //first ball from the elites
            int i1 = (int) (Math.random() * eliteSize);

            int i2 = (int) (Math.random() * eliteSize);

            float angle1 = allBalls.get(i1).getVelocityGA().angle;
            float speed1 = allBalls.get(i1).getVelocityGA().speed;

            float angle2 = allBalls.get(i2).getVelocityGA().angle;
            float speed2 = allBalls.get(i2).getVelocityGA().speed;

            Ball iterativeBall = allBalls.get(i);


            // Whole arithmetic recombination with random u
            double u = Math.random();
            iterativeBall.setVelocityGA((float) ((((1 - u) * speed1 + u * speed2)) / 1f), ((float) ((1 - u) * angle1 + u * angle2) / 1));
            iterativeBall.setVelocity((((float) ((1 - u) * speed1 + u * speed2)) / 1f), ((float) ((1 - u) * angle1 + u * angle2) / 1));


            if (rand.nextFloat() < MUTATION_RATE) {
                float randomNum = -1 + rand.nextFloat()*2;

                if (u > 0.5) {
                    float newSpeed = speed2 + randomNum*10;
                    float newAngle = angle2 + randomNum*10;
                    iterativeBall.setVelocityGA((newSpeed), newAngle);
                    iterativeBall.setVelocity((newSpeed), newAngle);
                } else {
                    float newSpeed = speed1 + randomNum*10;
                    float newAngle = angle1 + randomNum*10;
                    iterativeBall.setVelocityGA((newSpeed), newAngle);
                    iterativeBall.setVelocity((newSpeed), newAngle);
                }
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

        int distance = 3000;

        b.setFitnessValue(distance);


        while (Physics.physics.calculateAcceleration(b) && !b.isFixed()){
            if (b.isSlow()) {
                distance = calcToHoleDistance(b);
                if (distance < hole.getRadius()) {
                    b.setFitnessValue(0);
                    return;
                }
                b.setFitnessValue(distance);
            }
            Physics.physics.updateBall(b,Gdx.graphics.getDeltaTime());
        }

        if (b.isFixed ()) {
            b.setFitnessValue(3000);
            return;
        }
    }

    private int calcToHoleDistance(Ball b){
        double xDist = Math.pow(b.getPosition().x - hole.getPosition().x,2);
        double yDist = Math.pow(b.getPosition().y - hole.getPosition().y,2);
        double zDist = Math.pow(b.getPosition().z - hole.getPosition().z,2);
        return (int) Math.sqrt(xDist + yDist + zDist);
    }

    private void randomizeBallInput(){

        if(!firstIteration.isEmpty()){

            for(Ball ball : firstIteration){
                float random = randomFloat();
                float speed = random * course.getMaxSpeed();
                float angle = rand.nextFloat()*361;
                ball.setVelocityGA(speed, angle);
                ball.setVelocity(speed, angle);
            }
        }
    }


    private void createBallObjects(){
        this.initial_Position = course.getStartBall();
        for(int i = 0 ; i < POPULATION_SIZE * 5; i++){

            Ball addBall = new Ball();

            addBall.setPosition(initial_Position);
            addBall.fix(false);
            firstIteration.add(addBall);
        }
    }

    private void unFixFirstBalls(){
        for(Ball b: firstIteration){
            b.fix(false);
        }
    }

    private void unFixAllTheBall(){
        for(Ball someBall : allBalls){
            someBall.fix(false);
        }
    }

    private float randomFloat(){
        float result = rand.nextFloat();
        while(result < 0.005){
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
