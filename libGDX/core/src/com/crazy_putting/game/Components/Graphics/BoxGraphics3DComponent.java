package com.crazy_putting.game.Components.Graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.GraphicsManager;

public class BoxGraphics3DComponent extends Graphics3DComponent {
    private Vector3 _dimensions;
    public BoxGraphics3DComponent(Vector3 pDimensions){
        _dimensions = pDimensions;
        initBox();
    }
    public BoxGraphics3DComponent(Vector3 pDimensions, Color pColor){
        _color = pColor;
        _dimensions = pDimensions;
        initBox();
    }
    public BoxGraphics3DComponent(float pWidth, float pDepth, float pHeight){
        _dimensions = new Vector3(pWidth, pDepth, pHeight);
        initBox();
    }
    public BoxGraphics3DComponent(float pWidth, float pDepth, float pHeight, Color pColor){
        _color = pColor;
        _dimensions = new Vector3(pWidth, pDepth, pHeight);
        initBox();
    }
    private void initBox(){
        GraphicsManager.addGraphics3DComponent(this);
        ModelBuilder modelBuilder = new ModelBuilder();
        _model = modelBuilder.createBox(_dimensions.x, _dimensions.z, _dimensions.y, new Material(ColorAttribute.createDiffuse(_color)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        _instance = new ModelInstance(_model);
    }
}
