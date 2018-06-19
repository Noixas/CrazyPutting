package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;


public class CollisionDetector {



    public static Contact detectCollision(ColliderComponent comp1, ColliderComponent comp2){
        if(comp1 instanceof SphereCollider && comp2 instanceof BoxCollider){
            SphereCollider sphere = (SphereCollider) comp1;
            BoxCollider box = (BoxCollider) comp2;
            return SphereWithAABB(sphere,box);
        }
        else if(comp1 instanceof BoxCollider && comp2 instanceof BoxCollider){
            BoxCollider box1 = (BoxCollider) comp1;
            BoxCollider box2 = (BoxCollider) comp2;
            return AABBwithAABB(box1,box2);
        }

        else if(comp1 instanceof BoxCollider && comp2 instanceof SphereCollider){
            BoxCollider box = (BoxCollider) comp1;
            SphereCollider sphere = (SphereCollider) comp2;
            return SphereWithAABB(sphere,box);
        }

        else{
            SphereCollider sphere1 = (SphereCollider) comp1;
            SphereCollider sphere2 = (SphereCollider) comp2;
            return SphereWithSphere(sphere1,sphere2);
        }
    }


    //returns null for now
    public static Contact AABBwithAABB(BoxCollider box1, BoxCollider box2){
        if(Math.abs(box1.getPosition().x - box2.getPosition().x) > (box1.getHalfSizes().x + box2.getHalfSizes().x)) {
            return null;
        }
        if(Math.abs(box1.getPosition().y - box2.getPosition().y) > (box1.getHalfSizes().y + box2.getHalfSizes().y)) {
            return null;
        }
        if(Math.abs(box1.getPosition().z - box2.getPosition().z) > (box1.getHalfSizes().z + box2.getHalfSizes().z)) {
            return null;
        }
        return null;
    }

    public static Contact SphereWithSphere(SphereCollider sphereCollider1, SphereCollider sphereCollider2){
        //store the centres of 2 spheres
        Vector3 centre1 = sphereCollider1.getPosition();
        Vector3 centre2 = sphereCollider2.getPosition();

        //Vector between the objects
        Vector3 midline = centre1.cpy().sub(centre2);

        float distance = midline.len();

        if(distance <=0.0f || distance >= sphereCollider1.getRadius() + sphereCollider2.getRadius()){
            return null;
        }

        // if we are here,then 2 spheres collides

        Vector3 normal = midline.cpy().scl(1.0f/distance);

        Contact contact = new Contact();
        contact.contactNormal = normal;
        contact.contactPoint = centre1.cpy().add(midline.cpy().scl(0.5f));
        contact.penetration = sphereCollider1.getRadius() + sphereCollider2.getRadius() - distance;

        contact.object1 = sphereCollider1;
        contact.object2 = sphereCollider2;

        return contact;
    }




    public static Contact SphereWithAABB(SphereCollider sphereCollider, BoxCollider bBox){
        
        Vector3 pPosition = sphereCollider.getPosition();
        //System.out.println("pPosition: " + pPosition);

        Vector3 max = bBox.getPosition().cpy().add(bBox.getHalfSizes());
        Vector3 min = bBox.getPosition().cpy().sub(bBox.getHalfSizes());


        Vector3 closestPoint = new Vector3(0,0,0);


        //test the bounds against the points on X axis
        float distance = pPosition.x;

        if(distance < min.x){
            distance = min.x;
        }
        if(distance > max.x){
            distance = max.x;
        }
        closestPoint.x = distance;

        //test for Y axes
        distance = pPosition.y;
        if(distance < min.y){
            distance = min.y;
        }
        if(distance > max.y){
            distance = max.y;
        }
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

        //Check we're in contact
        distance = closestPoint.cpy().sub(pPosition).len2();
        System.out.println("Distance: " + distance);

        if(distance > sphereCollider.getRadius() * sphereCollider.getRadius()) {
            return null;
        }
        //we are in contact
        else {
            Contact contact = new Contact();

            System.out.println("WE ARE IN CONTACT MAFAKA");


            contact.contactNormal = (pPosition.cpy().sub(closestPoint)).nor();
            contact.contactPoint = closestPoint;
            contact.penetration = (float) (sphereCollider.getRadius() - Math.sqrt(distance));
            contact.object1 = sphereCollider;
            contact.object2 = bBox;

            System.out.println(contact==null);
            return contact;
        }
    }


}

