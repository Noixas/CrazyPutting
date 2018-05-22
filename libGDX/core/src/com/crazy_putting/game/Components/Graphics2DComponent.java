package com.crazy_putting.game.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crazy_putting.game.GameLogic.GraphicsManager;

import static com.crazy_putting.game.GameLogic.GraphicsManager.WORLD_HEIGHT;
import static com.crazy_putting.game.GameLogic.GraphicsManager.WORLD_WIDTH;

public class Graphics2DComponent extends GraphicsComponent {
    protected Texture _texture;
    protected int _width;
    protected int _height;

    public Graphics2DComponent( Texture pTexture) {
        GraphicsManager.addGraphics2DComponent(this);
        _texture = pTexture;
        _width = 15;
        _height = 15;
    }

    public Graphics2DComponent( Texture pTexture, int pWidth, int pHeight) {
        GraphicsManager.addGraphics2DComponent(this);
        _texture = pTexture;

        _width = pWidth;
        _height = pHeight;
    }
    public void render(SpriteBatch pBach)
    {
        if(_texture == null) return;


        pBach.draw(_texture, _owner.getPosition().x+WORLD_WIDTH/2-_width/2, _owner.getPosition().y+WORLD_HEIGHT/2-_height/2,_width, _height);

    }

}
