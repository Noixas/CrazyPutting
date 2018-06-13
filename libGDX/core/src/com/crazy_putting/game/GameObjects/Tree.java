package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Tree extends GameObject {
    private Texture texture;
    private Vector3 position;

    public Tree(String filename){
        texture = new Texture(filename);
        position = new Vector3();
    }

}
