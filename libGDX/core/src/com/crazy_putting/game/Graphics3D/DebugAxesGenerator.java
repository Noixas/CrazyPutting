package com.crazy_putting.game.Graphics3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class DebugAxesGenerator {
    public static final float GRID_MIN = -10000f;
    public static final float GRID_MAX = 10000f;
    public static final float GRID_STEP = 100f;
    public static Model generateAxes()
    {
        return createAxes();
    }
    private static Model createAxes () {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.LIGHT_GRAY);
        for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
            builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
            builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
        }
        builder = modelBuilder.part("axes", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.RED);
        builder.line(0, 0, 0, 1000, 0, 0);
        builder.setColor(Color.GREEN);
        builder.line(0, 0, 0, 0, 1000, 0);
        builder.setColor(Color.BLUE);
        builder.line(0, 0, 0, 0, 0, 1000);
        Model axesModel = modelBuilder.end();
        return  axesModel;
    }
}
