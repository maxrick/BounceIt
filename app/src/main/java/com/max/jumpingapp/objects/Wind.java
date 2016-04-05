package com.max.jumpingapp.objects;

import com.max.jumpingapp.types.XPosition;

import java.util.Random;

/**
 * Created by max on 3/30/2016.
 */
public class Wind {
    private static int CHANCE = 30;
    private Random random;

    public Wind(){
        random = new Random();
    }

    public void blow(XPosition xPosition, int curHeight) {
        if(curHeight >0){
            double chance = random.nextInt(CHANCE);
            if(chance == 1){
                int windPower = random.nextInt(curHeight/20);
                windPower = random.nextBoolean()? windPower : -windPower;
                xPosition.velocityByWind(windPower);
            }
        }

    }
    public static void moreWind(){
        CHANCE -= 5;
        if(CHANCE <3){
            CHANCE = 3;
        }
    }
}
