package com.roboter5123.robogames.service;

import com.roboter5123.robogames.model.GameState;

import java.util.HashMap;

public class GameService {

    private final GameState gameState;


    public GameService() {
        this.gameState = new GameState(false, false, new HashMap<>());
    }

    public boolean isGameStarted() {
        return this.gameState.isGameStarted();
    }

    public boolean isGameStarting() {
        return this.gameState.isGameStarting();
    }

    public void setGameStarting(boolean gameStarting) {
        this.gameState.setGameStarting(gameStarting);
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameState.setGameStarted(gameStarted);
    }


}
