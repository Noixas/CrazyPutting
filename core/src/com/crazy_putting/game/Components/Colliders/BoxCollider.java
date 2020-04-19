package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.GameManager;

public class BoxCollider extends ColliderComponent{
    private Vector3 halfSizes;

    public BoxCollider(Vector3 pos, Vector3 dim){
        this.position = pos;
        this.dimensions = new Vector3(dim);
        this.halfSizes = new Vector3(dim).scl(0.5f);
        isStatic = true;
    }

    public Vector3 getDimensions(){
        return this.dimensions;
    }
    public Vector3 getHalfSizes(){
        return this.halfSizes;
    }
    public boolean containsPoint(Vector3 pPoint){
        if(getPosition().x-halfSizes.x <= pPoint.x && getPosition().x + halfSizes.x >= pPoint.x &&
                getPosition().y-halfSizes.y <= pPoint.y && getPosition().y + halfSizes.y >= pPoint.y)return true;
        else return  false;
    }

    public boolean containsPointPath(Vector3 pPoint){
        return (getPosition().x - GameManager.allowedOffset - halfSizes.x <= pPoint.x && getPosition().x + GameManager.allowedOffset + halfSizes.x >= pPoint.x &&
                getPosition().y - GameManager.allowedOffset - halfSizes.y <= pPoint.y && getPosition().y + GameManager.allowedOffset + halfSizes.y >= pPoint.y);
    }

}
