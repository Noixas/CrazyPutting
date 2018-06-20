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

        float deltaVelocity = realSeparatingVelocity - separatingVelocity;

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
        Vector3 secondVelocity = new Vector3(contact.object2.getVelocity().Vx,contact.object2.getVelocity().Vy,0);

        Vector3 intermediateResult = relativeVelocity.cpy().sub(secondVelocity);
        return intermediateResult.cpy().dot(contact.contactNormal);


    }

    private static void resolvePenetration(Contact contact){
        float totalInverseMass = 1/contact.object1.getInverseMass() + 1/contact.object2.getInverseMass();

        Vector3 changeInPosition1 = contact.contactNormal.cpy().scl(((1/contact.object1.getInverseMass())/totalInverseMass)*contact.penetration);
        Vector3 changeInPosition2 = contact.contactNormal.cpy().scl(-1*((1/contact.object2.getInverseMass())/totalInverseMass)*contact.penetration);

        contact.object1.setPosition(contact.object1.getPosition().cpy().add(changeInPosition1));
        contact.object2.setPosition(contact.object2.getPosition().cpy().add(changeInPosition2));

    }
}
