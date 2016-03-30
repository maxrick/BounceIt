package com.max.jumpingapp;

import com.max.jumpingapp.objects.Trampolin;

/**
 * Created by normal on 29.10.2015.
 */
public class PlayerDiedException extends Throwable {
    public Trampolin trampolin;
    public  PlayerDiedException(Trampolin trampolin){
        this.trampolin = trampolin;
    }
}
