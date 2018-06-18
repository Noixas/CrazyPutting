package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class Contact {
    /*
    Holds the position of the contact
     */

    private Vector3 contactPoint;

    /*
    Holds the direction of the contact
     */

    private Vector3 contactNormal;

    /*
    Holds the depth of penetration at the contact point
     */
    float penetration;
}
