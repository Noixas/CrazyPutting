package com.crazy_putting.game.Bot;

import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;

public class MazeBot {
    public MazeBot(Hole hole, Course course){
        // 1. get intermediate points and tolerance (radius of intermediate points)
        // 2. change GA to make it work not between ball and hole, but between one point and another
        GeneticAlgorithm ga = new GeneticAlgorithm(hole,course);

    }
}
