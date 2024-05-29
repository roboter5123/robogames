package com.roboter5123.robogames.service;

import com.roboter5123.robogames.model.GameState;

import java.util.HashMap;
import java.util.Map;

public class GameServiceImpl implements GameService{

    private final Map<String, GameState> gameStates;

    public GameServiceImpl() {
        this.gameStates = new HashMap<>();
    }

    public boolean isGameStarted(String arenaName) {
        gameStateGuard(arenaName);
        return this.gameStates.get(arenaName).isGameStarted();
    }

    public boolean isGameStarting(String arenaName) {
        gameStateGuard(arenaName);
        return this.gameStates.get(arenaName).isGameStarting();
    }

    public void setGameStarting(String arenaName, boolean gameStarting) {
        gameStateGuard(arenaName);
        this.gameStates.get(arenaName).setGameStarting(gameStarting);
    }

    public void setGameStarted(String arenaName, boolean gameStarted) {
        gameStateGuard(arenaName);
        this.gameStates.get(arenaName).setGameStarted(gameStarted);
    }

    public Long getTimer(String arenaName) {
        gameStateGuard(arenaName);
        return this.gameStates.get(arenaName).getTimer();
    }

    public void setTimerTicks(String arenaName, Long timer) {
        gameStateGuard(arenaName);
        this.gameStates.get(arenaName).setTimer(timer);
    }

    private void gameStateGuard(String arenaName) {
        if (!this.gameStates.containsKey(arenaName)){
            this.gameStates.put(arenaName, new GameState(false, false));
        }
    }
}
