package com.roboter5123.robogames.model;

import org.bukkit.entity.Player;

import java.util.Map;

public class GameState {

    private boolean gameStarted;
    private boolean gameStarting;

    private Map<Player, PlayerState> playerStates;

    public GameState(boolean gameStarted, boolean gameStarting, Map<Player, PlayerState> playerStates) {
        this.gameStarted = gameStarted;
        this.gameStarting = gameStarting;
        this.playerStates = playerStates;
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

    public Map<Player, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(Map<Player, PlayerState> playerStates) {
        this.playerStates = playerStates;
    }
}
