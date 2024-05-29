package com.roboter5123.robogames.service;

public interface GameService {

    boolean isGameStarted(String arenaName);

    boolean isGameStarting(String arenaName);

    void setGameStarting(String arenaName, boolean gameStarting);

    void setGameStarted(String arenaName, boolean gameStarted);

    Long getTimer(String arenaName);

    void setTimerTicks(String arenaName, Long timer);
}
