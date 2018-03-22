package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crazy_putting.game.Components.GraphicsComponent;

import java.util.ArrayList;
import java.util.List;

public class GraphicsManager {
    public final static int WORLD_WIDTH = 1500;
    public final static int WORLD_HEIGHT = 1500;
    public final static int WINDOW_WIDTH = 700;
    public final static int WINDOW_HEIGHT = 700;
    private static List<GraphicsComponent> _graphicsComponentList = new ArrayList<GraphicsComponent>();
public static void addGraphicsComponent(GraphicsComponent pGC)
{
    _graphicsComponentList.add(pGC);
}

    public static List<GraphicsComponent> getAllGraphicComponents()
    {
        return _graphicsComponentList;
    }

    /**
     * Render all the graphics components that exist
     * @param pBatch
     */
    public static void Render(SpriteBatch pBatch)
    {

        for(int i = 0; i < _graphicsComponentList.size(); i++)
        {
            _graphicsComponentList.get(i).Render(pBatch);
            //System.out.println(i);
        }
    }
}
