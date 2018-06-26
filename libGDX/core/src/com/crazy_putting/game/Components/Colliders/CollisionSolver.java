package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public final class CollisionSolver {
    private static final float RESTITUTION = 0.9f;


    public static void dealCollision(Contact contact){
        if(contact!=null) {
            resolveVelocity(contact);
            resolvePenetration(contact);
        }
    }


    private static void resolveVelocity(Contact contact){
        float separatingVelocity = calculateSeparatingVelocity(contact);

        //objects are moving away from each other
        if(separatingVelocity > 0){
            return;
        }

        float realSeparatingVelocity = -RESTITUTION * separatingVelocity;
        //System.out.println("real separating vel : " + realSeparatingVelocity);

        float deltaVelocity = realSeparatingVelocity - separatingVelocity;
        //System.out.println("delta vel: " + deltaVelocity);

        float totalInverseMass = contact.object1.getInverseMass() +contact.object2.getInverseMass();

        float impulse = deltaVelocity / totalInverseMass;

        Vector3 impulsePerMass = contact.contactNormal.cpy().scl(impulse);

        contact.object1.getVelocity().Vx += impulsePerMass.x * contact.object1.getInverseMass();
        contact.object1.getVelocity().Vy += impulsePerMass.y * contact.object1.getInverseMass();

        contact.object2.getVelocity().Vx +=impulsePerMass.x * (-1*contact.object2.getInverseMass());
        contact.object2.getVelocity().Vy +=impulsePerMass.y * (-1*contact.object2.getInverseMass());
    }

    private static float calculateSeparatingVelocity(Contact contact) {
        Vector3 relativeVelocity = new Vector3(contact.object1.getVelocity().Vx,contact.object1.getVelocity().Vy,0);
        //System.out.println("first Velocity: " +relativeVelocity);
        Vector3 secondVelocity = new Vector3(contact.object2.getVelocity().Vx,contact.object2.getVelocity().Vy,0);
        //System.out.println("second velocity: " + secondVelocity);

        Vector3 intermediateResult = relativeVelocity.cpy().sub(secondVelocity);

        return intermediateResult.cpy().dot(contact.contactNormal);


    }

    private static void resolvePenetration(Contact contact){
        float totalInverseMass = contact.object1.getInverseMass() + contact.object2.getInverseMass();

        //System.out.println("collider1 position before: " + contact.object1.getPosition());
        //System.out.println("collider2 position before: " + contact.object2.getPosition());


        //System.out.println(contact.contactNormal);
        Vector3 movePerInverseMass = contact.contactNormal.cpy().scl(contact.penetration/totalInverseMass);

        Vector3 changeInPosition1 = movePerInverseMass.cpy().scl(contact.object1.getInverseMass());

        //System.out.println(changeInPosition1);
        Vector3 changeInPosition2 = movePerInverseMass.cpy().scl(contact.object2.getInverseMass());
        //System.out.println(changeInPosition2);


        if(contact.object1 instanceof SphereCollider) {
            contact.object1.get_owner().setPosition(contact.object1.getPosition().cpy().add(changeInPosition1));
        }
        if(contact.object2 instanceof SphereCollider) {
            contact.object2.get_owner().setPosition(contact.object2.getPosition().cpy().sub(changeInPosition2));
        }

        //System.out.println("_________________________________________");
       // System.out.println("collider1 position after: " + contact.object1.getPosition());
       // System.out.println("collider2 position after: " + contact.object2.getPosition());

    }
}
