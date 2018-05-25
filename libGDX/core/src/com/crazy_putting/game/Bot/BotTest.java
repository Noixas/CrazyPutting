package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.awt.image.AreaAveragingScaleFilter;
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
            Gdx.app.log("Next test course",testCourses.get(i).toString());
            for(int j = 0;j<testsPerCourse;j++){
                hole = new Hole((int) testCourses.get(i).getGoalRadius());
                GeneticAlgorithm GA = new GeneticAlgorithm(hole, testCourses.get(i));
                ArrayList<Float> result = new ArrayList<Float>();
                result.add(GA.getBestBall().getVelocityGA().speed);
                result.add(GA.getBestBall().getVelocityGA().angle);
                result.add((float)GA.getNrOfGenerationsProduced());
                result.add((float)i);
                results.add(result);
            }
        }
    }

    public void printResults(){
        System.out.println("Results");
        for(int i=0;i<results.size();i++){
            System.out.println("Course number: "+results.get(i).get(3)+ " nr of generations: "+results.get(i).get(2));
            System.out.println("Best ball - speed: "+results.get(i).get(0)+ " angle: "+results.get(i).get(1));
        }
    }
}
