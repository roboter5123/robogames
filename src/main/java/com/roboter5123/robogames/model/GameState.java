package com.roboter5123.robogames.model;

public class GameState {

    private boolean gameStarted;
    private boolean gameStarting;

    private Long timer;

    public GameState(boolean gameStarted, boolean gameStarting) {
        this.gameStarted = gameStarted;
        this.gameStarting = gameStarting;
        this.timer = 0L;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGameStarting() {
        return gameStarting;
    }

    public void setGameStarting(boolean gameStarting) {
        this.gameStarting = gameStarting;
    }

    public Long getTimer() {
        return timer;
    }

    public void setTimer(Long timer) {
        this.timer = timer;
    }
}
