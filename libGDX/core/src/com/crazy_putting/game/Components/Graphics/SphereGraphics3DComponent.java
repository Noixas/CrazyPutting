package com.crazy_putting.game.Components.Graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.crazy_putting.game.GameLogic.GraphicsManager;

public class SphereGraphics3DComponent extends Graphics3DComponent {

    private float _width;
    private float _deep;
    private float _height;
    public SphereGraphics3DComponent(float pRadius){
        _width = pRadius;
        _deep = pRadius;
        _height = pRadius;
        initSphere();
    }
    public SphereGraphics3DComponent(float pRadius, Color pColor){
        _color = pColor;
        _width = pRadius;
        _deep = pRadius;
        _height = pRadius;
        initSphere();
    }
    public SphereGraphics3DComponent(float pWidth,float pDeep,float pHeight, Color pColor){
        _color = pColor;
        _width = pWidth;
        _deep = pDeep;
        _height = pHeight;
        initSphere();
    }
    private void initSphere(){
        GraphicsManager.addGraphics3DComponent(this);
        ModelBuilder modelBuilder = new ModelBuilder();
        _model = modelBuilder.createSphere(_width, _height, _deep, 24, 24,new Material(ColorAttribute.createDiffuse(_color)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        _instance = new ModelInstance(_model);
    }

}
