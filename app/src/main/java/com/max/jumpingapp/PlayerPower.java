package com.max.jumpingapp;

/**
 * Created by normal on 24.10.2015.
 */
public class PlayerPower {
    private int playerPower;
    private int accelerator;
    private int minAccelerator=10;

    public PlayerPower() {
        this.playerPower = 0;
        this.accelerator = 0;
    }

    private void resetAccelerator() {
        accelerator = 0;
    }

    public void accelerate(double maxHeight, Trampolin trampolin) {
        if(playerPower<0){
            return;
        }
        accelerator += playerPower * trampolin.calcAccelerationFactor(maxHeight);
        if(accelerator > maxHeight){
            accelerator = (int) maxHeight;
        }
        if(maxHeight < minAccelerator){
            accelerator = minAccelerator;
        }
    }

    public void decelerate(double decelerateBy) {
        accelerator -= Math.abs(decelerateBy);
    }

    public boolean noPower() {
        return (this.playerPower <= 0);
    }

    public void activateAccelaration(Player player) {
        if(0 != accelerator){
            player.activateAccelaration(accelerator);
            resetAccelerator();
        }
    }

    public void decreasePower(double decreaseBy) {
        playerPower-= decreaseBy;
        if(playerPower < 0){
            playerPower = -1;
        }
    }

    public void resetPower() {
        playerPower = 0;
    }

    public void increasePower(double increaseBy) {
        playerPower+= increaseBy;
    }
}
