package com.crazy_putting.game.states;

import com.badlogic.gdx.Gdx;
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
    private int x;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        ball =  new Ball("golfBall.png");
        hole = new Hole();
        sr = new ShapeRenderer();
        create();

    }

    @Override
    public void create(){
//        randomizeStartPos();
        ball.setX(0);
        ball.setY(0);
        x=0;
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(ball.getTexture(), ball.getX(), ball.getY(),20, 20);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(hole.getX(), hole.getY(), hole.getRadius());
        sr.end();
    }

    public void move(int limit){
        if(limit!=x){
            ball.setX(ball.getX()+1);
            ball.setY(ball.getY()+1);
            System.out.println(x);
            x++;
        }

    }

    /**
        Randomizes the start position of the ball
     */
    public void randomizeStartPos(){
        Random random = new Random();
        final int OFFSET = 50;

        hole.setX(random.nextInt(MyCrazyPutting.WIDTH));
        hole.setY(random.nextInt(MyCrazyPutting.HEIGHT));
        while(hole.getX()>MyCrazyPutting.WIDTH/2-100&&hole.getX()<MyCrazyPutting.WIDTH/2+100 ||hole.getX()+hole.getRadius()>MyCrazyPutting.WIDTH || hole.getX()-hole.getRadius()<0){
            hole.setX(random.nextInt(MyCrazyPutting.WIDTH));
        }
        while(hole.getY()>MyCrazyPutting.HEIGHT/2-100&&hole.getY()<MyCrazyPutting.HEIGHT/2+100||hole.getY()+hole.getRadius()>MyCrazyPutting.HEIGHT || hole.getY()-hole.getRadius()<0){
            hole.setY(random.nextInt(MyCrazyPutting.HEIGHT));
        }

        final int minDistanceX = (int)(MyCrazyPutting.WIDTH*0.5);
        final int minDistanceY = (int)(MyCrazyPutting.WIDTH*0.5);
        ball.setX(random.nextInt(MyCrazyPutting.WIDTH));
        ball.setY(random.nextInt(MyCrazyPutting.HEIGHT));

        while(Math.abs(ball.getX()-hole.getX())<minDistanceX || OFFSET>ball.getX() || ball.getX()>MyCrazyPutting.WIDTH-OFFSET){
            ball.setX(random.nextInt(MyCrazyPutting.WIDTH));

        }
        while(Math.abs(ball.getY()-hole.getY())<minDistanceY || OFFSET>ball.getY() || ball.getY()>MyCrazyPutting.HEIGHT-OFFSET){
            ball.setY(random.nextInt(MyCrazyPutting.HEIGHT));
        }

    }

    @Override
    public void dispose() {

    }
}
