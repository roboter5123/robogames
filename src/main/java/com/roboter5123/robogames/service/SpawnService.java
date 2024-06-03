package com.roboter5123.robogames.service;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SpawnService {

    void loadSpawnConfig();

    Map<Player, Location> getPlayerSpawns(String arena);

    void createSpawn(String arenaName, Location spawn) throws IOException;

    void clearAllSpawns(String arenaName) throws IOException;

    List<Location> getAllSpawns(String arenaName);

    void clearPlayerSpawns(String arenaName);

    void removePlayerSpawnPoint(String arenaName, Player player);

    List<Location> getSpawnFreePoints(String arenaName);

    void addPlayerSpawn(String arenaName, Player player, Location spawn);
}
