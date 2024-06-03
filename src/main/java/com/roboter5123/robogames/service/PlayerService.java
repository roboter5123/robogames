package com.roboter5123.robogames.service;

import org.bukkit.entity.Player;

import java.util.List;

public interface PlayerService {

    void clearInGamePlayers(String arenaName);

    void addInGamePlayer(String arenaName, Player player);

    List<Player> getInGamePlayers(String arenaName);

    List<Player> getAlivePlayers(String arenaName);

    void removeAlivePlayer(String arenaName, Player player);

    void removeIngamePlayer(String arenaName, Player player);

    String getArenaNameByPlayer(Player player);
}
