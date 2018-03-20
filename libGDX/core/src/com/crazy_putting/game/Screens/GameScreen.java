package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.MyCrazyPutting;
import com.crazy_putting.game.Physics.PhysicsTest;

import java.util.Random;

public class GameScreen extends InputAdapter implements Screen {
    private Ball ball;
    private Hole hole;
    final GolfGame game;
    private ShapeRenderer sr;
    private int viewportX;
    private int viewportY;
    private Texture texture;
    private Pixmap pixmap;
    OrthographicCamera cam;

    public GameScreen(GolfGame game) {
        cam = new OrthographicCamera();
        this.game = game;
        ball =  new Ball("golfBall.png");
        hole = new Hole(30);
        sr = new ShapeRenderer();
        viewportX = MyCrazyPutting.WIDTH;
        viewportY = MyCrazyPutting.HEIGHT;
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
        Gdx.input.setInputProcessor(this);
        drawHeightMap();
    }

    public float height(float x, float y){
        float height = (float)(0.1*x + 0.03*Math.pow(x,2)+y*0.2);
//        System.out.println(height);
        return height;
    }

    @Override
    public void render(float delta) {
        ball.handleInput(game.input);
        ball.update(delta);
        PhysicsTest.update(ball, delta);
        int red = 34;
        int green = 137;
        int blue = 34;
        Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);

//        batch.begin();
//        if(texture!=null){
//            batch.draw(texture, 0, 0);
//        }
//        batch.end();
        game.batch.begin();
        if(texture!=null){
            game.batch.draw(texture, 0, 0);
        }

        game.batch.draw(ball.getTexture(), ball.getPosition().x, ball.getPosition().y,20*viewportX/MyCrazyPutting.WIDTH, 20*viewportY/MyCrazyPutting.HEIGHT);
        game.font.draw(game.batch, "Speed: "+Math.round(Math.sqrt(Math.pow(ball.getVelocity().Vx,2)+Math.pow(ball.getVelocity().Vy,2))), viewportX-150, viewportY-10);
        game.font.draw(game.batch, "Height: "+Math.round(height(ball.getPosition().x,ball.getPosition().y)), viewportX-150, viewportY-30);
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

    public void drawHeightMapTest(){
        pixmap = new Pixmap(700,700, Pixmap.Format.RGB888);
        texture = new Texture(pixmap);
        for(int i = 0; i<pixmap.getWidth();i++){
            for(int j = 0; j<pixmap.getHeight();j++){
//                if(i==0&&j==0){
//                    pixmap.setColor(Color.BLACK);
//                    pixmap.drawCircle(i,j,100);
//                }
                if(height(i,j)>0 && height(i,j)<1000){
                    pixmap.setColor(Color.BROWN);
                    pixmap.drawCircle(i,j,90);
                    System.out.println(height(i,j));

                }
                else if(height(i,j)>1000 && height(i,j)<3000){
                    pixmap.setColor(Color.PURPLE);
                    pixmap.drawPixel(i,j);

                }
                else if(height(i,j)>3000 && height(i,j)<4000){
                    pixmap.setColor(Color.BLACK);
                    pixmap.drawPixel(i,j);

                }
                else if(height(i,j)>4000 && height(i,j)<5000){
                    pixmap.setColor(Color.PINK);
                    pixmap.drawPixel(i,j);
                }
                else if (height(i,j)>500000 && height(i,j)<600000){
                    pixmap.setColor(Color.GREEN);
                    pixmap.drawPixel(i, j);
                }
                else if (height(i,j)>600000&& height(i,j)<700000){
                    pixmap.setColor(Color.YELLOW);
                    pixmap.drawPixel(i, j);
                }
                else if (height(i,j)>700000 && height(i,j)<800000){
                    pixmap.setColor(Color.BROWN);
                    pixmap.drawPixel(i, j);
                }
                else {
                    pixmap.setColor(Color.BLUE);
                    pixmap.drawPixel(i, j);
                    System.out.println(height(i,j));
                }

            }
        }
        texture.draw(pixmap, 0, 0);
    }
    public void drawHeightMap(){

        pixmap = new Pixmap(700,700, Pixmap.Format.RGB888);
        texture = new Texture(pixmap);
        int maxValue = (int)(height(pixmap.getWidth(),pixmap.getHeight()));
        int nrOfIntervals = 10;
        int interval = maxValue/nrOfIntervals;
        int[] intervals = new int[nrOfIntervals+1];

        for(int x=0;x<intervals.length;x++){
            intervals[x] = interval*x;
            System.out.println("Interval "+x+" : "+intervals[x]);
        }

        for(int i = 0; i<pixmap.getWidth();i++){
            for(int j = 0; j<pixmap.getHeight();j++){
                for(int x=0;x<intervals.length;x++){
                    if(height(i,j)<0){
                        pixmap.setColor(new Color(Color.BLUE));
                        pixmap.drawPixel(i, j);
                        break;
                    }
                    else if(height(i,j)>intervals[x] && height(i,j)<intervals[x+1]){
//                        System.out.println("Bang");
                        pixmap.setColor(new Color((float)(200/255.0*(1/(double)(x+1))),(float)((250-x*20)/255.0),(float)(200/255.0*(1/(double)(x+1))),1));
                        pixmap.drawPixel(i, j);
                     //   System.out.println(height(i,j)+" interval "+(x+1)+"r: "+200*(1/(double)(x+1))+"g: "+(250-x*20)+" b: "+200*(1/(double)(x+1)));
                        break;
                    }
                }
            }
        }
        texture.draw(pixmap, 0, 0);
    }
}
