package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Others.Velocity;

public class CollisionManager {
    private final float RESTITUTION = 0.9f;


    public void dealCollision(Contact contact){

        resolveVelocity(contact);
        resolvePenetration(contact);

    }


    private void resolveVelocity(Contact contact){
        float separatingVelocity = calculateSeparatingVelocity(contact);

        if(separatingVelocity > 0){
            return;
        }

        float realSeparatingVelocity = -RESTITUTION * separatingVelocity;

        float deltavelocity = realSeparatingVelocity - separatingVelocity;

        float totalInverseMass = contact.object1.getInverseMass() +contact.object2.getInverseMass();


        float impulse = deltavelocity / totalInverseMass;

        Vector3 impulsePerMass = contact.contactNormal.cpy().scl(impulse);

        contact.object1.getVelocity().Vx +=impulsePerMass.x * contact.object1.getInverseMass();
        contact.object1.getVelocity().Vy +=impulsePerMass.y * contact.object1.getInverseMass();

        contact.object2.getVelocity().Vx +=impulsePerMass.x * contact.object2.getInverseMass();
        contact.object2.getVelocity().Vy +=impulsePerMass.y * contact.object2.getInverseMass();




    }

    private float calculateSeparatingVelocity(Contact contact) {
        Velocity relativeVelocity;
        //if (!contact.object1.isStatic()) {
        relativeVelocity = contact.object1.getVelocity();
        //}
        //if(!contact.object2.isStatic()){
        relativeVelocity.sub(contact.object2.getVelocity());
        //}

        float result = relativeVelocity.multiply(contact.contactNormal);

        return result;
    }

    private void resolvePenetration(Contact contact){
        float totalMass = contact.object1.getMass() + contact.object2.getMass();

        Vector3 changeInPosition1 = contact.contactNormal.scl((contact.object1.getMass()/totalMass)*contact.penetration);
        Vector3 changeInPosition2 = contact.contactNormal.scl((-contact.object2.getMass()/totalMass)*contact.penetration);

        contact.object1.setPosition(contact.object1.getPosition().cpy().add(changeInPosition1));
        contact.object2.setPosition(contact.object2.getPosition().cpy().add(changeInPosition2));
    }


}
