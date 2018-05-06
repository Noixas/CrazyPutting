
package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.crazy_putting.game.Bot.Bot;
import com.crazy_putting.game.Components.GraphicsComponent;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Others.InputData;
import com.crazy_putting.game.Others.Velocity;
import com.crazy_putting.game.Parser.ReadAndAnalyse;
import com.crazy_putting.game.Physics.Physics;
import com.crazy_putting.game.Screens.GolfGame;

import java.util.Random;

public class GameManager {

    private Ball _ball;
    private Hole _hole;
    private GolfGame _game;
    private int _turns;
    private int _mode;
    private Bot bot;
    private boolean printMessage=true;
    public GameManager(GolfGame pGame, int pMode)
    {
        _mode = pMode;
        _ball = new Ball("golfBall.png");
        _game = pGame;
        _hole = new Hole((int)CourseManager.getActiveCourse().getGoalRadius());
        _turns = 0;
        Physics.updateCoefficients();
        System.out.println("Is that radius? "+(int)CourseManager.getActiveCourse().getGoalRadius());
        _ball.addGraphicComponent(new GraphicsComponent( _ball.getTexture()));
        _hole.addGraphicComponent(new GraphicsComponent(new Texture("hole.png"), _hole.getRadius()*2, _hole.getRadius()*2));
        _hole.setPosition(CourseManager.getGoalStartPosition());
        _ball.setPosition(CourseManager.getStartPosition());
        if(_mode == 2){
            ReadAndAnalyse.calculate("myFile.txt");

        }
        if (_mode == 3) {
            bot = new Bot(_ball, _hole, CourseManager.getActiveCourse());
        }

    }
    public void Update(float pDelta)
    {
        handleInput(_game.input);
        _ball.update(pDelta);
        Physics.update(_ball, pDelta);
        if(printMessage){
            UpdateGameLogic(pDelta);
        }

    }
    public void UpdateGameLogic(float pDelta)
    {
        if(isBallInTheHole(_ball,_hole) && _ball.isSlow()) {
            printMessage = false;
            System.out.println("Ball in goal");

            _ball.fix(true);
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

        int viewportX = GraphicsManager.WINDOW_WIDTH/2;
        int viewportY = GraphicsManager.WINDOW_HEIGHT/2;
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

    public int getTurns(){
        return _turns;
    }

    public Hole getHole() {
        return _hole;
    }
    public void handleInput(InputData input){
        // later on it should be if speed of the ball is zero (ball is not moving, then input data)
        if(_mode == 1) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !_ball.isMoving()) {
              CourseManager.reWriteCourse();//TODO: CHECK WHY THIS IS HERE
              Gdx.input.getTextInput(input, "Input data", "", "Input speed and direction separated with space");
            }
            if (input.getText() != null) {
                try {

                    String[] data = input.getText().split(" ");
                    float speed = Float.parseFloat(data[0]);
                    float angle = Float.parseFloat(data[1]);
                    input.clearText();//important to clear text or it will overwrite every frame
                    checkConstrainsAndSetVelocity(speed, angle);
//                    I commented the next line, because it seems to be a duplicate of the code two lines above
//                    input.clearText();//Important so we dont spam new velocity every time
                }
                catch (NumberFormatException e) {
                    // later on this will be added on the game screen so that it wasn't printed multiple times
                    // after doing this change, delete printing stack trace
                    Gdx.app.error("Exception: ", "You must input numbers");
                    e.getStackTrace();
                }
            }
        }
        else if(_mode == 2){
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
                System.out.println("MODE "+_mode+" with N: " + ReadAndAnalyse.getN());
                if(!_ball.isMoving() && _turns<ReadAndAnalyse.getN()) {
                    _ball.setVelocity(ReadAndAnalyse.getResult()[_turns][0], ReadAndAnalyse.getResult()[_turns][1]);
                    _ball.fix(false);
                    increaseTurnCount();
                }
                else if(_turns>=ReadAndAnalyse.getN()){
                  System.out.println("No more moves...");
                }
            }
        }
        else if (_mode == 3){
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
                Velocity computedVelocity = bot.computeVelocity();
                checkConstrainsAndSetVelocity(computedVelocity.speed, computedVelocity.angle);
            }
        }
    }

    public static boolean isBallInTheHole(Ball ball, Hole hole){
        if(Math.sqrt(Math.pow(ball.getPosition().x -hole.getPosition().x,2) +Math.pow((ball.getPosition().y - hole.getPosition().y),2))< hole.getRadius()){
            return true;
        }
        return false;
    }

    public void checkConstrainsAndSetVelocity(float inputSpeed, float inputAngle) {
        float speed = checkMaxSpeedConstrain(inputSpeed);
        if(speed==0) {
            speed=0.000001f;
        }
        increaseTurnCount();
        _ball.setVelocity(speed, inputAngle);
        _ball.fix(false);
    }

    public float checkMaxSpeedConstrain(float speed){
        if(speed > CourseManager.getMaxSpeed()){
            speed = CourseManager.getMaxSpeed();
        }
        return speed;
    }

}
