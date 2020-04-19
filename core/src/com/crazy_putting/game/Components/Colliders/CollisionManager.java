package com.crazy_putting.game.Components.Colliders;

import java.util.ArrayList;

public final class CollisionManager {

    public static ArrayList<ColliderComponent> colliders = new ArrayList<ColliderComponent>();

    private static ArrayList<Contact> contacts = new ArrayList<Contact>();
    private static ArrayList<Contact> simulationsContact = new ArrayList<Contact>();
    private static int lastUpdateCollisions = 0;

    public static ArrayList<ColliderComponent> getColliders(){
        return colliders;
    }

    public static void addCollider(ColliderComponent component){
        if(!colliders.contains(component)){
            colliders.add(component);
        }
    }

    public static void deleteCollider(ColliderComponent component){
        if(colliders.contains(component)){
            colliders.remove(component);
        }
    }

    public static void update(){
        //System.out.println(colliders.size());
        lastUpdateCollisions = 0;
        fillContactList();
        dealContacts();
        synchronizeColliders();
        contacts.clear();

    }
    public static int getAmountCollisionsLastUpdate(){
        return lastUpdateCollisions;
    }
    public static void updateIgnoreSpheres(){
        //System.out.println(colliders.size());
        fillIgnoreContact();
        dealContacts();
        synchronizeColliders();
        simulationsContact.clear();

    }

    private static void fillIgnoreContact(){
        for(ColliderComponent component: colliders){
            if(!component.isStatic()){
                for(ColliderComponent anotherComponent : colliders){
                    if(!component.equals(anotherComponent) && anotherComponent instanceof SphereCollider){
                        Contact contact = CollisionDetector.detectCollision(component,anotherComponent);
                        if(contact!=null && !simulationsContact.contains(contact)){
                            simulationsContact.add(contact);

                        }
                    }
                }
            }
        }
    }
    private static void dealSimulatedContacts(){
        for(Contact contact : simulationsContact){
            CollisionSolver.dealCollision(contact);
        }
    }

    private static void synchronizeColliders(){
        if(!colliders.isEmpty()){
            for(ColliderComponent collider : colliders){
                if(collider.isEnabled())
                collider.synchronize();
            }
        }
    }

    private static void dealContacts(){
        for(Contact contact : contacts){
            CollisionSolver.dealCollision(contact);
            lastUpdateCollisions++;
        }
    }

    private static void fillContactList(){
        for(ColliderComponent component: colliders){
            if(!component.isStatic()){
                for(ColliderComponent anotherComponent : colliders){
                    if(!component.equals(anotherComponent)){
                        Contact contact = CollisionDetector.detectCollision(component,anotherComponent);
                        if(contact!=null && !contacts.contains(contact)){
                            contacts.add(contact);

                            //System.out.println("Plus one coll" + lastUpdateCollisions);
                        }
                    }
                }
            }
        }
    }
    public static void dispose(){
        contacts.clear();
        simulationsContact.clear();
    }
}
