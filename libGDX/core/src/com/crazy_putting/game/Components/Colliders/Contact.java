package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class Contact {
    /*
    Holds the position of the contact
     */

    public Vector3 contactPoint;

    /*
    Holds the direction of the contact
     */

    public Vector3 contactNormal;

    /*
    Holds the depth of penetration at the contact point
     */
    public float penetration;


    public Collidable object1;
    public Collidable object2;


    public Contact(Vector3 point, Vector3 normal, float penetration, Collidable obj1,Collidable obj2){
        this.contactNormal=normal;
        this.contactPoint=point;
        this.penetration=penetration;
        this.object1=obj1;
        this.object2=obj2;
    }
    public Contact(){

    }


}
