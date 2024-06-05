package com.roboter5123.robogames.service;

import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.roboter5123.robogames.service.model.Arena;
import com.roboter5123.robogames.tasks.command.ScanArenaCommand;

public interface ArenaService {

	void addSpawn(Player player, Location newSpawnPoint);

	void createArena(Player player, String arenaName);

	void scanArena(Player player, String arenaName);
}
