package com.maxrick.bounceit.types;

/**
 * Created by max on 4/12/2016.
 */
public class PowerPercent {
    private final double value;

    public PowerPercent(double value){
        this.value = value;
    }
    public PowerPercent(){
        this(0);
    }

    public PowerPercent cap() {
        if(value < 0){
            return new PowerPercent();}
        if (value > 100) {
            return new PowerPercent();
        }
        return this;
    }

    public double value() {//@// TODO: 4/12/2016 delete this method
        return value;
    }
}
