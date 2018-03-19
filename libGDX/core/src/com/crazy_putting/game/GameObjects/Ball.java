package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.Others.InputData;
import com.crazy_putting.game.Others.Velocity;

public class Ball extends GameObject{
    private Vector2 position;
    private Velocity velocity;
    private Texture texture;

    public Ball(String filename){
        texture = new Texture(filename);
        position = new Vector2();
        velocity = new Velocity();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPositionX(float x){
        position.x = x;
    }

    public void setPositionY(float y){
        position.y = y;
    }

    public Texture getTexture() {
        return texture;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void update(float  dt){

    }

    public void handleInput(InputData input){
        // later on it should be if speed of the ball is zero (ball is not moving, then input data)
        if(Gdx.input.isKeyJustPressed(Input.Keys.I)){
            Gdx.input.getTextInput(input, "Input data", "", "Input speed and direction separated with space");
        }
        if(input.getText()!=null){
            try{
                String[] data = input.getText().split(" ");
                velocity.speed = Float.parseFloat(data[0]);
                velocity.angle = Float.parseFloat(data[1]);
            }
            catch(NumberFormatException e){
                // later on this will be added on the game screen so that it wasn't printed multiple times
                // after doing this change, delete printing stack trace
                Gdx.app.error("Exception: ","You must input numbers");
                e.getStackTrace();
            }
        }

    }




}
