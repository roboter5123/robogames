package com.roboter5123.robogames.service;

import org.bukkit.entity.Player;

public interface GameService {

    void joinGame(Player player, String arenaName);

    void leaveGame(Player player);

    void startGame(String arenaName);

    void endGame(String arenaName);
}
