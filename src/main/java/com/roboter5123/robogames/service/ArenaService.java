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
        arenaConfig.set("region.pos1.x", arena.getPos1().getx());
        arenaConfig.set("region.pos1.y", arena.getPos1().gety());
        arenaConfig.set("region.pos1.z", arena.getPos1().getz());
        arenaConfig.set("region.pos2.x", arena.getPos2().getx());
        arenaConfig.set("region.pos2.y", arena.getPos2().gety());
        arenaConfig.set("region.pos2.z", arena.getPos2().getz());

        try {
            arenaConfig.save(arenaFile);
            this.arena = arena;
        } catch (IOException ignored) {
        }
    }

    private Arena convertToArena(YamlConfiguration arenaConfig) {
        Arena convertedArena = new Arena();
        String worldName = arenaConfig.getString("region.world");
        convertedArena.setWorldName(worldName);

        Location lobbySpawn = new Location();
        lobbySpawn.setx(arenaConfig.getDouble("region.lobby.x"));
        lobbySpawn.sety(arenaConfig.getDouble("region.lobby.y"));
        lobbySpawn.setz(arenaConfig.getDouble("region.lobby.z"));
        lobbySpawn.setWorld(this.roboGames.getServer().getWorld(worldName));
        convertedArena.setLobbySpawn(lobbySpawn);

        Location pos1 = new Location();
        pos1.setx(arenaConfig.getDouble("region.pos1.x"));
        pos1.sety(arenaConfig.getDouble("region.pos1.y"));
        pos1.setz(arenaConfig.getDouble("region.pos1.z"));
        pos1.setWorld(this.roboGames.getServer().getWorld(worldName));
        convertedArena.setPos1(pos1);

        Location pos2 = new Location();
        pos2.setx(arenaConfig.getDouble("region.pos2.x"));
        pos2.sety(arenaConfig.getDouble("region.pos2.y"));
        pos2.setz(arenaConfig.getDouble("region.pos2.z"));
        pos2.setWorld(this.roboGames.getServer().getWorld(worldName));
        convertedArena.setPos2(pos2);
        return convertedArena;
    }
}
