package com.roboter5123.robogames.repository;

import org.bukkit.entity.Player;

import java.util.List;

public interface PlayerRepository {

    void removeAllIngamePlayersByArenaName(String arenaName);

    void createIngamePlayer(String arenaName, Player player);

    List<Player> getInGamePlayersByArenaName(String arenaName);

    List<Player> getAlivePlayersByArenaName(String arenaName);

    void removeAlivePlayerByArenaName(String arenaName, Player player);

    void removeIngamePlayerByArenaName(String arenaName, Player player);

    void setMetaDataOnPlayer(Player player, String key, Object value);

    String getArenaNameByPlayer(Player player);
}
