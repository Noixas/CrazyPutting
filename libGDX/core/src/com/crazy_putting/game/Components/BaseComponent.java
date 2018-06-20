package com.crazy_putting.game.Components;

import com.crazy_putting.game.GameObjects.GameObject;

public abstract class BaseComponent {
    protected GameObject _owner;
    public void setOwner( GameObject pGameObj)
    {
        _owner = pGameObj;
    }


}
