package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.graphics.Texture;
import com.crazy_putting.game.Components.GraphicsComponent;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.MyCrazyPutting;
import com.crazy_putting.game.Physics.PhysicsGenericFormulaTest;
import com.crazy_putting.game.Screens.GolfGame;

import java.util.Random;

public class GameManager {

    private Ball _ball;
    private Hole _hole;
    private GolfGame _game;

    public GameManager(GolfGame pGame)
    {
     _ball = new Ball("golfBall.png");
     _game = pGame;
     _hole = new Hole(30);
     
    _ball.addGraphicComponent(new GraphicsComponent( _ball.getTexture()));
    _hole.addGraphicComponent(new GraphicsComponent( new Texture("hole.png"), _hole.getRadius()*2, _hole.getRadius()*2));

        randomizeStartPos();
    }
    public void Update(float pDelta)
    {
        _ball.handleInput(_game.input);
        _ball.update(pDelta);
        PhysicsGenericFormulaTest.update(_ball, pDelta);
        UpdateGameLogic(pDelta);

    }
    private void UpdateGameLogic(float pDelta)
    {
        if(Math.abs(_ball.getPosition().x - _hole.getPosition().x) < _hole.getRadius() &&
                Math.abs(_ball.getPosition().y - _hole.getPosition().y) < _hole.getRadius() &&
                _ball.isMoving())
            System.out.println("Ball in goal");
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
}
