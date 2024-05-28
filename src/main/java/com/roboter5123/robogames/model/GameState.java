package com.roboter5123.robogames.model;

public class GameState {

    private boolean gameStarted;
    private boolean gameStarting;

    public GameState(boolean gameStarted, boolean gameStarting) {
        this.gameStarted = gameStarted;
        this.gameStarting = gameStarting;
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
}
