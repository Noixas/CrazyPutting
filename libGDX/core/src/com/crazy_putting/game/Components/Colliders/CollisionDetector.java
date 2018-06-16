package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class CollisionDetector {
    Contact contact ;


    public float AABBwithAABB(AABB box1, AABB box2){
        if(Math.abs(box1.getPosition().x - box2.getPosition().x) > (box1.getHalfSizes().x + box2.getHalfSizes().x)) {
            return 0;
        }
        if(Math.abs(box1.getPosition().y - box2.getPosition().y) > (box1.getHalfSizes().y + box2.getHalfSizes().y)) {
            return 0;
        }
        if(Math.abs(box1.getPosition().z - box2.getPosition().z) > (box1.getHalfSizes().z + box2.getHalfSizes().z)) {
            return 0;
        }
        return 1;
    }

    public float SphereWithSphere(Sphere sphere1, Sphere sphere2){
        //store the centres of 2 spheres
        Vector3 centre1 = sphere1.getPosition();
        Vector3 centre2 = sphere2.getPosition();

        //Vector between the objects
        Vector3 midline = centre1.cpy().sub(centre2);

        float distance = midline.len();

        if(distance <=0.0f || distance >= sphere1.getRadius() + sphere2.getRadius()){
            return 0;
        }

        // if we are here,then 2 spheres collides

        Vector3 normal = midline.cpy().scl(1.0f/distance);

        contact = new Contact();
        contact.contactNormal = normal;
        contact.contactPoint = centre1.cpy().add(midline.cpy().scl(0.5f));
        contact.penetration = sphere1.getRadius() + sphere2.getRadius() - distance;


        return 1;
    }




    public float SphereWithAABB(Sphere sphere, AABB bBox){
        
        Vector3 pPosition = sphere.getPosition();
        Vector3 max = bBox.getPosition().cpy().add(bBox.getHalfSizes());
        //System.out.println("Max" + max);
        Vector3 min = bBox.getPosition().cpy().sub(bBox.getHalfSizes());
        //System.out.println("Min " + min);

       // System.out.println("Sphere position: " + pPosition);
        //System.out.println("Box pos: " + bBox.getCenter());

        Vector3 closestPoint = new Vector3(0,0,0);


        //test the bounds against the points on X axis
        float distance = pPosition.x;


        if(distance < min.x){
            distance = min.x;
        }
        if(distance > max.x){
            distance = max.x;
        }
        //System.out.println("Distance x: " + distance);
        closestPoint.x = distance;

        //test for Y axes

        distance = pPosition.y;
        if(distance < min.y){
            distance = min.y;
        }
        if(distance > max.y){
            distance = max.y;
        }
       // System.out.println("Distance y: " + distance);
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
        //System.out.println("Distance z: " + distance);

        //Check we're in contact
        //System.out.println("Closest point: " + closestPoint);
        //System.out.println("Shpere position: " + pPosition);
        distance = closestPoint.cpy().sub(pPosition).len2();
       // System.out.println("Distance: " + distance);
        //System.out.println("Distance: " + distance);




        if(distance > sphere.getRadius() * sphere.getRadius()) {
            //System.out.println("nothing");
            return 0;
        }

            //we are in contact
            contact = new Contact();

            System.out.println("WE ARE IN CONTACT MAFAKA");

            contact.contactNormal = (pPosition.cpy().sub(closestPoint)).nor();

            contact.contactPoint = closestPoint;

            contact.penetration = (float) (sphere.getRadius()-Math.sqrt(distance));
            System.out.println("Penetration: " + contact.penetration);

            contact.object1 = sphere;

            contact.object2 = bBox;



            return 1;

    }

    public Contact getContact(){
        return this.contact;
    }


}

