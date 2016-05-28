package com.max.jumpingapp.util;

/**
 * Created by max on 5/28/2016.
 */
public class MathHelper {
    public static int getNthDigit(long number, int base, int n) {
        return (int) ((number / Math.pow(base, n - 1)) % base);
    }

    public static String makeStringOfDigits(long mergedValues, int base, int[] ints) {
        String returnString="";
        for(int number : ints){
            returnString+= String.valueOf(getNthDigit(mergedValues, base, number));
        }
        return returnString;
    }
}
