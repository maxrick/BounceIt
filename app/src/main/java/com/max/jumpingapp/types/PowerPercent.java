package com.max.jumpingapp.types;

/**
 * Created by max on 4/12/2016.
 */
public class PowerPercent {
    double value;
    public PowerPercent(double value){
        this.value = value;
    }

    public void reset() {
        value = 0; //@// TODO: 4/12/2016 make immutable
    }

    public PowerPercent cap() {
        if(value < 0){value=0;}
        if (value > 100) {
            value=100;
        }
        return new PowerPercent(value);
    }

    public double value() {//@// TODO: 4/12/2016 delete this method
        return value;
    }
}
