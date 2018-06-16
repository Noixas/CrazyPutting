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




    public Contact SphereWithAABB(Sphere sphere, AABB bBox){
        
        Vector3 pPosition = sphere.getPosition();
        Vector3 max = bBox.getCenter().cpy().add(bBox.getHalfSizes());
        System.out.println("Max" + max);
        Vector3 min = bBox.getCenter().cpy().sub(bBox.getHalfSizes());
        System.out.println("Min " + min);

        System.out.println("Sphere position: " + pPosition);
        System.out.println("Box pos: " + bBox.getCenter());

        Vector3 closestPoint = new Vector3(0,0,0);


        //test the bounds against the points on X axis
        float distance = pPosition.x;


        if(distance < min.x){
            distance = min.x;
        }
        if(distance > max.x){
            distance = max.x;
        }
        System.out.println("Distance x: " + distance);
        closestPoint.x = distance;

        //test for Y axes

        distance = pPosition.y;
        if(distance < min.y){
            distance = min.y;
        }
        if(distance > max.y){
            distance = max.y;
        }
        System.out.println("Distance y: " + distance);
        closestPoint.y = distance;

        //test for Z axes
        distance = pPosition.z;
        if(distance < min.z){
            distance = min.z;
        }
        if(distance > max.z){
            distance = max.z;
        }
        closestPoint.z = distance;
        System.out.println("Distance z: " + distance);

        //Check we're in contact
        System.out.println("Closest point: " + closestPoint);
        System.out.println("Shpere position: " + pPosition);
        distance = closestPoint.cpy().sub(pPosition).len2();
        System.out.println("Distance: " + distance);
        //System.out.println("Distance: " + distance);



        Contact contact = new Contact();
        if(distance > sphere.getRadius() * sphere.getRadius()) {
            System.out.println("nothing");
        }
        else{
            //we are in contact
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("WE ARE IN CONTACT MAFAKA");
            System.out.println();
            System.out.println();
            System.out.println();
            contact.contactNormal = (pPosition.sub(closestPoint)).nor();

            contact.contactPoint = closestPoint;

            contact.penetration = (float) (sphere.getRadius()-Math.sqrt(distance));

            contact.object1 = sphere;
            contact.object2 = bBox;

            return contact;
        }

        return null;
    }


    




    public float calculateImpulse(Sphere sphere, AABB box){
        return 0.0f;
    }


}

