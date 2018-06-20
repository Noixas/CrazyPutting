package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GameManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;

import java.util.ArrayList;

public class MazeBot {
    private final int  intermediateRadius = 30;
    private Vector3 startPoint;
    public MazeBot(Ball ball, Hole hole, Course course){
        // 1. get intermediate points and tolerance (radius of intermediate points)
        // 2. change GA to make it work not between ball and hole, but between one point and another
        ArrayList<Vector3> intermediatePoints = new ArrayList<Vector3>();
        intermediatePoints.add(new Vector3(50,50,800));
        intermediatePoints.add(new Vector3(560,-100,800));
        intermediatePoints.add(new Vector3(340,80,800));
        intermediatePoints.add(new Vector3(-440,50,800));
        intermediatePoints.add(new Vector3(-205,-550,800));
        intermediatePoints.add(new Vector3(560,-60,800));
        intermediatePoints.add(hole.getPosition());
        startPoint = CourseManager.getStartPosition();
        for(Vector3 point:intermediatePoints){
            Hole destinationPoint = new Hole(intermediateRadius,point);
            Gdx.app.log("Positions "," goal "+point.x+" "+point.y+" start "+startPoint.x+" "+startPoint.y);
            GeneticAlgorithm ga = new GeneticAlgorithm(destinationPoint,course,startPoint);
            startPoint = ga.getEndPosition();
//            Ball b = ga.getBestBall();
//            float speed = b.getVelocityGA().speed;
//            float angle = b.getVelocityGA().angle;
//            ball.setVelocity(speed,angle);
//            ball.fix(false);
        }

    }
}
