package com.max.jumpingapp;

public class Animator {
    private final PlayerStatus playerStatus;

    public Animator(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    void animate(boolean touching) {
//        if (touching && (playerStatus.getRect().height() - 3) >= playerStatus.getMinHeightRect()) {
//            playerStatus.getRect().top += 5;
//        }
//        if (!touching && ((playerStatus.getRect().height() + 10 < playerStatus.getNormalHeightRect()) || playerStatus.isAnimateStrech())) {
//            if (!playerStatus.isAnimateStrech()) {
//                playerStatus.setAnimateStrech(true);
//                playerStatus.setMaxHeightRect(2 * playerStatus.getNormalHeightRect() - playerStatus.getRect().height());
//            }
//            playerStatus.getRect().top -= 10;
//            if (playerStatus.getRect().height() > playerStatus.getMaxHeightRect()) {
//                playerStatus.setAnimateStrech(false);
//            }
//        }
//        if (!touching && !playerStatus.isAnimateStrech() && playerStatus.getRect().height() - 15 > playerStatus.getNormalHeightRect()) {
//            playerStatus.getRect().top += 15;
//        }

    }
}