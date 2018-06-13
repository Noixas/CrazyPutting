package com.crazy_putting.game.Components.Graphics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class CustomGraphics3DComponent extends Graphics3DComponent {
    public CustomGraphics3DComponent(Model pModel) {
        _model = pModel;
        _instance = new ModelInstance(_model);
    }
}
