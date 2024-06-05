package com.roboter5123.robogames.repository;

import org.bukkit.entity.Player;

import java.util.List;

public interface PlayerRepository {

    void clearInGamePlayers(String arenaName);

    void addInGamePlayer(String arenaName, Player player);

    List<Player> getInGamePlayers(String arenaName);

    List<Player> getAlivePlayers(String arenaName);

    void removeAlivePlayer(String arenaName, Player player);

    void removeIngamePlayer(String arenaName, Player player);

    void setMetaDataOnPlayer(Player player, String key, Object value);

    String getArenaNameByPlayer(Player player);
}
