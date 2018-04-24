package com.crazy_putting.game.Bot;

import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.Others.Velocity;

public class Bot {
    public Ball ball;
    public Course course;

    public Bot(Ball ball, Course course){
        this.ball = ball.clone();
        this.course = course;
    }

    public Velocity computeCourse(){
        Velocity newCourse = new Velocity();
        return newCourse;
    }
}
