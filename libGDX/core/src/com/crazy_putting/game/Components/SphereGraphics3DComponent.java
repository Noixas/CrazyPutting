package com.crazy_putting.game.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.crazy_putting.game.GameLogic.GraphicsManager;

public class SphereGraphics3DComponent extends Graphics3DComponent {
    private float _radius;
    public SphereGraphics3DComponent(float pRadius){
        _radius = pRadius;
        initSphere();
    }
    public SphereGraphics3DComponent(float pRadius, Color pColor){
        _color = pColor;
        _radius = pRadius;
        initSphere();
    }
    private void initSphere(){
        GraphicsManager.addGraphics3DComponent(this);
        ModelBuilder modelBuilder = new ModelBuilder();

        _radius = 40f;//hardcoded for testing, erase asap
        _model = modelBuilder.createSphere(_radius, _radius, _radius, 24, 24,new Material(ColorAttribute.createDiffuse(_color)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        _instance = new ModelInstance(_model);
    }

}
