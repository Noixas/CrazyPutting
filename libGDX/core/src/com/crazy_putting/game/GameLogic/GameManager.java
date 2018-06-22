
package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Bot.Bot;
import com.crazy_putting.game.Bot.GeneticAlgorithm;
import com.crazy_putting.game.Bot.MazeBot;
import com.crazy_putting.game.Components.Colliders.CollisionManager;
import com.crazy_putting.game.Components.Colliders.SphereCollider;
import com.crazy_putting.game.Components.Graphics.Graphics2DComponent;
import com.crazy_putting.game.Components.Graphics.SphereGraphics3DComponent;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.GameObject;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Others.InputData;
import com.crazy_putting.game.Others.MultiplayerSettings;
import com.crazy_putting.game.Others.Velocity;
import com.crazy_putting.game.Parser.ReadAndAnalyse;
import com.crazy_putting.game.Physics.Physics;
import com.crazy_putting.game.Screens.GolfGame;
import com.crazy_putting.game.Screens.MenuScreen;

import java.util.ArrayList;

public class GameManager {

    private Ball _ball;
    private Hole _hole;
    private GolfGame _game;
    private int _turns;
    private int _mode;
    private Bot bot;

    private int nPlayers;
    private int allowedDistance;
    private Ball[] allBalls;
    private Hole[] allHoles;
    private float[][] allInput;
    private double[][] distancesMatrix;
    private Vector3[] cachePositions;
    private ArrayList<Velocity> mazeVelocities = new ArrayList<Velocity>();

    private int _player;
    private Vector3 _cachePosition;

    public GameManager(GolfGame pGame, int pMode){
        _mode = pMode;
        _game = pGame;
        if (_mode == 4) {
            nPlayers = MultiplayerSettings.PlayerAmount;
            allowedDistance = MultiplayerSettings.AllowedDistance;
        }
        else { nPlayers = 1; }
        if (_mode == 2)
            ReadAndAnalyse.calculate("myFile.txt");
        initGameObjects();
        _turns = 0;
        Physics.physics.updateCoefficients();
    }

    private void initGameObjects(){
        allBalls = new Ball[nPlayers];
        allHoles = new Hole[nPlayers];
        distancesMatrix = new double[nPlayers][nPlayers];
        cachePositions = new Vector3[nPlayers];
        if (_mode==4 && MultiplayerSettings.Simultaneous)
            allInput = new float[nPlayers][2];
        else
            allInput = new float[1][2];
        CourseManager.initObstacles();
  //      do {
            System.out.println("Setup");
            for (int i = 0; i < nPlayers; i++) {
                if(allBalls[i] != null){
                    allBalls[i].destroy();
                }
                allBalls[i] = new Ball((CourseManager.getStartPosition(i)));
                allHoles[i] = new Hole((int) CourseManager.getActiveCourse().getGoalRadius(), (CourseManager.getGoalStartPosition(i)));
                System.out.println("Balls "+allBalls[i].getPosition().x+" "+allBalls[i].getPosition().y);
                System.out.println("Hole "+allHoles[i].getPosition().x+" "+allHoles[i].getPosition().y);
            }
            keepBallsWithinDistances();
     //   } while (_mode==4 && (!checkLegitimacy()&&false));
        if(MenuScreen.Mode3D ) {//3D Logic
         // if we are in multiplayer mode
            for (int i = 0; i < nPlayers; i++) {
                    allBalls[i].addGraphicComponent(new SphereGraphics3DComponent(40, Color.WHITE));
                    SphereCollider sphere = new SphereCollider(CourseManager.getStartPosition(i),20);
                    allBalls[i].addColliderComponent(sphere);
                    allHoles[i].addGraphicComponent(new SphereGraphics3DComponent(40, Color.BLACK));
            }
        }
        else{//2D Logic
            for (int i = 0; i < nPlayers; i++) {
                allBalls[i].addGraphicComponent(new Graphics2DComponent(new Texture("golfBall.png")));
                allHoles[i].addGraphicComponent(new Graphics2DComponent(new Texture("hole.png"), allHoles[i].getRadius() * 2, allHoles[i].getRadius() * 2));
            }
        }
        _ball = allBalls[0];
        _hole = allHoles[0];
        _player = 0;
    }
    /*
     If the balls are further away than they should then we find the centroid and
     position the balls relative to their position and centroid but within the allowed distance
     */
    public void keepBallsWithinDistances(){

        if(checkLegitimacy()) return;
        Vector3 middle = new Vector3();
        for(int i = 0; i<nPlayers;i++){
            middle.add(allBalls[i].getPosition());
        }
        middle.x = middle.x/nPlayers;
        middle.y = middle.y/nPlayers;
        middle.z = middle.z/nPlayers;
        for(int i = 0; i<nPlayers;i++){
            allBalls[i].destroy();
            allBalls[i] = new Ball(new Vector3(middle).add(new Vector3(middle).sub(allBalls[i].getPosition()).nor().scl(allowedDistance/2)));

        }
    }
    public void update(float pDelta){
        if(pDelta > 0.03){
            pDelta = 0.00166f;
        }
        if(mazeVelocities.size()==0){
            handleInput(_game.input);
        }
        else{
            if(!_ball.isMoving()){
                _ball.setVelocity(mazeVelocities.get(0));
                _ball.fix(false);
                mazeVelocities.remove(0);
            }
        }
        if (_mode == 4 && !MultiplayerSettings.Simultaneous)
            Physics.physics.updateObject(_ball, pDelta);
        else
            Physics.physics.update(pDelta);
        CollisionManager.update();
        updateGameLogic(pDelta);
        if (_mode == 4)
            multiPlayerUpdate(pDelta);
    }

