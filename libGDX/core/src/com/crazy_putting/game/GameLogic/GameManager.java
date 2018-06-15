
package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Bot.Bot;
import com.crazy_putting.game.Bot.GeneticAlgorithm;
import com.crazy_putting.game.Components.Graphics.Graphics2DComponent;
import com.crazy_putting.game.Components.Graphics.SphereGraphics3DComponent;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Others.InputData;
import com.crazy_putting.game.Others.Velocity;
import com.crazy_putting.game.Parser.ReadAndAnalyse;
import com.crazy_putting.game.Physics.Physics;
import com.crazy_putting.game.Screens.GolfGame;
import com.crazy_putting.game.Screens.MenuScreen;

public class GameManager {

    private Ball _ball;
    private Hole _hole;
    private GolfGame _game;
    private int _turns;
    private int _mode;
    private Bot bot;
    private boolean printMessage = true;
    public GameManager(GolfGame pGame, int pMode){
        _mode = pMode;
        _game = pGame;
        if (_mode == 2)
            ReadAndAnalyse.calculate("myFile.txt");
        initGameObjects();
        _turns = 0;
        Physics.physics.updateCoefficients();
    }
    private void initGameObjects(){
        _ball = new Ball(CourseManager.getStartPosition());
        _hole = new Hole((int) CourseManager.getActiveCourse().getGoalRadius(), CourseManager.getGoalStartPosition());
        if(MenuScreen.Mode3D ) {//3D Logic
            _ball.addGraphicComponent(new SphereGraphics3DComponent(40, Color.WHITE));
            _hole.addGraphicComponent(new SphereGraphics3DComponent(40,Color.BLACK));
        }
        else{//2D Logic
            _ball.addGraphicComponent(new Graphics2DComponent(new Texture("golfBall.png")));
            _hole.addGraphicComponent(new Graphics2DComponent(new Texture("hole.png"), _hole.getRadius() * 2, _hole.getRadius() * 2));
        }
    }
    public void update(float pDelta){
        if(pDelta > 0.03){
            pDelta = 0.00166f;
        }
       handleInput(_game.input);

        Physics.physics.update(pDelta);
        if(printMessage){
            updateGameLogic(pDelta);
        }
    }
    //TODO blazej or Simon, is here where we stop the ball? otherwise we can erase this
    public void updateGameLogic(float pDelta){
        if(isBallInTheHole(_ball,_hole) && _ball.isSlow()) {
            printMessage = false;
            _ball.fix(true);
            _ball.setVelocityComponents(0,0);
            System.out.println("Ball in goal");
            _ball.fix(true);
        }
    }

    //TODO move to input class?
    //TODO fix GA in AI mode
    public void handleInput(InputData input){
        // later on it should be if speed of the ball is zero (ball is not moving, then input data)
            if(_mode == 1) {

                if (Gdx.input.isKeyJustPressed(Input.Keys.G) && !_ball.isMoving()){
                    System.out.println(_ball.getPosition().x + "  " + _ball.getPosition().y);

                    GeneticAlgorithm GA = new GeneticAlgorithm(_hole, CourseManager.getActiveCourse());

                    Ball b = GA.getBestBall();
                    float speed = b.getVelocityGA().speed;
                    float angle = b.getVelocityGA().angle;
                    _ball.setVelocity(speed,angle);
                    _ball.fix(false);

                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !_ball.isMoving()) {
              //CourseManager.reWriteCourse();//TODO: CHECK WHY THIS IS HERE
              Gdx.input.getTextInput(input, "Input data", "", "Input speed and direction separated with space");
            }
            if (input.getText() != null) {
                try {
                    String[] data = input.getText().split(" ");
                    float speed = Float.parseFloat(data[0]);
                    float angle = Float.parseFloat(data[1]);
                    input.clearText();//important to clear text or it will overwrite every frame
                    checkConstrainsAndSetVelocity(speed, angle);
                  //  input.clearText();//important to clear text or it will overwrite every frame

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
            if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !_ball.isMoving()){
                bot = new Bot(_ball,_hole, CourseManager.getActiveCourse());
                bot.computeOptimalVelocity();
                Velocity computedVelocity = bot.getBestBall().getVelocity();
                Gdx.app.log("Ball","Position x "+ _ball.getPosition().x+" position y "+_ball.getPosition().y);
                checkConstrainsAndSetVelocity(computedVelocity.speed, computedVelocity.angle);
                Gdx.app.log("Manager","speed "+computedVelocity.speed+" angle "+computedVelocity.angle);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.G) && !_ball.isMoving()){

                GeneticAlgorithm GA = new GeneticAlgorithm(_hole,CourseManager.getActiveCourse());
                Ball b = GA.getBestBall();
                float speed = b.getVelocityGA().speed;
                float angle = b.getVelocityGA().angle;
                _ball.setVelocity(speed,angle);
                _ball.fix(false);
            }
        }
    }
    public static boolean isBallInTheHole(Ball ball, Hole hole){
        if(Math.sqrt(Math.pow(ball.getPosition().x -hole.getPosition().x,2) +Math.pow((ball.getPosition().y - hole.getPosition().y),2)+Math.pow((ball.getPosition().z - hole.getPosition().z),2))< hole.getRadius()){
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
    public void increaseTurnCount(){
        _turns++;
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
    /////////////////////////////////////////////////////////////////////
    //////////Methods for spline Edit Mode//////////////////////////////
    ////////////////////////////////////////////////////////////////////

    /**
     * Overwrite the position of ball and hole when saving the new coordinates of the edited course by spplines
     */
    public void saveBallAndHolePos(){
        CourseManager.getActiveCourse().setBallStartPos(_ball.getPosition());
        CourseManager.getActiveCourse().setGoalPosition(_hole.getPosition());
    }

    /**
     *  Updates the height position of the ball and hole after the course changed using spline editor
     */
    public void updateObjectPos(){
        _ball.getPosition().z = CourseManager.calculateHeight(_ball.getPosition().x,_ball.getPosition().y);
        _hole.getPosition().z = CourseManager.calculateHeight(_hole.getPosition().x,_hole.getPosition().y);
    }

    /**
     * Change the position of the ball when using the change ball position editor mode
     * @param pos
     */
    public void updateBallPos(Vector3 pos){
        _ball.setPosition(pos);
    }

    /**
     * Change the position of the hole when using the change hole position editor mode
     * @param pos
     */
    public void updateHolePos(Vector3 pos){
        _hole.setPosition(pos);
    }

}
