package com.roboter5123.robogames.service;

import com.roboter5123.robogames.service.model.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ArenaService {

	void addSpawn(Player player, Location newSpawnPoint);

	void createArena(Player player, String arenaName);

	void scanArena(Player player, String arenaName);

	void refillChests(String arenaName);

	Arena getArena(String arenaName);
}
