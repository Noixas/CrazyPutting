package com.crazy_putting.game.Bot;

import com.crazy_putting.game.GameLogic.CourseManager;

public class Node extends AbstractNode {
    //private int value; // This is height of select point
    //private Node next; // Link to next node

    public Node(int xPosition, int yPosition){
        super(xPosition, yPosition);
        if(CourseManager.calculateHeight(xPosition, yPosition)< + 0) setWalkable(false);
    }

    public void setCosts(AbstractNode endNode){
        this.setCosts((int)CourseManager.calculateHeight(endNode.getxPosition(), endNode.getyPosition())
                - (int)CourseManager.calculateHeight(this.getxPosition(), this.getyPosition())
                + (absolute(this.getxPosition() - endNode.getxPosition()) + absolute(this.getyPosition() - endNode.getyPosition())) * BASICMOVEMENTCOST);
    }

    private int absolute(int a){
        if(a>0) return a;
        else return -a;
    }

}

