package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Components.Graphics.Graphics3DComponent;
import com.crazy_putting.game.Components.Graphics.SphereGraphics3DComponent;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.GameObjects.SplinePoint;
import com.crazy_putting.game.Others.Velocity;

import java.util.ArrayList;

public class MazeBot {
    private final int  intermediateRadius = 30;
    private Vector3 startPoint;
    private Hole hole;
    private Ball ball;
    private Course course;
    private ArrayList<Vector3> intermediatePoints;
    public MazeBot(Ball ball, Hole hole, Course course){
        // 1. get intermediate points and tolerance (radius of intermediate points)
        // 2. change GA to make it work not between ball and hole, but between one point and another
        this.ball = ball;
        this.hole = hole;
        this.course = course;
        int startX = Math.round(CourseManager.getStartPosition(0).x);
        int startY = Math.round(CourseManager.getStartPosition(0).y);


        Map<Node> nodeMap = new Map<Node>(2000, 2000, new ExampleFactory());
        ArrayList<Node> path = (ArrayList<Node>)nodeMap.findPath(startX, startY); //Find path between StartNode and GoalNode

        intermediatePoints = new ArrayList<Vector3>();
        for(Node node:path){
            intermediatePoints.add(new Vector3(node.getxCoordinate(),node.getyCoordinate(),0));
        }
        calculateZ(intermediatePoints);
        createGraphicPoints(intermediatePoints);
        for(Vector3 point:intermediatePoints){
            System.out.println("Point x "+point.x+" y "+point.y+" z "+point.z);
        }
    }

    public ArrayList<Velocity> findSolution(){
        startPoint = CourseManager.getStartPosition(0);
        ArrayList<Velocity> mazeVelocities = new ArrayList<Velocity>();
        for(Vector3 point:intermediatePoints){
            Hole destinationPoint = new Hole(intermediateRadius,point);
            Gdx.app.log("Positions "," goal "+point.x+" "+point.y+" start "+startPoint.x+" "+startPoint.y);
            GeneticAlgorithm ga = new GeneticAlgorithm(destinationPoint,course,startPoint);
            mazeVelocities.add(new Velocity(ga.getBestBall().getVelocityGA().speed,ga.getBestBall().getVelocityGA().angle));
            startPoint = ga.getEndPosition();
//            Ball b = ga.getBestBall();
//            float speed = b.getVelocityGA().speed;
//            float angle = b.getVelocityGA().angle;
//            ball.setVelocity(speed,angle);
//            ball.fix(false);
        }
        Gdx.app.log("Log","findSolution finished");

        for(Velocity velocity:mazeVelocities){
            System.out.println("Speed: "+velocity.speed+" "+velocity.angle);
        }
        return mazeVelocities;

    }

    public void createGraphicPoints(ArrayList<Vector3> intermediatePoints){
        for(int i=0;i<intermediatePoints.size()-1;i++){
            SplinePoint point = new SplinePoint(new Vector3(intermediatePoints.get(i)));
            point.enabled = true;
            Graphics3DComponent pointGraphics = new SphereGraphics3DComponent(40, Color.YELLOW);
            point.addGraphicComponent(pointGraphics);
        }
    }

    public void calculateZ(ArrayList<Vector3> intermediatePoints){
        for(int i=0;i<intermediatePoints.size();i++){
            intermediatePoints.get(i).z = CourseManager.calculateHeight(intermediatePoints.get(i).x,intermediatePoints.get(i).y);
        }
    }


}