    //TODO blazej or Simon, is here where we stop the ball? otherwise we can erase this
    public void updateGameLogic(float pDelta){
        if (_mode == 4 && MultiplayerSettings.Simultaneous){
            int i = 0;
            while (i < nPlayers) {
                if (isBallInTheHole(allBalls[i], allHoles[i]) && allBalls[i].isSlow()) {
                    ballIsDone(allBalls[i]);
                }
                i++;
            }
        }
        else{
            if (isBallInTheHole(_ball, _hole) && _ball.isSlow()) {
                ballIsDone(_ball);
            }
        }
    }

    private void ballIsDone(Ball ball){
        System.out.println("Ball in goal");
        ball.setVelocityComponents(0, 0);
        ball.fix(true);
        ball.enabled = false;
        Physics.physics.removeMovableObject(ball);
    }

    //TODO move to input class?
    //TODO fix GA in AI mode
    public void handleInput(InputData input){
        // later on it should be if speed of the ball is zero (ball is not moving, then input data)
        if(_mode == 1) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.G) && !_ball.isMoving()){
                System.out.println(_ball.getPosition().x + "  " + _ball.getPosition().y);
                GeneticAlgorithm GA = new GeneticAlgorithm(_hole, CourseManager.getActiveCourse(),CourseManager.getStartPosition(0));
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
                    allInput[0][0] = speed;
                    allInput[0][1] = angle;
                    input.clearText();//important to clear text or it will overwrite every frame
                    checkConstrainsAndSetVelocity(allInput);
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
                bot = new Bot(_ball,_hole, CourseManager.getActiveCourse(), CourseManager.getStartPosition(0));
                bot.computeOptimalVelocity();
                Velocity computedVelocity = bot.getBestBall().getVelocity();
                Gdx.app.log("Ball","Position x "+ _ball.getPosition().x+" position y "+_ball.getPosition().y);
                allInput[0][0] = computedVelocity.speed;
                allInput[0][1] = computedVelocity.angle;
                checkConstrainsAndSetVelocity(allInput);
                Gdx.app.log("Manager","speed "+computedVelocity.speed+" angle "+computedVelocity.angle);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.G) && !_ball.isMoving()){
                // TODO
                MazeBot mazeBot = new MazeBot(_ball,_hole,CourseManager.getActiveCourse());
                mazeVelocities = mazeBot.findSolution();
            }
        }
        else if(_mode == 4 && !MultiplayerSettings.Simultaneous) {
            changePlayer();
            _ball = allBalls[_player];
            _hole = allHoles[_player];
                if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !anyBallIsMoving()) {
                    if (!isBallInTheHole(_ball, _hole))
                        Gdx.input.getTextInput(input, "Input data", "", "For player " + (_player + 1) + ": input speed and direction separated with space");
                }
                if (input.getText() != null) {
                    try {
                        String[] data = input.getText().split(" ");
                        float speed = Float.parseFloat(data[0]);
                        float angle = Float.parseFloat(data[1]);
                        allInput[0][0] = speed;
                        allInput[0][1] = angle;
                        input.clearText();//important to clear text or it will overwrite every frame
                        copyPreviousPosition();
                        checkConstrainsAndSetVelocity(allInput);
                    } catch (NumberFormatException e) {
                        // later on this will be added on the game screen so that it wasn't printed multiple times
                        // after doing this change, delete printing stack trace
                        Gdx.app.error("Exception: ", "You must input numbers");
                        e.getStackTrace();
                    }
                }
        }
        else if(_mode == 4 && MultiplayerSettings.Simultaneous) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !anyBallIsMoving()) {
                Gdx.input.getTextInput(input, "Input data", "", "For all Players: input speed and direction separated with space");
            }
            if (input.getText() != null) {
                try {
                    String[] data = input.getText().split(" ");
                    for (int i=0; i<nPlayers; i++) {
                        float speed = Float.parseFloat(data[i*2]);
                        float angle = Float.parseFloat(data[i*2+1]);
                        allInput[i][0] = speed;
                        allInput[i][1] = angle;
                        input.clearText();//important to clear text or it will overwrite every frame
                    }
                    copyPreviousPosition();
                    checkConstrainsAndSetVelocity(allInput);
                }
                catch (NumberFormatException e) {
                    // later on this will be added on the game screen so that it wasn't printed multiple times
                    // after doing this change, delete printing stack trace
                    Gdx.app.error("Exception: ", "You must input numbers");
                    e.getStackTrace();
                }
            }
        }
    }

    public static boolean isBallInTheHole(Ball ball, Hole hole){
        if(Math.sqrt(Math.pow(ball.getPosition().x -hole.getPosition().x,2) +Math.pow((ball.getPosition().y - hole.getPosition().y),2)+Math.pow((ball.getPosition().z - hole.getPosition().z),2))< hole.getRadius()){
            return true;
        }
        return false;
    }
    public Ball getPlayer(int pPlayer){
        return allBalls[pPlayer];
    }
    public void checkConstrainsAndSetVelocity(float[][] input) {
        for (int i=0; i<input.length; i++) {
            float speed = checkMaxSpeedConstrain(input[i][0]);
            float angle = input[i][1];
            if (speed == 0) {
                speed = 0.000001f;
            }
            if (_mode == 4 && MultiplayerSettings.Simultaneous) {
                allBalls[i].setVelocity(speed, angle);
                allBalls[i].fix(false);
            }
            else{
                _ball.setVelocity(speed, angle);

                _ball.fix(false);
            }
        }
        if (nPlayers==1 || MultiplayerSettings.Simultaneous || _player+1==nPlayers)
        increaseTurnCount();
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
        for (int i=0; i<nPlayers; i++) {
            CourseManager.getActiveCourse().setBallStartPos(allBalls[i].getPosition(),i);
            CourseManager.getActiveCourse().setGoalPosition(allHoles[i].getPosition(),i);
        }
    }

    /**
     *  Updates the height position of the ball and hole after the course changed using spline editor
     */
    public void updateObjectPos(){
        for (int i=0; i<nPlayers; i++) {
            Ball _ball = allBalls[i];
            Hole _hole = allHoles[i];
            _ball.getPosition().z = CourseManager.calculateHeight(_ball.getPosition().x, _ball.getPosition().y);
            _hole.getPosition().z = CourseManager.calculateHeight(_hole.getPosition().x, _hole.getPosition().y);
        }
    }

    /**
     * Change the position of the ball when using the change ball position editor mode
     * @param pos
     */
    public void updateBallPos(Vector3 pos, int pPlayer){

        Vector3 cache = allBalls[pPlayer].getPosition();
        allBalls[pPlayer].setPosition(pos);
        if(checkDistances(allBalls)==false)
            allBalls[pPlayer].setPosition(cache);
    }

    /**
     * Change the position of the hole when using the change hole position editor mode
     * @param pos
     */
    public void updateHolePos(Vector3 pos, int pPlayer){
        Vector3 cache = allHoles[pPlayer].getPosition();
        allHoles[pPlayer].setPosition(pos);
        if(checkDistances(allHoles)==false)
            allHoles[pPlayer].setPosition(cache);
    }

    /////////////////////////////////////////////////////////////////////
    //////////Methods for multiple players Mode//////////////////////////////
    ////////////////////////////////////////////////////////////////////

    public int getActivePlayerIndex(){
        return _player;
    }
    public boolean checkDistances(GameObject[] balls){
        //TODO: can improve the update depends if simultaneous or not
        if (nPlayers==1)
            return true;
        for (int i=0; i<nPlayers; i++){
            for (int j=0; j<nPlayers; j++) {
                double d = euclideanDistance(balls[i].getPosition(), balls[j].getPosition());
                distancesMatrix[i][j] = d;
                if (distancesMatrix[i][j] > allowedDistance && allBalls[i].enabled && allBalls[j].enabled)
                    return false;
            }
        }
        return true;
    }

    public double euclideanDistance(Vector3 start, Vector3 goal){
        double d = (float) Math.sqrt(Math.pow(start.x-goal.x,2)+Math.pow(start.y-goal.y,2)+Math.pow(start.z-goal.z,2));
        return d;
    }

    public boolean anyBallIsMoving(){
        for (int i=0; i<nPlayers; i++) {
            if (allBalls[i].isMoving()) {
                return true;
            }
        }
        return false;
    }

    public void changeActiveBallAndHole(int n){
        if (n >= allBalls.length) return;
        _ball = allBalls[n];
        _hole = allHoles[n];
    }
    public int getAmountPlayers(){
        return nPlayers;
    }
    public void multiPlayerUpdate(double pDelta){
        if (!anyBallIsMoving() && !checkDistances(allBalls)){
            System.out.println("Exceeding the allowed distance from each other. Please try again.");
            returnToPreviousPosition();
            // TODO: display UI massage
        }
        // TODO: exceeding depends on simultaneous or not
    }

    public void copyPreviousPosition(){
        if (MultiplayerSettings.Simultaneous) {
            for (int i = 0; i < nPlayers; i++) {
                cachePositions[i] = new Vector3(allBalls[i].getPosition());
            }
        }
        else{
            _cachePosition = new Vector3(_ball.getPosition());
        }
    }

    public void returnToPreviousPosition(){
        if (MultiplayerSettings.Simultaneous) {
            for (int i = 0; i < nPlayers; i++) {
                allBalls[i].setPosition(cachePositions[i]);
            }
        }
        else{
            _ball.setPosition(_cachePosition);
        }
    }

    public Vector3 createPosition(Vector3 p) {
        float size = CourseManager.getCourseDimensions().x / 2;
        float x;
        float y;
        float z;
        do {
            x = (float) (p.x + Math.random() * allowedDistance/2);
            y = (float) (p.y + Math.random() * allowedDistance/2);
            z = CourseManager.calculateHeight(x, y);
        } while (x<-1*size || x>size || y<-1*size || y>size || z<0);
        return new Vector3(x, y, z);
    }

    public boolean checkLegitimacy(){
        if (checkDistances(allBalls)==false || checkDistances(allHoles)==false)
            return false;
        int a = 0;
        for (Hole element: allHoles){
            for (Hole element2: allHoles){
                a++;
                double d = euclideanDistance(element.getPosition(),element2.getPosition());
                if ( d>0 && d < element.getRadius()*2)
                    return false;
            }
        }
        return true;
    }

    public void changePlayer(){
        if (_player+1 == nPlayers)
            _player = 0;
        else
            _player++;
    }

}
