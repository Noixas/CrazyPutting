package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;
import javafx.geometry.Point2D;

public class Plane {

    protected Vector3 normal;

    protected Point2D firstPoint;
    protected Point2D secondPoint;

    public Plane(ColliderComponent component){
        if(component instanceof SphereCollider){
            SphereCollider sphere = (SphereCollider) component;
            firstPoint = new Point2D(sphere.position.x - sphere.getRadius(),sphere.position.y);
            secondPoint = new Point2D(sphere.position.x + sphere.getRadius(),sphere.position.y);
        }

    }


}
