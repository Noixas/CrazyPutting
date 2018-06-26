package com.crazy_putting.game.GameObjects;


import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Components.Colliders.ColliderComponent;
import com.crazy_putting.game.Components.Colliders.CollisionManager;
import com.crazy_putting.game.Components.Graphics.GraphicsComponent;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;

public class GameObject {
    private GraphicsComponent _graphicComponent;
    private ColliderComponent _colliderComponent;



    protected Vector3 _position = new Vector3();//= Vector3.Zero;
    public boolean enabled = true;
    public GameObject(Vector3 pPosition)
    {
        _position = pPosition;
    }
    public GameObject(){}
    public void addGraphicComponent(GraphicsComponent pGC){
        _graphicComponent = pGC;
        _graphicComponent.setOwner(this);
    }
    public GraphicsComponent getGraphicComponent() {
        return _graphicComponent;
    }
    public Vector3 getPosition(){
        return _position;
    }
    public void updateHeight()
    {
        _position.z = CourseManager.calculateHeight(_position.x,_position.y);
    }
    @Override
    public String toString() {
        return _position.toString();
    }
    public void addColliderComponent(ColliderComponent pCollider){

        _colliderComponent = pCollider;
        _colliderComponent.setOwner(this);
    }
    public void deleteColliderComponent(){
        if(_colliderComponent != null){
            _colliderComponent = null;
        }
    }

    public ColliderComponent getColliderComponent() {
        return _colliderComponent;
    }
    public void setPosition(Vector3 position){
        this._position = new Vector3(position);
        if(_colliderComponent!=null){
            _colliderComponent.setPosition(new Vector3(position));
        }
    }
    public void destroy(){
        enabled = false;
        if(_colliderComponent!= null)
            CollisionManager.deleteCollider(_colliderComponent);
        if(_graphicComponent != null)
            GraphicsManager.deleteGraphicsComponent(_graphicComponent);
    }





}
