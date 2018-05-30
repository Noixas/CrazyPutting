package com.crazy_putting.game.Bot;

public abstract class AbstractNode {

    protected static final int BASICMOVEMENTCOST = 0;
    protected static final int DIAGONALMOVEMENTCOST = 0;

    private int xPosition;
    private int yPosition;
    private boolean walkable;

    private AbstractNode previous;
    private boolean diagonally;
    private int movementPenalty; ///optional


    private int gCosts;
    private int hCosts;


    public AbstractNode(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.walkable = true;
        this.movementPenalty = 0;
    }

    public boolean isDiagonaly() {
        return diagonally;
    }

    public void setIsDiagonaly(boolean isDiagonaly) {
        this.diagonally = isDiagonaly;
    }

    public void setCoordinates(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }



    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public AbstractNode getPrevious() {
        return previous;
    }


    public void setPrevious(AbstractNode previous) {
        this.previous = previous;
    }

    public void setMovementPenalty(int movementPenalty) {
        this.movementPenalty = movementPenalty;
    }

    public int getfCosts() {
        return gCosts + hCosts;
    }

    public int getgCosts() {
        return gCosts;
    }

    private void setgCosts(int gCosts) {
        this.gCosts = gCosts + movementPenalty;
    }

    public void setgCosts(AbstractNode previousAbstractNode, int basicCost) {
        setgCosts(previousAbstractNode.getgCosts() + basicCost);
    }

    public void setgCosts(AbstractNode previousAbstractNode) {
        if (diagonally) {
            setgCosts(previousAbstractNode, DIAGONALMOVEMENTCOST);
        } else {
            setgCosts(previousAbstractNode, BASICMOVEMENTCOST);
        }
    }

    public int calculategCosts(AbstractNode previousAbstractNode) {
        if (diagonally) {
            return (previousAbstractNode.getgCosts()
                    + DIAGONALMOVEMENTCOST + movementPenalty);
        } else {
            return (previousAbstractNode.getgCosts()
                    + BASICMOVEMENTCOST + movementPenalty);
        }
    }


    public int calculategCosts(AbstractNode previousAbstractNode, int movementCost) {
        return (previousAbstractNode.getgCosts() + movementCost + movementPenalty);
    }

    public int gethCosts() {
        return hCosts;
    }


    protected void setCosts(int hCosts) {
        this.hCosts = hCosts;
    }

    public int calculateHcosts(int goalX, int goalY)
    {
        int currentX, currentY;
        currentX = this.xPosition;
        currentY = this.yPosition;
        setCosts((int)Math.sqrt((currentX-goalX)*(currentX-goalX)+(currentY-goalY)*(currentY-goalY)));
        return gethCosts();
    }


    public abstract void setCosts(AbstractNode endAbstractNode);


    private int getMovementPenalty() {
        return movementPenalty;
    }

    @Override
    public String toString() {
        return "(" + getxPosition() + ", " + getyPosition() + "): h: "
                + gethCosts() + " g: " + getgCosts() + " f: " + getfCosts();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractNode other = (AbstractNode) obj;
        if (this.xPosition != other.xPosition) {
            return false;
        }
        if (this.yPosition != other.yPosition) {
            return false;
        }
        return true;
    }


}
