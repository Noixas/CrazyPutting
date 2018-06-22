package com.crazy_putting.game.Bot;

import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Course;

import java.util.ArrayList;
import java.util.List;

public class Map<T extends AbstractNode> {

    private T[][] nodes;

    protected int width;
    protected int length;
    //Goal node coordinates
    private final int GOAL_X = (int)CourseManager.getGoalStartPosition().x, GOAL_Y = (int)CourseManager.getGoalStartPosition().y;
    private final int GOAL_NODE_X = GOAL_X + 1000, GOAL_NODE_Y = GOAL_Y + 1000;
    private final float GOAL_RADIUS = CourseManager.getActiveCourse().getGoalRadius();

    private float gold = 0;

    ArrayList<T> path = new ArrayList<T>();
    private NodeFactory nodeFactory;

    public Map(int width, int length, NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        nodes = (T[][]) new AbstractNode[width][length];
        this.width = width - 1;
        this.length = length - 1;
        initEmptyNodes();
    }

    private void initEmptyNodes() {
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= length; j++) {
                nodes[i][j] = (T) nodeFactory.createNode(i, j);
                nodes[i][j].setCoordinates(i - 1000, j - 1000);
                nodes[i][j].setBASICMOVEMENTCOST(0);
                nodes[i][j].setWalkable(false);
//                nodes[i][j].setBASICMOVEMENTCOST(CourseManager.calculateHeight(i - 1000, j - 1000)/10);
                if(CourseManager.calculateHeight( nodes[i][j].getxCoordinate(), nodes[i][j].getyCoordinate()) > + 0) {nodes[i][j].setWalkable(true);}
                nodes[i][j].setTotalCost(GOAL_NODE_X, GOAL_NODE_Y);
//                if(Math.sqrt(Math.pow(nodes[i][j].getxCoordinate() - GOAL_X,2) + Math.pow(nodes[i][j].getyCoordinate() - GOAL_Y,2)) < GOAL_RADIUS){
//                    nodes[i][j].setGoal(true);
//                }
            }
        }
        System.out.println("Nodes INITIALISED");
    }

    public final T getNode(int x, int y) {
        return nodes[x][y];
    }

    private List<T> openList;
    private List<T> closedList;


    public final List<T> findPath(int oldX, int oldY, int newX, int newY){
        T current;
        /* Converting coords to node indeces*/
        oldX += 1000;
        oldY += 1000;
        newX += 1000;
        newY += 1000;

        openList = new ArrayList<T>();
        closedList = new ArrayList<T>();
        current = nodes[oldX][oldY];
        current.setPrevious(current);
        current.setgCosts(0);

        openList.add(current);
        while(!openList.isEmpty()){
            current = lowestFInOpen();
            openList.remove(current);
            if(current.getxIndex() == newX && current.getyIndex() == newY){
                System.out.println("Path found");
                return null;
                //return calcPath(nodes[oldX][oldY], current);
            }
            closedList.add(current);

            List<T> adjacentNodes = getAdjacent(current);
            for(int i=0;i<adjacentNodes.size();i++){
                T currentAdj = adjacentNodes.get(i);
                if(!closedList.contains(currentAdj)){
                    if(!openList.contains(currentAdj)){
                        currentAdj.setgCosts(currentAdj, Integer.MAX_VALUE);
                        currentAdj.setPrevious(null);
                    }
                    updateVertex(current, currentAdj);
                }
            }
        }
        return null;
    }

    private void updateVertex(T s, T s1)
    {
        gold = s1.getgCosts();
        computeCost(s, s1);
        if(s1.getgCosts() < gold)
        {
            if(openList.contains(s1)){
                openList.remove(s1);
            }
            s1.setTotalCost( GOAL_NODE_X, GOAL_NODE_Y);
            openList.add(s1);
        }
    }
    private void computeCost(T s, T s1)
    {
        if(lineOfSight(s, s1))
        {
//          System.out.println("FOUND LOS");
            if(s.getPrevious().getgCosts() + calculateStraight(s.getPrevious().getxIndex(), s.getPrevious().getyIndex(), s1.getxIndex(), s1.getyIndex()) < s1.getgCosts()){
                s1.setPrevious(s.getPrevious());
                s1.setgCosts(s.getPrevious().getgCosts() + calculateStraight(s.getPrevious().getxIndex(), s.getPrevious().getyIndex(), s1.getxIndex(), s1.getyIndex()));
            }
        }
        else{
            //System.out.println("Didnt find los");
            if(s.getgCosts() + calculateStraight(s.getxIndex(), s.getyIndex(), s1.getxIndex(), s1.getyIndex()) < s1.getgCosts()){
                s1.setPrevious(s);
                s1.setgCosts(s.getgCosts() + calculateStraight(s.getxIndex(), s.getyIndex(), s1.getxIndex(), s1.getyIndex()));
            }
        }
    }


    private List<T> calcPath(T start, T goal) {
        // TODO if invalid nodes are given (eg cannot find from goal to start, this method will result in an infinite loop!)

        T curr = goal;
        boolean done = false;
        while (!done) {
            path.add(0,curr);
            curr = (T) curr.getPrevious();
            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }

    private T lowestFInOpen() {
        T cheapest = openList.get(0);
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getTotalCost() < cheapest.getTotalCost()) {
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }

    private List<T> getAdjacent(T node) {
        int x = node.getxIndex();
        int y = node.getyIndex();
        List<T> adj = new ArrayList<T>();

        T temp;
            // WEST WEST WEST
        if (x > 0) {
            temp = this.getNode((x - 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
            // EAST EAST EAST
        if (x < width) {
            temp = this.getNode((x + 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
            // SOUTH SOUTH SOUTH
        if (y > 0) {
            temp = this.getNode(x, (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
            // NORTH NORTH NORTH
        if (y < length) {
            temp = this.getNode(x, (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
            // NORTH EAST NORTH EAST NORTH EAST
        if (x < width && y < length) {
            temp = this.getNode((x + 1), (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
            // SOUTH WEST SOUTH WEST SOUTH WEST
        if (x > 0 && y > 0) {
            temp = this.getNode((x - 1), (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
            // NORTH WEST NORTH WEST NORTH WEST
        if (x > 0 && y < length) {
            temp = this.getNode((x - 1), (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
            // SOUTH EAST SOUTH EAST SOUTH EAST
        if (x < width && y > 0) {
            temp = this.getNode((x + 1), (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
        return adj;
    }

    private boolean lineOfSight(T s, T s1)
    {
        int x1,y1,x2,y2;
        x1 = s.getPrevious().getxIndex();
        y1 = s.getPrevious().getyIndex();

        x2 = s1.getxIndex();
        y2 = s1.getyIndex();

        int dx,dy;
        int f = 0;
        int sx, sy; // direction of movement
        dx = x2 - x1;
        dy = y2 - y1;

        if(dx < 0){
            dx = -dx;
            sx = -1;
        }
        else sx = 1;

        if(dy < 0){
            dy = -dy;
            sy = -1;
        }
        else sy = 1;
        if(dx > dy){
            while(x1 != x2) {
                f += dy;
                if(f >= dx){
                    if(nodes[x1 + ((sx - 1) / 2)][y1 + ((sy - 1) / 2)].isWalkable()==false){return false;}
                    y1 += sy;
                    f -= dx;
                }
                if(f != 0 && nodes[x1+((sx-1)/2)][y1+((sy-1)/2)].isWalkable()==false){return false;}
                if(dy == 0 && nodes[x1+((sx-1)/2)][y1].isWalkable()==false && nodes[x1 + ((sx - 1) / 2)][y1 - 1].isWalkable()==false) {return false;}
                x1 += sx;
            }


        }
        else {
            while(y1 != y2) {
                f += dx;
                if(f >= dy){
                    if(nodes[x1 + ((sx - 1) / 2)][y1 + ((sy - 1) / 2)].isWalkable()==false){return false;}
                    x1 += sx;
                    f -= dy;
                }
            if(f != 0 && nodes[x1 + ((sx - 1) / 2)][ y1 + ((sy - 1) / 2)].isWalkable()==false ){return false;}
            if(dx == 0 && nodes[x1][y1 + ((sy - 1) / 2)].isWalkable()==false && nodes[x1 - 1][y1 + ((sy - 1) / 2)].isWalkable()==false ){return false;}
            y1 += sy;
            }
        }
        return true;
    }
    private int calculateStraight(int x1, int y1, int x2, int y2){
        return (int)Math.sqrt(Math.pow(x2 - x1 , 2) + Math.pow(y2 - y1 , 2));
    }
}
