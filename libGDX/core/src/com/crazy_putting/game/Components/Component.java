package com.crazy_putting.game.Components;

import com.crazy_putting.game.GameObjects.GameObject;

public abstract class Component {
    /**
     Will be implemented in the next phase of the project 1.2.
     */
    protected GameObject _owner;
    public void setOwner( GameObject pGameObj)
    {
        _owner = pGameObj;
    }
}
