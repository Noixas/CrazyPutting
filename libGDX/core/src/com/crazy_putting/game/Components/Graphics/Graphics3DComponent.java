package com.crazy_putting.game.Components.Graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.GraphicsManager;

public abstract class Graphics3DComponent extends GraphicsComponent {
    protected Model _model;
    protected ModelInstance _instance;
    protected Color _color;
    private static Quaternion _emptyQuaternion = new Quaternion();

    public Graphics3DComponent() {
        GraphicsManager.addGraphics3DComponent(this);
    }
    public void setColor(Color pCustomColor){
       _color =  pCustomColor;
    }
    public ModelInstance getInstance() {
        return _instance;
    }
    public void render(ModelBatch pModelBatch, Environment pEnvironment ) {
        if(_owner.enabled == false )return;
        Vector3 pos2d = _owner.getPosition();
        Vector3 pos = new Vector3(pos2d.x, pos2d.z,pos2d.y);
        //TODO: pos2D will allow the balls that spawn when clicking to appear at right spot, pos will show hole and ball in right spot
         if(_color == Color.WHITE)
               pos.y+=20f;
        _instance.transform.set(pos,_emptyQuaternion);
        pModelBatch.render(_instance,pEnvironment);
    }
}
