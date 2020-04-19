package com.crazy_putting.game.Components.Graphics;

        import com.badlogic.gdx.graphics.Color;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.GraphicsManager;

public class ArrowGraphics3DComponent extends Graphics3DComponent {
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
        //System.out.println("From "+_from+" to  "+ _to);
        Vector3 v = new Vector3();
        _to.sub(_from);
        _to.nor().scl(200);
        _model = modelBuilder.createArrow(v.x,v.y,v.z,_to.x,_to.y,_to.z,0.1f,0.3f,10, GL20.GL_TRIANGLES,new Material(ColorAttribute.createDiffuse(_color)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        _instance = new ModelInstance(_model);
    }
    private void swapYZ(Vector3 v){
        Vector3 cache = new Vector3(v);
        v.y=cache.z;
        v.z=cache.y;
    }

}
