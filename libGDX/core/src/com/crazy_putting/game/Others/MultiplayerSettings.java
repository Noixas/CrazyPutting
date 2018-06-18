package com.crazy_putting.game.Others;

public class MultiplayerSettings {
    public static int PlayerAmount = 1;
    public static int AllowedDistance = Integer.MAX_VALUE;
    public static boolean Simultaneous = true;
    public MultiplayerSettings(int pAmount, int pDistance, boolean pSimultaneous){
        PlayerAmount = pAmount;
        AllowedDistance = pDistance;
        Simultaneous = pSimultaneous;
    }
}
