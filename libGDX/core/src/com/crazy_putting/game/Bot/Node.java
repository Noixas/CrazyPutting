package com.crazy_putting.game.Bot;

import com.crazy_putting.game.GameLogic.CourseManager;

public class Node extends AbstractNode {

    public Node(int xIndex, int yIndex){
        super(xIndex, yIndex);
    }

//    public void sethCosts(AbstractNode endNode){
//        this.setCosts((int)CourseManager.calculateHeight(endNode.getxCoordinate(), endNode.getyCoordinate())
//                - (int)CourseManager.calculateHeight(this.getxCoordinate(), this.getyCoordinate()));
//    }
//
//    private int absolute(int a){
//        if(a>0) return a;
//        else return -a;
//    }

}

