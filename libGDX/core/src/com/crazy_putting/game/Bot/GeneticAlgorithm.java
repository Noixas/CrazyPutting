package com.crazy_putting.game.Bot;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Components.Colliders.SphereCollider;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Others.Noise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm extends SuperBot{
    private Random rand;
    private ArrayList<Ball> allBalls;
    private ArrayList<Ball> firstIteration;
    private ArrayList<Ball> children;
    private final int POPULATION_SIZE = 200;
    private final double ELITE_RATE = 0.1;
    private final double MUTATION_RATE = 0.3;
    private int maxIterations = 60;
    public int  nrOfGenerationsProduced;
    private int stuckCounter;
    private final int stuckThreshold = 20;
    private int lastBestBall;
    private boolean mazeFitness;
    private Map<Node> map;

    public GeneticAlgorithm(Hole hole, Course course, Vector3 initial_position, boolean mazeFitness){
        super(hole,course,initial_position);
        Gdx.app.log("Log","Genetic started");
        // TODO decreaded population size and generations
        this.rand = new Random();
        this.allBalls = new ArrayList<Ball>();
        this.firstIteration = new ArrayList<Ball>();
        stuckCounter = 0;
        lastBestBall = 10000;
        this.mazeFitness = mazeFitness;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public GeneticAlgorithm(Hole hole, Course course, Vector3 initial_position, boolean mazeFitness, int maxIterations, Map<Node> map){
        this(hole,course,initial_position,mazeFitness);
        this.maxIterations = maxIterations;
        this.map = map;
        System.out.println("here");

    }

    public void runGenetic(){
        createBallObjects();
        run();
        bestBall = allBalls.get(0);
        printBestBall();
    }

    public boolean isFitForMaze(Ball b){
        if(!mazeFitness){
            return false;
        }
        if(isClose(b)&&map.botLineOfSight((int)b.getPosition().x,(int)b.getPosition().y,(int)hole.getPosition().x,(int)hole.getPosition().y)){
            return true;
        }
        return false;
    }

    public boolean isClose(Ball b){
        boolean isClose = euclideanDistance(hole.getPosition(),initial_Position)>euclideanDistance(hole.getPosition(),b.getPosition());
        Gdx.app.debug("Debug",String.valueOf(isClose));
        return isClose;
    }

    public double euclideanDistance(Vector3 start, Vector3 goal){
        double dist = (float) Math.sqrt(Math.pow(start.x-goal.x,2)+Math.pow(start.y-goal.y,2)+Math.pow(start.z-goal.z,2));
        return dist;
    }

    //main method for the algorithm
    public void run(){

        //create 5xPopulationSize list
        randomizeBallInput();
        unFixFirstBalls();

        //simulates for bigger list all the balls
        simulateFirstShots();

        //sort them by their distance result
        Collections.sort(firstIteration);

        //take only the best ones in our original population
        createPopulation();
        Gdx.app.debug("Debug","max iterations"+maxIterations);
        for(int i = 0; i < maxIterations;i++){
            unFixAllTheBall();

            simulateShots();

            Collections.sort(allBalls);

            System.out.println("Generation: " + (i+1) + " The best score is: " + allBalls.get(0).getFitnessValue()+" speed "+allBalls.get(0).getVelocityGA().speed+" angle "+allBalls.get(0).getVelocityGA().angle+" "+calcToHoleDistance(allBalls.get(0)));

            if(allBalls.get(0).getFitnessValue() == 0){
                System.out.println("Success");
                setEndPosition(allBalls.get(0).getEndPosition());
                //WATCH OUT
                for(int j =1;j<allBalls.size();j++){
                    allBalls.get(j).destroy();
                }
                for(int j =1;j<firstIteration.size();j++){
                    firstIteration.get(j).destroy();
                }
                return;
            }
            if(isStuck()){
                startSimplex(allBalls);
                break;
            }
            children = null;

            allBalls = elitistCrossover();

        }
        setEndPosition(allBalls.get(0).getEndPosition());
        //printBestBall();

    }

    private boolean isStuck(){
        if(lastBestBall==allBalls.get(0).getFitnessValue()){
            if(stuckCounter>=stuckThreshold){
                stuckCounter = 0;
                return true;
            }
            stuckCounter++;
            return false;
        }
        lastBestBall = allBalls.get(0).getFitnessValue();
        return false;
    }


    private void createPopulation(){
        for(int i=0; i<POPULATION_SIZE; i++){
            allBalls.add(firstIteration.get(i));
        }
    }

    private void simulateFirstShots(){
        for(int i = 0; i < firstIteration.size(); i++){
            simulateShot(firstIteration.get(i));
            if(firstIteration.get(i).getFitnessValue()==0){
                return;
            }
        }
    }

    private void simulateShots(){
        for(int i = 0; i < POPULATION_SIZE; i++){
            simulateShot(allBalls.get(i));
            if(allBalls.get(i).getFitnessValue()==0){
                return;
            }
        }
    }

    private ArrayList<Ball> elitistCrossover(){
        int eliteSize = (int) (POPULATION_SIZE*ELITE_RATE);
        children = new ArrayList<Ball>();
        chooseElite(eliteSize);
        for(int i = eliteSize; i<POPULATION_SIZE;i++) {

            //first ball from the elites
            Ball father = allBalls.get((int) (Math.random() * eliteSize));
            Ball mother = allBalls.get((int) (Math.random() * eliteSize));
            reproduceLinearly(father,mother,i);
        }
        return children;
    }

    public void reproduce(Ball i1, Ball i2, int i){
        float angle1 = i1.getVelocityGA().angle;
        float speed1 = i1.getVelocityGA().speed;

        float angle2 = i2.getVelocityGA().angle;
        float speed2 = i2.getVelocityGA().speed;

        Ball iterativeBall = allBalls.get(i);


        // Whole arithmetic recombination with random u
        double u = Math.random();
        iterativeBall.setVelocityGA((float) ((((1 - u) * speed1 + u * speed2)) / 1f), ((float) ((1 - u) * angle1 + u * angle2) / 1));
        iterativeBall.setVelocity((((float) ((1 - u) * speed1 + u * speed2)) / 1f), ((float) ((1 - u) * angle1 + u * angle2) / 1));


        if (rand.nextFloat() < MUTATION_RATE) {
            float randomNum = -1 + rand.nextFloat()*2;

            if (u > 0.5) {
                float newSpeed = speed2 + randomNum*5;
                float newAngle = angle2 + randomNum*5;
                iterativeBall.setVelocityGA((newSpeed), newAngle);
                iterativeBall.setVelocity((newSpeed), newAngle);
            } else {
                float newSpeed = speed1 + randomNum;
                float newAngle = angle1 + randomNum;
                iterativeBall.setVelocityGA(Math.round(newSpeed), Math.round(newAngle));
                iterativeBall.setVelocity(Math.round(newSpeed), Math.round(newAngle));
            }
        }
        iterativeBall.setPosition(initial_Position);

        children.add(iterativeBall);
    }

    private void reproduceLinearly(Ball i1, Ball i2, int i){
        float angle1 = i1.getVelocityGA().angle;
        float speed1 = i1.getVelocityGA().speed;

        float angle2 = i2.getVelocityGA().angle;
        float speed2 = i2.getVelocityGA().speed;

        Ball iterativeBall = allBalls.get(i);

        float u = Noise.getInstance().nextFloat();
        float newSpeed;
        float newAngle;
        if(u < 0.33){
            newSpeed = (float) (0.5 * speed1 + 0.5 * speed2);
            newAngle = (float) (0.5 * angle1 + 0.5 * angle2);
        }
        else if(u < 0.66){
            newSpeed = (float) (1.5 * speed1 - 0.5 * speed2);
            newAngle = (float) (1.5 * angle1 - 0.5 * angle2);
        }
        else{
            newSpeed = (float) (-0.5 * speed1 + 1.5 * speed2);
            newAngle = (float) (-0.5 * angle1 + 1.5 * angle2);
        }
        /*

        if (rand.nextFloat() < MUTATION_RATE) {
            float randomNum = -1 + rand.nextFloat()*2;

            if (u > 0.5) {
                newSpeed = speed2 + randomNum*5;
                newAngle = angle2 + randomNum*5;
            } else {
                newSpeed = speed1 + randomNum;
                newAngle = angle1 + randomNum;
            }
        }
        */


        if (Noise.getInstance().nextFloat() < MUTATION_RATE) {

            float speedAdd;
            float angleAdd;
            if (Noise.getInstance().nextFloat() > 0.3){
                speedAdd = Noise.getInstance().nextNormal(0, nrOfGenerationsProduced);
                angleAdd = Noise.getInstance().nextNormal(0,maxIterations - nrOfGenerationsProduced+1);

        }
        else{
                speedAdd = Noise.getInstance().nextNormal(0, maxIterations - nrOfGenerationsProduced+1);
                angleAdd = Noise.getInstance().nextNormal(0,nrOfGenerationsProduced * 2.5f);
            }
            //System.out.println(randomNum);

            newSpeed += speedAdd;
            newAngle += angleAdd;

        }
        
        //if(Noise.getInstance().nextInt(1000) < 150) {
         //   System.out.println("New speed: " + newSpeed + " new angle: " + newAngle);
        //}
        if(newSpeed > course.getMaxSpeed()){
            newSpeed = course.getMaxSpeed();
        }
        iterativeBall.setVelocityGA((newSpeed), newAngle);
        iterativeBall.setVelocity((newSpeed), newAngle);

        iterativeBall.setPosition(initial_Position);

        children.add(iterativeBall);

    }

    private void chooseElite(int eSize){
        for(int i=0; i<eSize; i++){
            children.add(allBalls.get(i));
        }
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
        for(int i = 0 ; i < POPULATION_SIZE * 5; i++){
            Ball addBall = new Ball(initial_Position);
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
