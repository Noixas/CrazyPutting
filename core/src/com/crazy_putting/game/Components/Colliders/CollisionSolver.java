package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.Others.MultiplayerSettings;
import com.crazy_putting.game.Physics.Physics;

public final class CollisionSolver {
    private static final float RESTITUTION = 0.9f;


    public static void dealCollision(Contact contact) {
        if (contact != null) {
            resolveVelocity(contact);
            resolvePenetration(contact);
            if (contact.object1.get_owner() instanceof Ball) {
                Ball ball = (Ball) contact.object1.get_owner();
                if (MultiplayerSettings.PlayerAmount > 1) {
                    Physics.physics.addMovableObject(ball);
                    ball.fix(false);
                }
            }
            if (contact.object2.get_owner() instanceof Ball) {
                Ball ball = (Ball) contact.object1.get_owner();
                if (MultiplayerSettings.PlayerAmount > 1) {
                    Physics.physics.addMovableObject(ball);
                    ball.fix(false);
                }
            }
        }
    }



    private static void resolveVelocity(Contact contact){
        float separatingVelocity = calculateSeparatingVelocity(contact);

        //objects are moving away from each other
        if(separatingVelocity > 0){
            return;
        }

        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println("Object 1 velocity before: " + contact.object1.getVelocity());
       // System.out.println("Object 2 velocity before: " + contact.object2.getVelocity());

        float realSeparatingVelocity = -RESTITUTION * separatingVelocity;
        //System.out.println("real separating vel : " + realSeparatingVelocity);

        float deltaVelocity = realSeparatingVelocity - separatingVelocity;
       // System.out.println("delta vel: " + deltaVelocity);

        float totalInverseMass = contact.object1.getInverseMass() +contact.object2.getInverseMass();
        //System.out.println("Total inverse mass: " + totalInverseMass);

        float impulse = deltaVelocity / totalInverseMass;

        Vector3 impulsePerMass = contact.contactNormal.cpy().scl(impulse);
        //System.out.println("Impulse per mass : " + impulsePerMass);

        contact.object1.getVelocity().Vx += impulsePerMass.x * contact.object1.getInverseMass();
        contact.object1.getVelocity().Vy += impulsePerMass.y * contact.object1.getInverseMass();

        contact.object2.getVelocity().Vx +=impulsePerMass.x * (-1*contact.object2.getInverseMass());
        contact.object2.getVelocity().Vy +=impulsePerMass.y * (-1*contact.object2.getInverseMass());
        //System.out.println("Object 1 velocity after: " + contact.object1.getVelocity());
        //System.out.println("Object 2 velocity after: " + contact.object2.getVelocity());
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println();

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

        //System.out.println("initial distance penetration: " + contact.object1.get_owner().getPosition().dst(contact.object2.get_owner().getPosition()));

        //System.out.println("collider1 position before: " + contact.object1.getPosition());
        //System.out.println("collider2 position before: " + contact.object2.getPosition());


        //System.out.println(contact.contactNormal);
        Vector3 movePerInverseMass = contact.contactNormal.cpy().scl(contact.penetration/totalInverseMass);

        Vector3 changeInPosition1 = movePerInverseMass.cpy().scl(contact.object1.getInverseMass());

        //System.out.println("change in position 1: " + changeInPosition1);
        Vector3 changeInPosition2 = movePerInverseMass.cpy().scl(contact.object2.getInverseMass());
        //System.out.println("Change in position 2: " + changeInPosition2);


        if(contact.object1 instanceof SphereCollider) {
            contact.object1.get_owner().setPosition(contact.object1.getPosition().cpy().add(changeInPosition1));
        }
        if(contact.object2 instanceof SphereCollider) {
            contact.object2.get_owner().setPosition(contact.object2.getPosition().cpy().sub(changeInPosition2));
        }
       // System.out.println("resolved distance penetration: " + contact.object1.get_owner().getPosition().dst(contact.object2.get_owner().getPosition()));



    }
}
