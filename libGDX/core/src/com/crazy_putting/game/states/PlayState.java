package com.crazy_putting.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.MyCrazyPutting;
import javafx.scene.shape.Circle;

import java.util.Random;

public class PlayState extends State {
    private Ball ball;
    private Hole hole;
    private ShapeRenderer sr;
    private int viewportX;
    private int viewportY;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        ball =  new Ball("golfBall.png");
        hole = new Hole(30);
        sr = new ShapeRenderer();
        viewportX = MyCrazyPutting.WIDTH/2;
        viewportY = MyCrazyPutting.HEIGHT/2;
        create();
        System.out.println("Ball - x: "+ball.getX()+" y: "+ball.getY());
        System.out.println("Hole - x: "+hole.getX()+" y: "+hole.getY());
    }

    @Override
    public void create(){
        randomizeStartPos();

        System.out.println(viewportY);
        viewportX = Math.abs(ball.getX()-hole.getX())+100;
        viewportY = Math.abs(ball.getY()-hole.getY())+100;
        System.out.println(viewportX);

        cam.setToOrtho(false,  viewportX, viewportY);
//        viewportX = MyCrazyPutting.WIDTH/2;
//        viewportY = MyCrazyPutting.HEIGHT/2;
    }

    @Override
    protected void handleInput() {
//        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            cam.zoom -= 1;
//        }
//        cam.update();
    }

    @Override
    public void update(float dt) {
        handleInput();
        ball.update(dt);
        ball.setX(ball.getX()+1);
        ball.setY(ball.getY()+1);
        cam.position.x = Math.min(ball.getX(),hole.getX());
        cam.position.y = Math.min(ball.getY(),hole.getY());
        viewportX = Math.abs(ball.getX()-hole.getX())+100;
        viewportY = Math.abs(ball.getY()-hole.getY())+100;
        System.out.println(viewportY+ " "+viewportX);
        cam.setToOrtho(false,  viewportX, viewportY);
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(ball.getTexture(), ball.getX(), ball.getY(),20*viewportX/MyCrazyPutting.WIDTH, 20*viewportY/MyCrazyPutting.HEIGHT);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(hole.getX(), hole.getY(), hole.getRadius());
        sr.end();
//        System.out.println(ball.getX() + ball.getY());
    }
//
//    public void move(int limit){
//        if(limit!=x){
//            ball.setX(ball.getX()+1);
//            ball.setY(ball.getY()+1);
//            System.out.println(x);
//            x++;
//        }
//
//    }

    /**
        Randomizes the start position of the ball
     */
    public void randomizeStartPos(){
        Random random = new Random();
        final int OFFSET = 50;

        hole.setX(random.nextInt(viewportX));
        hole.setY(random.nextInt(viewportY));
        while(hole.getX()>viewportX/2-100&&hole.getX()<viewportX/2+100 ||hole.getX()+hole.getRadius()>viewportX || hole.getX()-hole.getRadius()<0){
            hole.setX(random.nextInt(viewportX));
        }
        while(hole.getY()>viewportY/2-100&&hole.getY()<viewportY/2+100||hole.getY()+hole.getRadius()>viewportY || hole.getY()-hole.getRadius()<0){
            hole.setY(random.nextInt(viewportY));
        }

        final int minDistanceX = (int)(viewportX*0.5);
        final int minDistanceY = (int)(viewportX*0.5);
        ball.setX(random.nextInt(viewportX));
        ball.setY(random.nextInt(viewportY));

        while(Math.abs(ball.getX()-hole.getX())<minDistanceX || OFFSET>ball.getX() || ball.getX()>viewportX-OFFSET){
            ball.setX(random.nextInt(viewportX));

        }
        while(Math.abs(ball.getY()-hole.getY())<minDistanceY || OFFSET>ball.getY() || ball.getY()>viewportY-OFFSET){
            ball.setY(random.nextInt(viewportY));
        }

    }

    @Override
    public void dispose() {

    }
}
