package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.model.Arena;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;

public class ArenaService {

	private final RoboGames roboGames;
	private Arena arena;
	private static String arenaFileName = "arena.yml";

	public ArenaService(RoboGames roboGames) {
		this.roboGames = roboGames;
	}

	public void loadArenaConfig() {
		File arenaFile = new File(this.roboGames.getDataFolder(), arenaFileName);
		if (!arenaFile.exists()) {
			arenaFile.getParentFile().mkdirs();
			this.roboGames.saveResource("arena.yml", false);
		}
		YamlConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
		this.arena = convertToArena(arenaConfig);
	}

	public World getWorld(String worldName) {
		return this.roboGames.getServer().getWorld(worldName);
	}

	public Arena getArena() {
		return this.arena;
	}

	public void createArena(Arena arena) {
		File arenaFile = new File(this.roboGames.getDataFolder(), "arena.yml");
		if (!arenaFile.exists()) {
			arenaFile.getParentFile().mkdirs();
			this.roboGames.saveResource("arena.yml", false);
		}
		YamlConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);

		arenaConfig.set("region.world", arena.getWorld());
		arenaConfig.set("region.pos1.x", arena.getPos1().getX());
		arenaConfig.set("region.pos1.y", arena.getPos1().getY());
		arenaConfig.set("region.pos1.z", arena.getPos1().getZ());
		arenaConfig.set("region.pos2.x", arena.getPos2().getX());
		arenaConfig.set("region.pos2.y", arena.getPos2().getY());
		arenaConfig.set("region.pos2.z", arena.getPos2().getZ());

		try {
			arenaConfig.save(arenaFile);
			this.arena = arena;
		} catch (IOException ignored) {
		}
	}

	private Arena convertToArena(YamlConfiguration arenaConfig) {
		
		Arena convertedArena = new Arena();
		String arenaWorldName = arenaConfig.getString("region.world");
		convertedArena.setWorldName(arenaWorldName);

		Double pos1X = arenaConfig.getDouble("region.pos1.x");
		Double pos1Y = arenaConfig.getDouble("region.pos1.y");
		Double pos1Z = arenaConfig.getDouble("region.pos1.z");
		World arenaWorld = this.roboGames.getServer().getWorld(arenaWorldName);
		Location pos1 = new Location(arenaWorld, pos1X, pos1Y, pos1Z);
		convertedArena.setPos1(pos1);

		Double pos2X = arenaConfig.getDouble("region.pos1.x");
		Double pos2Y = arenaConfig.getDouble("region.pos1.y");
		Double pos2Z = arenaConfig.getDouble("region.pos1.z");
		Location pos2 = new Location(arenaWorld, pos2X, pos2Y, pos2Z);
		convertedArena.setPos2(pos2);

		Double lobbyX = arenaConfig.getDouble("region.lobby.x");
		Double lobbyY = arenaConfig.getDouble("region.lobby.y");
		Double lobbyZ = arenaConfig.getDouble("region.lobby.z");
		String lobbyWorldName = arenaConfig.getString("region.lobby.world");
		World lobbyWorld = this.roboGames.getServer().getWorld(lobbyWorldName);
		Location lobbySpawn = new Location(lobbyWorld, lobbyX, lobbyY, lobbyZ);
		convertedArena.setLobbySpawn(lobbySpawn);
		
		return convertedArena;
	}
}
