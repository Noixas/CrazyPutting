package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.crazy_putting.game.Components.GraphicsComponent;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.MyCrazyPutting;
import com.crazy_putting.game.Others.InputData;
import com.crazy_putting.game.Physics.PhysicsGenericFormulaTest;
import com.crazy_putting.game.Screens.GolfGame;

import java.util.Random;

public class GameManager {

    private Ball _ball;
    private Hole _hole;
    private GolfGame _game;
    private int _turns;

    public GameManager(GolfGame pGame)
    {
     _ball = new Ball("golfBall.png");
     _game = pGame;
     _hole = new Hole(30);
     _turns = 0;

    _ball.addGraphicComponent(new GraphicsComponent( _ball.getTexture()));
    _hole.addGraphicComponent(new GraphicsComponent(
            new Texture("hole.png"), _hole.getRadius()*2, _hole.getRadius()*2));

        randomizeStartPos();
    }
    public void Update(float pDelta)
    {
        handleInput(_game.input);
        _ball.update(pDelta);
        PhysicsGenericFormulaTest.update(_ball, pDelta);
        UpdateGameLogic(pDelta);

    }
    private void UpdateGameLogic(float pDelta)
    {
        if(Math.abs(_ball.getPosition().x - _hole.getPosition().x) < _hole.getRadius() &&
                Math.abs(_ball.getPosition().y - _hole.getPosition().y) < _hole.getRadius() &&
                _ball.isMoving()) {
            System.out.println("Ball in goal");
        }
    }
    public void increaseTurnCount()
    {
        _turns++;
        System.out.println("Turns: " + _turns);
    }
    /**
     Randomizes the start position of the ball
     */
    public void randomizeStartPos(){
        Random random = new Random();
        final int OFFSET = 50;

        int viewportX = MyCrazyPutting.WIDTH/2;
        int viewportY = MyCrazyPutting.HEIGHT/2;
        _hole.setPositionX(random.nextInt(viewportX));
        _hole.setPositionY(random.nextInt(viewportY));
        while(_hole.getPosition().x>viewportX/2-100&&_hole.getPosition().x<viewportX/2+100
                ||_hole.getPosition().x+_hole.getRadius()>viewportX || _hole.getPosition().x-_hole.getRadius()<0){
            _hole.setPositionX(random.nextInt(viewportX));
        }
        while(_hole.getPosition().y>viewportY/2-100&&_hole.getPosition().y<viewportY/2+100
                ||_hole.getPosition().y+_hole.getRadius()>viewportY || _hole.getPosition().y-_hole.getRadius()<0){
            _hole.setPositionY(random.nextInt(viewportY));
        }

        final int minDistanceX = (int)(viewportX*0.5);
        final int minDistanceY = (int)(viewportX*0.5);
        _ball.setPositionX(random.nextInt(viewportX));
        _ball.setPositionY(random.nextInt(viewportY));

        while(Math.abs(_ball.getPosition().x-_hole.getPosition().x)<minDistanceX ||
                OFFSET>_ball.getPosition().x || _ball.getPosition().x>viewportX-OFFSET){
            _ball.setPositionX(random.nextInt(viewportX));
        }
        while(Math.abs(_ball.getPosition().y-_hole.getPosition().y)<minDistanceY ||
                OFFSET>_ball.getPosition().y || _ball.getPosition().y>viewportY-OFFSET){
            _ball.setPositionY(random.nextInt(viewportY));
        }

    }

    public Ball getBall() {
        return _ball;
    }

    public Hole getHole() {
        return _hole;
    }
    public void handleInput(InputData input){
        // later on it should be if speed of the ball is zero (ball is not moving, then input data)
        if(Gdx.input.isKeyJustPressed(Input.Keys.I) && _ball.isMoving() == false){
            Gdx.input.getTextInput(input, "Input data", "", "Input speed and direction separated with space");
        }
        if(input.getText()!=null){
            try{

                String[] data = input.getText().split(" ");
                _ball.setVelocity(Float.parseFloat(data[0]),Float.parseFloat(data[1]));
                input.clearText();//important to clear text or it will overwrite every frame
                if(Float.parseFloat((data[0]))!=0) {
                    _ball.setVelocity(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
                    increaseTurnCount();
                }
                else{
                    float e = (float) 0.0001;
                    _ball.setVelocity(e,Float.parseFloat(data[1]));
                }
                input.clearText();//Important so we dont spam new velocity every time
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
