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

import java.awt.image.AreaAveragingScaleFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
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

    private final int POPULATION_SIZE = 200;
    private final double ELITE_RATE = 0.1;
    private final double MUTATION_RATE = 0.3;
    private static final int MAX_ITER = 80;
    public int  nrOfGenerationsProduced;
    //public int fails;
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
        this.initial_Position = CourseManager.getStartPosition();
        //fails=0;

//        createBallObjects();
//
//        run();
//
//        bestBall = allBalls.get(0);
//        printBestBall();
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

        for(int i = 0; i < MAX_ITER;i++){


            unFixAllTheBall();

            simulateShots();

            Collections.sort(allBalls);



            System.out.println("Generation: " + (i+1) + " The best score is: " + allBalls.get(0).getFitnessValue()+" speed "+allBalls.get(0).getVelocityGA().speed+" angle "+allBalls.get(0).getVelocityGA().angle+" "+calcToHoleDistance(allBalls.get(0)));

            if(allBalls.get(0).getFitnessValue() == 0){
                System.out.println("Success");

                return;
            }
            // why is that here?
//            Collections.shuffle(allBalls);
            children = null;

            allBalls = tournamentCrossover();

        }
//        Collections.sort(allBalls);
//        System.out.println("Fitness value "+allBalls.get(0).getFitnessValue());
//        ArrayList<Ball> balls = new ArrayList<Ball>();
//        Ball a = allBalls.get(0).clone();
//        Ball b = allBalls.get(1).clone();
//        Ball c = allBalls.get(2).clone();
//        balls.add(a);
//        balls.add(b);
//        balls.add(c);
//        startSimplex(balls);
        printBestBall();

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

    private ArrayList<Ball> tournamentCrossover(){
        children = new ArrayList<Ball>();
        for(int i=0;i<allBalls.size();i++){
            ArrayList<Ball> tournamentList = new ArrayList<Ball>();
            for(int j=0;j<3;j++){
                tournamentList.add(allBalls.get((int)(Math.random()*allBalls.size())));
            }
            Collections.sort(tournamentList);
            System.out.println("Tournament");
            for(Ball b:tournamentList){
                System.out.println(b.getFitnessValue());
            }
            Ball father = tournamentList.get(0).clone();
            tournamentList.clear();
            for(int j=0;j<3;j++){
                tournamentList.add(allBalls.get((int)(Math.random()*allBalls.size())));
            }
            Collections.sort(tournamentList);
            Ball mother = tournamentList.get(0).clone();
//            System.out.println(father.getVelocity().angle+" "+mother.getVelocity().angle);
            reproduce(father,mother,i);


        }
        return children;
    }

    private ArrayList<Ball> elitistCrossover(){
        int eliteSize = (int) (POPULATION_SIZE*ELITE_RATE);

        children = new ArrayList<Ball>();


        chooseElite(eliteSize);

        for(int i = eliteSize; i<POPULATION_SIZE;i++) {

            //first ball from the elites
            Ball father = allBalls.get((int) (Math.random() * eliteSize));

            Ball mother = allBalls.get((int) (Math.random() * eliteSize));

            reproduce(father,mother,i);
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
        iterativeBall.setPosition(CourseManager.getStartPosition());

        children.add(iterativeBall);
    }


    private void chooseElite(int eSize){
        for(int i=0; i<eSize; i++){
            children.add(allBalls.get(i));
        }
    }

    public void simulateShot(Ball b){
        int distance = 4000;
        b.setFitnessValue(distance);
        int lastDistance = 0;
        while (!Physics.physics.isGoingToStop(b) && !b.isFixed()){
            if (b.isSlow()) {
                distance = calcToHoleDistance(b);
                if (distance < hole.getRadius()) {
                    b.setFitnessValue(0);
                    System.out.println("Fitness value "+b.getFitnessValue()+" position "+b.getPosition().x+" "+b.getPosition().y);
                    b.setPosition(initial_Position);
                    b.setVelocity(b.getVelocityGA().speed,b.getVelocityGA().angle);
                    return;
                }
            }
            lastDistance = calcToHoleDistance(b);
            Physics.physics.updateObject(b,Gdx.graphics.getDeltaTime());
//            System.out.println("Ball"+" position "+b.getPosition().x+" "+b.getPosition().y);
        }
        System.out.println("Going to stop or fixed "+b.getVelocity().speed);

        if (b.isFixed ()) {
            b.setFitnessValue(3000+lastDistance);
            System.out.println("Fitness value fixed"+b.getFitnessValue()+" position "+b.getPosition().x+" "+b.getPosition().y);
            b.setPosition(initial_Position);
            b.setVelocity(b.getVelocityGA().speed,b.getVelocityGA().angle);
            return;
        }
        else{
            distance = calcToHoleDistance(b);
            b.setFitnessValue(distance);
            System.out.println("Fitness value finished"+b.getFitnessValue()+" position "+b.getPosition().x+" "+b.getPosition().y+" is fixed"+b.getVelocityGA().speed+" "+b.getVelocityGA().angle+
                    " "+b.getVelocity().speed+" "+b.getVelocity().angle);

            b.setPosition(initial_Position);
            b.setVelocity(b.getVelocityGA().speed,b.getVelocityGA().angle);
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

    public void startSimplex(ArrayList<Ball> initialBalls){
        Gdx.app.log("Start","Simplex");
        ArrayList<Ball> balls = new ArrayList<Ball>();

        Ball x1 = initialBalls.get(0).clone();
        Ball x2 = initialBalls.get(1).clone();
        Ball x3 = initialBalls.get(2).clone();
        x1.fix(false);
        x2.fix(false);
        x3.fix(false);
        System.out.println("x2 "+x1. getFitnessValue()+" "+x1. isFixed());
        System.out.println("x2 "+x2.getFitnessValue()+" "+x2.isFixed());
        System.out.println("x2 "+x3.getFitnessValue()+" "+x3.isFixed());
        System.out.println(x1. getVelocity().speed + " "+x1. getVelocity().angle+" "+x1. getVelocityGA().speed+" "+x1. getVelocityGA().angle);
        System.out.println(x2.getVelocity().speed + " "+x2.getVelocity().angle+" "+x2.getVelocityGA().speed+" "+x2.getVelocityGA().angle);
        System.out.println(x3.getVelocity().speed + " "+x3.getVelocity().angle+" "+x3.getVelocityGA().speed+" "+x3.getVelocityGA().angle);
        x1. setVelocity(x1. getVelocityGA().speed,x1. getVelocityGA().angle);
        x2.setVelocity(x2.getVelocityGA().speed,x2.getVelocityGA().angle);
        x3.setVelocity(x3.getVelocityGA().speed,x3.getVelocityGA().angle);
        System.out.println(x1.getPosition().x+" "+x1.getPosition().y);
        simulateShot(x1);
        System.out.println("break");
        simulateShot(x2);
        simulateShot(x3);
        balls.add(x1);
        balls.add(x2);
        balls.add(x3);
        System.out.println("b2 "+x2.getFitnessValue());
        bestBall = findRoot(balls);
        Gdx.app.log("Log - simplex ball",bestBall.getVelocity().speed+" "+bestBall.getVelocity().angle);
    }

    public Ball findRoot(ArrayList<Ball> balls){
        Collections.sort(balls);
        System.out.println("Fitness values");
        for(Ball ball:balls){
            System.out.println(ball.getFitnessValue()+" "+ball.getVelocity().speed+" "+ball.getVelocity().angle+" "+ball.isFixed());
        }
        // TODO set positions of a, x2, x3
        // TODO run simulations for point

        System.out.println(balls.get(0).getFitnessValue());

        if(balls.size()==0||balls.get(0).getFitnessValue()!=0){
            Ball x1 = balls.get(0).clone();
            Ball x2 = balls.get(1).clone();
            Ball x3 = balls.get(2).clone();
            float speed0 = (x1.getVelocityGA().speed+x2.getVelocityGA().speed)/2;
            float angle0 = (x1.getVelocityGA().angle+x2.getVelocityGA().angle)/2;
            Velocity x0 = new Velocity(speed0,angle0);
            System.out.println(x1. getFitnessValue());
            Ball xr = new Ball();
            xr.setPosition(initial_Position);
            xr.fix(false);
            // xr = x0+alpha*(x0-x3)
            // alpha - reflection coefficient
            final float  alpha = 1;
            float speedR = x0.speed+alpha*(x0.speed-x3.getVelocityGA().speed);
            float angleR = x0.angle+alpha*(x0.angle-x3.getVelocityGA().angle);
            xr.setVelocity(speedR,angleR);
            xr.setVelocityGA(speedR,angleR);
            simulateShot(xr);
            System.out.println(xr.getFitnessValue());
            if(x1.getFitnessValue()<xr.getFitnessValue()&&xr.getFitnessValue()<x2.getFitnessValue()){
                balls.clear();
                balls.add(x1);
                balls.add(x2);
                balls.add(xr);
                System.out.println("x1, x2, xr1");
                findRoot(balls);
            }
            else if(xr.getFitnessValue()<x1.getFitnessValue()){
                Ball xe = new Ball();
                xe.setPosition(initial_Position);
                xe.fix(false);
                final float gamma = 2;
                float speedE = x0.speed+gamma*(xr.getVelocityGA().speed-x0.speed);
                float angleE = x0.angle+gamma*(xr.getVelocityGA().angle-x0.angle);
                xe.setVelocity(speedE,angleE);
                xe.setVelocityGA(speedE,angleE);
                simulateShot(xe);
                if(xe.getFitnessValue()<xr.getFitnessValue()) {
                    balls.clear();
                    balls.add(x1);
                    balls.add(x2);
                    balls.add(xe);
                    System.out.println("x1, x2, xe");
                    findRoot(balls);
                }
                else {
                    balls.clear();
                    balls.add(x1);
                    balls.add(x2);
                    balls.add(xr);
                    System.out.println("x1, x2, xr");
                    findRoot(balls);

                }
            }
            else if(xr.getFitnessValue()>=x2.getFitnessValue()) {
                // xc = x0 + rho*(x3-x0)
                Ball xc = new Ball();
                xc.setPosition(initial_Position);
                xc.fix(false);
                final float rho = 0.5f;
                float speedC = x0.speed+rho*(x3.getVelocityGA().speed-x0.speed);
                float angleC = x0.angle+rho*(x3.getVelocityGA().angle-x0.angle);
                xc.setVelocity(speedC,angleC);
                xc.setVelocityGA(speedC,angleC);
                simulateShot(xc);
                if(xc.getFitnessValue()<x3.getFitnessValue()) {
                    balls.clear();
                    balls.add(x1);
                    balls.add(x2);
                    balls.add(xc);
                    System.out.println("x1, x2, xc");
                    findRoot(balls);
                }
                else{

                    // xi = x1+sigma*(xi-x1)

                    x2.setPosition(initial_Position);
                    x2.fix(false);
                    final float sigma = 0.5f;
                    float speed2 = x1.getVelocityGA().speed+sigma*(x2.getVelocityGA().speed-x1.getVelocityGA().speed);
                    float angle2 = x1.getVelocityGA().angle+sigma*(x2.getVelocityGA().angle-x1.getVelocityGA().angle);
                    x2.setVelocity(speed2,angle2);
                    System.out.println("speed2 "+speed2+" "+angle2);
                    x2.setVelocityGA(speed2,angle2);
                    simulateShot(x2);

                    x3.setPosition(initial_Position);
                    x3.fix(false);
                    float speed3 = x1.getVelocityGA().speed+sigma*(x3.getVelocityGA().speed-x1.getVelocityGA().speed);
                    float angle3 = x1.getVelocityGA().angle+sigma*(x3.getVelocityGA().angle-x1.getVelocityGA().angle);
                    x3.setVelocity(speed3,angle3);
                    System.out.println("speed3 "+speed3+" "+angle3);
                    x3.setVelocityGA(speed3,angle3);
                    simulateShot(x3);
                    balls.clear();
                    balls.add(x1);
                    balls.add(x2);
                    balls.add(x3);
                    System.out.println("x1, x2, x3 1");
                    findRoot(balls);
                }
            }
            else{

                // xi = x1+sigma*(xi-x1)
                x2.setPosition(initial_Position);
                x2.fix(false);
                final float sigma = 0.5f;
                float speed2 = x1.getVelocityGA().speed+sigma*(x2.getVelocityGA().speed-x1.getVelocityGA().speed);
                float angle2 = x1.getVelocityGA().angle+sigma*(x2.getVelocityGA().angle-x1.getVelocityGA().angle);
                System.out.println("speed2 "+speed2+" "+angle2);
                x2.setVelocity(speed2,angle2);
                x2.setVelocityGA(speed2,angle2);
                simulateShot(x2);

                x3.setPosition(initial_Position);
                x3.fix(false);
                float speed3 = x1.getVelocityGA().speed+sigma*(x3.getVelocityGA().speed-x1.getVelocityGA().speed);
                float angle3 = x1.getVelocityGA().angle+sigma*(x3.getVelocityGA().angle-x1.getVelocityGA().angle);
                System.out.println("speed3 "+speed3+" "+angle3);
                x3.setVelocity(speed3,angle3);
                x3.setVelocityGA(speed3,angle3);
                simulateShot(x3);
                balls.clear();
                balls.add(x1);
                balls.add(x2);
                balls.add(x3);
                System.out.println("x1, x2, x3 2");
                findRoot(balls);
            }
        }
        System.out.println("Ball found"+balls.get(0).getVelocityGA().speed+" "+balls.get(0).getVelocityGA().angle);
        return balls.get(0);
    }
}
