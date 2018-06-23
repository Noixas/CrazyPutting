package com.crazy_putting.game.Components.Graphics;

        import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.GraphicsManager;

public class ArrowGraphics3DComponent extends Graphics3DComponent {
    private float _radius;
    private Vector3 _from;
    private  Vector3 _to;
    public ArrowGraphics3DComponent(Vector3 from, Vector3 to){
       _from = from;
       _to = to;

        initArrow();
    }
    public ArrowGraphics3DComponent(Vector3 from, Vector3 to, Color pColor){
        _color = pColor;
        _from = from;
        _to = to;
        initArrow();
    }
    private void initArrow(){
        GraphicsManager.addGraphics3DComponent(this);
        ModelBuilder modelBuilder = new ModelBuilder();
        swapYZ(_from);
        //swapYZ(_to);
        System.out.println("From "+_from+" to  "+ _to);
        _model = modelBuilder.createArrow(_from,_to,new Material(ColorAttribute.createDiffuse(_color)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        _instance = new ModelInstance(_model);
    }
    private void swapYZ(Vector3 v){
        Vector3 cache = new Vector3(v);
        v.y=cache.z;
        v.z=cache.y;
    }

}
