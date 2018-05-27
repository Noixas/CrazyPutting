package com.crazy_putting.game.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;

public class Graphics3DComponent extends GraphicsComponent {
    private Model _model;
    private ModelInstance _instance;

    ////test delete asap
    public Color col;
    public Graphics3DComponent( Texture pTexture) {
        GraphicsManager.addGraphics3DComponent(this);
        ModelBuilder modelBuilder = new ModelBuilder();
        float radius = 30f;
        _model = modelBuilder.createSphere(radius, radius, radius, 24, 24, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        _instance = new ModelInstance(_model);
        //    isBall = true;

    }
    /*
    1 for ball, 2 for hole, TODO: improve implementation
     */
    public Graphics3DComponent( int pTypeElement) {

        Color endColor;
        if(pTypeElement == 1) {
            endColor = Color.WHITE;
            System.out.println("Whitw ball");
            col = Color.WHITE;
        }
        else
            endColor = Color.BLACK;
        GraphicsManager.addGraphics3DComponent(this);
        ModelBuilder modelBuilder = new ModelBuilder();
        float radius = 40f;
        _model = modelBuilder.createSphere(radius, radius, radius, 24, 24,new Material(ColorAttribute.createDiffuse(endColor)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        _instance = new ModelInstance(_model);
       //    isBall = true;

    }
    public Graphics3DComponent( Model pModel) {
        GraphicsManager.addGraphics3DComponent(this);
        _model = pModel;
        _instance = new ModelInstance(_model);
    }

    public ModelInstance getInstance() {
        return _instance;
    }

    public void render(ModelBatch pModelBatch, Environment pEnvironment ) {
        Vector3 pos2d = _owner.getPosition();
        Vector3 pos = new Vector3(pos2d.x, CourseManager.calculateHeight(pos2d.x,pos2d.y),pos2d.y);
        //TODO: pos2D will allow the balls that spawn when clicking to appear at right spot, pos will show hole and ball in right spot
    if(col == Color.WHITE)
        pos.y+=20f;
        _instance.transform.set(pos,new Quaternion());
        //System.out.println(_instance.transform);

        pModelBatch.render(_instance,pEnvironment);
    }
}
