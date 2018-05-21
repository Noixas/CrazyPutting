package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crazy_putting.game.GameLogic.GameManager;
import com.crazy_putting.game.Screens.GameScreen3D;
import com.crazy_putting.game.Screens.GolfGame;

public class GUI extends GameObject {
/*
TODO: Use stage and the view part created in gamescreen3D to create an input listener so the buttons work
 */

    private GolfGame _game;
    private GameManager _gameManager;
    private Camera _cam2D;
    private Skin _skin;
    private TextButton soloButton;
    private Table table;
    private Stage UIStage;
    private Label speedText;
    Viewport view;

    public GUI(GolfGame pGame, GameManager pGameManager, Camera pCam2D, FitViewport viewPort)
    {
        _game = pGame;
        if(viewPort ==null)
            view = new FitViewport(10,10);
       else
           view = viewPort;
        UIStage = new Stage(viewPort,_game.batch);
        UIStage.getViewport().setScreenBounds(GameScreen3D.Width3DScreen,0 ,GameScreen3D.Width2DScreen-1,GameScreen3D.Height2DScreen-1);
        UIStage.getViewport().apply();
        UIStage.setDebugAll(true);
        _gameManager = pGameManager;
        _cam2D = pCam2D;
        _skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        soloButton = new TextButton("Solo play", _skin);
        soloButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button55");
            }
        });


        initUI();
        System.out.println("VIEWPORT POS "+ view.getScreenY()   );

    }
    private void initUI()
    {
        Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        table = new Table();
       // table.align(Align.bottom);
        table.setFillParent(true);
        table.center();
      //  table.setWidth(GameScreen3D.Width2DScreen );
      //  table.setHeight(GameScreen3D.Width2DScreen);
       // table.align(Align.center);

      //  Vector3 tablePos = _cam2D.unproject(new Vector3(, 0,0),view.getScreenX(),view.getScreenY(),view.getScreenWidth(),view.getScreenHeight());

        Vector2 pos = UIStage.screenToStageCoordinates(new Vector2(GameScreen3D.Width3DScreen, GameScreen3D.Height2DScreen));
        System.out.println(pos+"TESSSS");
      //  table.setOrigin(0,0);
       // table.setPosition(pos.x,pos.y);
        speedText = new Label("TEST TABLE",skin);
        table.setDebug(true);
       // table.setSize(GameScreen3D.Width2DScreen,);
        Label ne = new Label("TEST TABLE this is longer",skin);
        Label ne2 = new Label("TEST TA2222222222222E this is longer",skin);
        System.out.println(table.getY()+"X OF TABLE");

        table.add(speedText);
        table.row();
        table.add(ne2);
        table.row();
        table.add(ne);
        table.row();
        table.add(soloButton);


        Vector3 unprojectSpeed = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 10,0));
        Vector3 unprojectHeight =_cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 40,0));
        Vector3 unprojectBallX = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 70,0));
        Vector3 unprojectBallY = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 100,0));
        Vector3 unprojectTurn = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 130,0));
        UIStage.addActor(table);
      //  UIStage.addActor(soloButton);


    }
    public InputProcessor getUIInputProcessor()
    {
        return UIStage;
    }
    public void render()
    {
        Ball ball =_gameManager.getBall();
        Vector3 unprojectSpeed = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 10,0));
        Vector3 unprojectHeight =_cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 40,0));
        Vector3 unprojectBallX = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 70,0));
        Vector3 unprojectBallY = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 100,0));
        Vector3 unprojectTurn = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 130,0));
     //   System.out.println(GameScreen3D.Width3DScreen);
        /*
            Setup of the HUD in top left corner.
         */
       // table.draw(_game.batch,100);
       /* _game.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        _game.font.getData().setScale(1.2f);
        _game.font.setColor(Color.BLACK);
        speedText.setText("Speed: "+Math.round(Math.sqrt(Math.pow(ball.getVelocity().Vx,2)+Math.pow(ball.getVelocity().Vy,2))));
        _game.font.draw(_game.batch, "Speed: "+Math.round(Math.sqrt(Math.pow(ball.getVelocity().Vx,2)+Math.pow(ball.getVelocity().Vy,2))), unprojectSpeed.x, unprojectSpeed.y);
        _game.font.draw(_game.batch, "Height: "+Math.round(CourseManager.calculateHeight(ball.getPosition().x,ball.getPosition().y)), unprojectHeight.x, unprojectHeight.y);
        _game.font.draw(_game.batch, "Ball x: "+Math.round(ball.getPosition().x), unprojectBallX.x, unprojectBallX.y);
        _game.font.draw(_game.batch, "Ball y: "+Math.round(ball.getPosition().y), unprojectBallY.x, unprojectBallY.y);
        _game.font.draw(_game.batch, "Turn: "+ _gameManager.getTurns(), unprojectTurn.x, unprojectTurn.y);
*/
        Vector3 pos = _cam2D.unproject(new Vector3(GameScreen3D.Width3DScreen, 170,0));
      //  soloButton.setPosition(pos.x,pos.y);
        //soloButton.draw(_game.batch,100);
        UIStage.draw();

    }
    private void renderGUI()
    {
 
    }

}
