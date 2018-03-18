package com.crazy_putting.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Others.InputData;
import com.crazy_putting.game.MyCrazyPutting;

import java.util.Random;

public class PlayState extends State {
    private Ball ball;
    private Hole hole;
    private ShapeRenderer sr;
    private int viewportX;
    private int viewportY;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        input = new InputData();
        ball =  new Ball("golfBall.png");
        hole = new Hole(30);
        sr = new ShapeRenderer();
        viewportX = MyCrazyPutting.WIDTH/2;
        viewportY = MyCrazyPutting.HEIGHT/2;
        create();
        System.out.println("Ball - x: "+ball.getPosition().x+" y: "+ball.getPosition().y);
        System.out.println("Hole - x: "+hole.getPosition().x+" y: "+hole.getPosition().y);
    }

    @Override
    public void create(){
        randomizeStartPos();

        System.out.println(viewportY);
        viewportX = Math.abs((int)(ball.getPosition().x-hole.getPosition().x))+100;
        viewportY = Math.abs((int)(ball.getPosition().y-hole.getPosition().y))+100;
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
        ball.handleInput(input);
    }

    @Override
    public void update(float dt) {
        handleInput();
        ball.update(dt);
        ball.setPositionX(ball.getPosition().x+1);
        ball.setPositionY(ball.getPosition().y+1);
        cam.position.x = Math.min(ball.getPosition().x,hole.getPosition().x);
        cam.position.y = Math.min(ball.getPosition().y,hole.getPosition().y);
//        viewportX = Math.abs(ball.getPosition().x-hole.getPosition().x)+100;
//        viewportY = Math.abs(ball.getPosition().y-hole.getPosition().y)+100

        cam.setToOrtho(false,  viewportX, viewportY);
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(ball.getTexture(), ball.getPosition().x, ball.getPosition().y,20*viewportX/MyCrazyPutting.WIDTH, 20*viewportY/MyCrazyPutting.HEIGHT);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(hole.getPosition().x, hole.getPosition().y, hole.getRadius());
        sr.end();
//        System.out.println(ball.getPosition().x + ball.getPosition().y);
    }
//
//    public void move(int limit){
//        if(limit!=x){
//            ball.setPositionX(ball.getPosition().x+1);
//            ball.setPositionY(ball.getPosition().y+1);
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
    public void dispose() {

    }
}
