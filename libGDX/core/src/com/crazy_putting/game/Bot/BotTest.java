package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Physics.Physics;

import java.util.ArrayList;
import java.util.List;

public class BotTest {
    private ArrayList<Course> testCourses;
    private Hole hole;
    private int testsPerCourse;
    private ArrayList<ArrayList<Float>> results;

    public BotTest(List<Course> testCourses, int testsPerCourse){
        this.testCourses = (ArrayList<Course>)testCourses;
        this.testsPerCourse = testsPerCourse;
        results=new ArrayList<ArrayList<Float>>();
    }

    public void runTest(){
        for(int i=0;i<testCourses.size();i++){
            CourseManager.setActiveCourseWithIndex(i);
            Gdx.app.log("Next test course",testCourses.get(i).toString());
            Physics.physics.updateCoefficients();
            hole = new Hole((int) testCourses.get(i).getGoalRadius());
            hole.setPosition(testCourses.get(i).getGoalPosition());
            for(int j = 0;j<testsPerCourse;j++){
                GeneticAlgorithm GA = new GeneticAlgorithm(hole, testCourses.get(i));
                ArrayList<Float> result = new ArrayList<Float>();
                result.add(GA.getBestBall().getVelocityGA().speed);
                result.add(GA.getBestBall().getVelocityGA().angle);
                System.out.println(GA.getBestBall().getVelocityGA().speed==GA.getBestBall().getVelocity().speed);
                System.out.println(GA.getBestBall().getVelocityGA().angle==GA.getBestBall().getVelocity().angle);
                result.add((float)GA.getNrOfGenerationsProduced());
                result.add((float)i);
                results.add(result);
                System.out.println("Fitness "+GA.getBestBall().getFitnessValue());
            }
        }
    }

    public void printResults(){
        System.out.println("Results");
        for(int i=0;i<results.size();i++){
            int numberOfGenerations = 20;
            if(results.get(i).get(2)<numberOfGenerations){
                System.out.println("Course number: "+results.get(i).get(3)+ " nr of generations: "+results.get(i).get(2));
                System.out.println("Best ball - speed: "+results.get(i).get(0)+ " angle: "+results.get(i).get(1));
            }
            else{
                System.out.println("Course number: "+results.get(i).get(3)+" Couldn't find optimal path");
            }
        }
    }
}