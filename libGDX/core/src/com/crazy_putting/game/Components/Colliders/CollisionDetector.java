package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class CollisionDetector {

    public boolean AABBwithAABB(AABB box1, AABB box2){
        if(Math.abs(box1.getCenter().x - box2.getCenter().x) > (box1.getHalfSizes().x + box2.getHalfSizes().x)) {
            return false;
        }
        if(Math.abs(box1.getCenter().y - box2.getCenter().y) > (box1.getHalfSizes().y + box2.getHalfSizes().y)) {
            return false;
        }
        if(Math.abs(box1.getCenter().z - box2.getCenter().z) > (box1.getHalfSizes().z + box2.getHalfSizes().z)) {
            return false;
        }
        return true;
    }

    public boolean SphereWithSphere(Sphere sphere1, Sphere sphere2){
        float distance =sphere1.getPosition().dst(sphere2.getPosition());
        float radiusSum = sphere1.getRadius() + sphere2.getRadius();

        return (distance <= radiusSum);
    }

    public boolean SphereWithAABB(Sphere sphere, AABB box){
        float distance = distanceToAABB(sphere.getPosition(),box);

        return distance<=sphere.getRadius();
    }


    private float distanceToAABB(Vector3 pPosition,AABB bBox){
        Vector3 mins = bBox.getCenter().cpy().sub(bBox.getHalfSizes());
        Vector3 maxs = bBox.getCenter().cpy().add(bBox.getHalfSizes());
        System.out.println("msx " + maxs);
        System.out.println("min " + mins);
        float distance = 0.0f;
        System.out.println(pPosition);

        //test the bounds against the points on X axis
        if(pPosition.x < mins.x){
            distance += Math.abs(mins.x-pPosition.x);
        }
        if(pPosition.x > maxs.x){
            distance += Math.abs(pPosition.x - maxs.x);
        }

        //test for Y axes
        if(pPosition.y < mins.y){
            distance += Math.abs(mins.y-pPosition.y);
        }
        if(pPosition.y>maxs.y){
            distance += Math.abs(pPosition.y - maxs.y);
        }

        //test for Z axes
        if(pPosition.z < mins.z){
            distance += Math.abs(mins.z-pPosition.z);
        }
        if(pPosition.z>maxs.z){
            distance += Math.abs(pPosition.z - maxs.z);
        }
        return distance;

    }


    public float calculateImpulse(Sphere sphere, AABB box){
        return 0.0f;
    }


}

