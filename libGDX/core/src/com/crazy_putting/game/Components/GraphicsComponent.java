package com.crazy_putting.game.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameObjects.GameObject;

import static com.crazy_putting.game.GameLogic.GraphicsManager.WORLD_HEIGHT;
import static com.crazy_putting.game.GameLogic.GraphicsManager.WORLD_WIDTH;

public class GraphicsComponent extends Component {

    private GameObject _owner;
    private Texture _texture;
    private int _width;
    private int _height;
    public GraphicsComponent( Texture pTexture)
    {
        GraphicsManager.addGraphicsComponent(this);
        _texture = pTexture;
        _width = 15;
        _height = 15;
    }
    public GraphicsComponent( Texture pTexture, int pWidth, int pHeight)
    {
        GraphicsManager.addGraphicsComponent(this);
        _texture = pTexture;

        _width = pWidth;
        _height = pHeight;
    }
    public void setOwner( GameObject pGameObj)
    {
        _owner = pGameObj;
    }
    public void Render(SpriteBatch pBach)
    {
        if(_texture == null) return;


       // pBach.draw(_texture, _parent.getPosition().x, _parent.getPosition().y,20* GameManager._viewportX/ MyCrazyPutting.WIDTH, 20*GameManager._viewportY/ MyCrazyPutting.HEIGHT);
        pBach.draw(_texture, _owner.getPosition().x+WORLD_WIDTH/2, _owner.getPosition().y+WORLD_HEIGHT/2,_width, _height);

    }

}
