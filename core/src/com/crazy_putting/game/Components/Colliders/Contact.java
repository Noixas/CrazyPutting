package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class Contact {

    //Holds the position of the contact
    public Vector3 contactPoint;

    //Holds the direction of the contact
    public Vector3 contactNormal;

    //Holds the depth of penetration at the contact point
    public float penetration;

    public ColliderComponent object1;
    public ColliderComponent object2;


    public Contact(Vector3 point, Vector3 normal, float penetration, ColliderComponent obj1,ColliderComponent obj2){
        this.contactNormal=normal;
        this.contactPoint=point;
        this.penetration=penetration;
        this.object1=obj1;
        this.object2=obj2;
    }

    public Contact() {

    }

    public String toString(){
        String result = "ContactPoint: " + contactPoint +"\nContactNormal: " + contactNormal + "\nPenetration: " + penetration;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Contact) {
            Contact cont = (Contact) o;

            if (this.object1.equals(cont.object1) && this.object2.equals(cont.object2)) {
                return true;
            }
            if (this.object1.equals(cont.object2) && this.object2.equals(cont.object1)) {
                return true;
            }
        }
        return false;
    }

}
