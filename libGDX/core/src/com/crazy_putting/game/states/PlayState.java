package com.crazy_putting.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.crazy_putting.game.MyCrazyPutting;
import javafx.scene.shape.Circle;

import java.util.Random;

public class PlayState extends State {
    private Texture ball;
    private int currentPosX;
    private int currentPosY;
    private ShapeRenderer sr;
    private final int holeRadius = 100;
    private int holePosX;
    private int holePosY;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        ball =  new Texture("golfBall.png");
        sr = new ShapeRenderer();
        create();

    }

    @Override
    public void create(){
        int[] startPos = randomizeStartPos();
        currentPosX = startPos[0];
        currentPosY = startPos[1];
        holePosX = startPos[2];
        holePosY = startPos[3];
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
        sb.draw(ball, currentPosX, currentPosY,20, 20);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(holePosX, holePosY, holeRadius);
        sr.end();
    }

    /**
        Randomizes the start position of the ball
     */
    public int[] randomizeStartPos(){
        Random random = new Random();
        final int OFFSET = 50;

        int holeX = random.nextInt(MyCrazyPutting.WIDTH);
        int holeY = random.nextInt(MyCrazyPutting.HEIGHT);
        while(holeX>MyCrazyPutting.WIDTH/2-100&&holeX<MyCrazyPutting.WIDTH/2+100 ||holeX+holeRadius>MyCrazyPutting.WIDTH || holeX-holeRadius<0){
            holeX = random.nextInt(MyCrazyPutting.WIDTH);
        }
        while(holeY>MyCrazyPutting.HEIGHT/2-100&&holeY<MyCrazyPutting.HEIGHT/2+100||holeY+holeRadius>MyCrazyPutting.HEIGHT || holeY-holeRadius<0){
            holeY = random.nextInt(MyCrazyPutting.HEIGHT);
        }

        final int minDistanceX = (int)(MyCrazyPutting.WIDTH*0.5);
        final int minDistanceY = (int)(MyCrazyPutting.WIDTH*0.5);
        int randomX = random.nextInt(MyCrazyPutting.WIDTH);
        int randomY = random.nextInt(MyCrazyPutting.WIDTH);

        while(Math.abs(randomX-holeX)<minDistanceX || OFFSET>randomX || randomX>MyCrazyPutting.WIDTH-OFFSET){
            randomX = random.nextInt(MyCrazyPutting.WIDTH);

        }
        while(Math.abs(randomY-holeY)<minDistanceY || OFFSET>randomY || randomY>MyCrazyPutting.HEIGHT-OFFSET){
            randomY = random.nextInt(MyCrazyPutting.HEIGHT);
        }

        int[] randomPos = {randomX, randomY, holeX, holeY};
        return randomPos;
    }

    @Override
    public void dispose() {

    }
}
