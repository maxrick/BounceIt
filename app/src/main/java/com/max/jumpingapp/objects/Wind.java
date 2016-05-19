package com.max.jumpingapp.objects;

import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.XPosition;

import java.util.Random;

/**
 * Created by max on 3/30/2016.
 */
public class Wind {
    protected int chance = 20;
    protected Random random;

    public Wind(){
        random = new Random();
    }

    public void blow(XPosition xPosition, Height curHeight) {
        if(curHeight.isPositive()&&chance>0){
            double chance = random.nextInt(this.chance);
            if(chance == 1){
                int windPower = random.nextInt(curHeight.devideBy(15));
                windPower = random.nextBoolean()? windPower : -windPower;
                xPosition.velocityByWind(windPower);
            }
        }

    }
    public void moreWind(){
        chance -= 5;
        if(chance <3){
            chance = 3;
        }
    }
}
