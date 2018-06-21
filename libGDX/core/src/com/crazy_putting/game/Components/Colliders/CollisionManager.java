package com.crazy_putting.game.Components.Colliders;

import java.util.ArrayList;

public final class CollisionManager {

    private static ArrayList<ColliderComponent> colliders = new ArrayList<ColliderComponent>();
    private static ArrayList<Contact> contacts = new ArrayList<Contact>();


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
        fillContactList();
        dealContacts();
        synchronizeColliders();
        contacts.clear();

    }


    private static void synchronizeColliders(){
        if(!colliders.isEmpty()){
            for(ColliderComponent collider : colliders){
                collider.synchronize();
            }
        }
    }

    private static void dealContacts(){
        for(Contact contact : contacts){

            CollisionSolver.dealCollision(contact);

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
                        }
                    }
                }
            }
        }
    }

}
