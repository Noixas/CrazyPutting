package com.crazy_putting.game.Bot;

public class ExampleFactory implements NodeFactory {

    @Override
    public AbstractNode createNode(int x, int y) {
        return new Node(x, y);
    }

}

