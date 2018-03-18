package com.crazy_putting.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crazy_putting.game.Others.InputData;
import com.crazy_putting.game.MyCrazyPutting;

public class MenuState extends State{
    private Texture background;
    private Texture playButton;


    public MenuState(GameStateManager gsm){
        super(gsm);
        background = new Texture("golfField.jpg");
        playButton = new Texture("startButton.png");
        input = new InputData();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            gsm.set(new PlayState(gsm));
            dispose();
        }


    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background,0,0, MyCrazyPutting.WIDTH,MyCrazyPutting.HEIGHT);
        sb.draw(playButton, (MyCrazyPutting.WIDTH/2)-(playButton.getWidth()/2), (MyCrazyPutting.HEIGHT/2)-(playButton.getHeight()/2));
        sb.end();
    }

    @Override
    public void dispose(){
        background.dispose();
        playButton.dispose();
    }

    @Override
    public void create(){

    }
}
