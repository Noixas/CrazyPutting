package com.crazy_putting.game.Bot;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Components.Colliders.SphereCollider;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
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
    private static final int MAX_ITER = 80;
    public int  nrOfGenerationsProduced;



    public GeneticAlgorithm(Hole hole, Course course, Vector3 initial_position){
        super(hole,course,initial_position);
        Gdx.app.log("Log","Genetic started");
        // TODO decreaded population size and generations
        this.rand = new Random();
        this.allBalls = new ArrayList<Ball>();
        this.firstIteration = new ArrayList<Ball>();

        createBallObjects();
        run();
        bestBall = allBalls.get(0);
        printBestBall();
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
                setEndPosition(allBalls.get(0).getEndPosition());
                return;
            }
//            if(allBalls.get(0).getFitnessValue()<60){
////                bruteForce(allBalls);
//                startSimplex(allBalls);
//            }
            // why is that here?
//            Collections.shuffle(allBalls);
            children = null;

            allBalls = elitistCrossover();

        }
        printBestBall();

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

//    private ArrayList<Ball> tournamentCrossover(){
//        // TODO finish
//        children = new ArrayList<Ball>();
//        for(int i=0;i<allBalls.size();i++){
//            ArrayList<Ball> tournamentList = new ArrayList<Ball>();
//            for(int j=0;j<3;j++){
//                tournamentList.add(allBalls.get((int)(Math.random()*allBalls.size())));
//            }
//            Collections.sort(tournamentList);
//            System.out.println("Tournament");
//            for(Ball b:tournamentList){
//                System.out.println(b.getFitnessValue());
//            }
//            Ball father = tournamentList.get(0).clone();
//            tournamentList.clear();
//            for(int j=0;j<3;j++){
//                tournamentList.add(allBalls.get((int)(Math.random()*allBalls.size())));
//            }
//            Collections.sort(tournamentList);
//            Ball mother = tournamentList.get(0).clone();
//            reproduce(father,mother,i);
//        }
//        return children;
//    }

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
            SphereCollider sp = new SphereCollider(addBall.getPosition(), 20);
            addBall.addColliderComponent(sp);
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
