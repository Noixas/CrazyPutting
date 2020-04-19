package com.crazy_putting.game.Others;

import com.badlogic.gdx.math.Vector3;

import javax.swing.text.Position;
import java.util.Random;

public class Noise {

    private static Noise instance;
    private static Random random = new Random();


    public static Noise getInstance(){
        if(instance==null){
            instance = new Noise();
        }
        return instance;
    }

    private Noise(){

    }

    public float nextStandardNormal(){
        return (float) random.nextGaussian();
    }

    public float nextNormal(float mean, float stDev){
        if(stDev < 0){
            System.out.println("Standard deviation should be non-negative");
            return Float.parseFloat(null);
        }
        float result =(float) random.nextGaussian();
        return result * stDev + mean;
    }

    public int nextInt(int bound){
        return random.nextInt(bound+1);
    }

    public int nextInt(int from, int to){
        int result = random.nextInt(to-from+1);
        return result+from;
    }

    public float nextFloat(){
        return random.nextFloat();
    }

    public float nextFloat(float from, float to){
        float result = (to-from)*random.nextFloat() + from;
        return result;
    }

    public Velocity noiseVelocity(Velocity velocity, float stDev){
        velocity.Vx += nextNormal(0,stDev);
        velocity.Vy +=nextNormal(0,stDev);
        velocity.angle +=nextNormal(0,stDev);
        return velocity;
    }

    public Vector3 noisePosition(Vector3 someVector, float stDev){
        someVector.x += nextNormal(0,stDev);
        someVector.y += nextNormal(0,stDev);
        someVector.z += nextNormal(0,stDev);

        return someVector;

    }

}
