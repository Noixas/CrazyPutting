package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.MyCrazyPutting;
import com.crazy_putting.game.Others.InputData;
import com.crazy_putting.game.Physics.PhysicsTest;

import java.util.Random;

public class GameScreen implements Screen {
    private Ball ball;
    private Hole hole;
    final GolfGame game;
    private ShapeRenderer sr;
    private PhysicsTest physics;
    private int viewportX;
    private int viewportY;
    OrthographicCamera cam;

    public GameScreen(GolfGame game) {
        cam = new OrthographicCamera();
        physics = new PhysicsTest();
        this.game = game;

        ball =  new Ball("golfBall.png");
        hole = new Hole(30);
        sr = new ShapeRenderer();
        viewportX = MyCrazyPutting.WIDTH/2;
        viewportY = MyCrazyPutting.HEIGHT/2;
        randomizeStartPos();
//
//        System.out.println(viewportY);
//        viewportX = Math.abs((int)(ball.getPosition().x-hole.getPosition().x))+100;
//        viewportY = Math.abs((int)(ball.getPosition().y-hole.getPosition().y))+100;
//        System.out.println(viewportX);

        cam.setToOrtho(false,  viewportX, viewportY);
//        viewportX = MyCrazyPutting.WIDTH/2;
//        viewportY = MyCrazyPutting.HEIGHT/2;
        System.out.println("Ball - x: "+ball.getPosition().x+" y: "+ball.getPosition().y);
        System.out.println("Hole - x: "+hole.getPosition().x+" y: "+hole.getPosition().y);
    }

    /**
     Randomizes the start position of the ball
     */
    public void randomizeStartPos(){
        Random random = new Random();
        final int OFFSET = 50;

        hole.setPositionX(random.nextInt(viewportX));
        hole.setPositionY(random.nextInt(viewportY));
        while(hole.getPosition().x>viewportX/2-100&&hole.getPosition().x<viewportX/2+100 ||hole.getPosition().x+hole.getRadius()>viewportX || hole.getPosition().x-hole.getRadius()<0){
            hole.setPositionX(random.nextInt(viewportX));
        }
        while(hole.getPosition().y>viewportY/2-100&&hole.getPosition().y<viewportY/2+100||hole.getPosition().y+hole.getRadius()>viewportY || hole.getPosition().y-hole.getRadius()<0){
            hole.setPositionY(random.nextInt(viewportY));
        }

        final int minDistanceX = (int)(viewportX*0.5);
        final int minDistanceY = (int)(viewportX*0.5);
        ball.setPositionX(random.nextInt(viewportX));
        ball.setPositionY(random.nextInt(viewportY));

        while(Math.abs(ball.getPosition().x-hole.getPosition().x)<minDistanceX || OFFSET>ball.getPosition().x || ball.getPosition().x>viewportX-OFFSET){
            ball.setPositionX(random.nextInt(viewportX));
        }
        while(Math.abs(ball.getPosition().y-hole.getPosition().y)<minDistanceY || OFFSET>ball.getPosition().y || ball.getPosition().y>viewportY-OFFSET){
            ball.setPositionY(random.nextInt(viewportY));
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ball.handleInput(game.input);
        physics.update(ball,delta);
        int red = 34;
        int green = 137;
        int blue = 34;
        Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        game.batch.draw(ball.getTexture(), ball.getPosition().x, ball.getPosition().y,20*viewportX/MyCrazyPutting.WIDTH, 20*viewportY/MyCrazyPutting.HEIGHT);
        game.batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(hole.getPosition().x, hole.getPosition().y, hole.getRadius());
        sr.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
