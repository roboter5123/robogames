package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.model.Arena;
import com.roboter5123.robogames.model.Coordinate;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

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
        arenaConfig.set("region.pos1.x", arena.getPos1().getxCoordinate());
        arenaConfig.set("region.pos1.y", arena.getPos1().getyCoordinate());
        arenaConfig.set("region.pos1.z", arena.getPos1().getzCoordinate());
        arenaConfig.set("region.pos2.x", arena.getPos2().getxCoordinate());
        arenaConfig.set("region.pos2.y", arena.getPos2().getyCoordinate());
        arenaConfig.set("region.pos2.z", arena.getPos2().getzCoordinate());

        try {
            arenaConfig.save(arenaFile);
            this.arena = arena;
        } catch (IOException ignored) {
        }
    }

    private Arena convertToArena(YamlConfiguration arenaConfig) {
        Arena convertedArena = new Arena();
        convertedArena.setWorld(arenaConfig.getString("region.world"));

        Coordinate pos1 = new Coordinate();
        pos1.setxCoordinate(arenaConfig.getDouble("region.pos1.x"));
        pos1.setyCoordinate(arenaConfig.getDouble("region.pos1.y"));
        pos1.setzCoordinate(arenaConfig.getDouble("region.pos1.z"));
        convertedArena.setPos1(pos1);

        Coordinate pos2 = new Coordinate();
        pos2.setxCoordinate(arenaConfig.getDouble("region.pos2.x"));
        pos2.setyCoordinate(arenaConfig.getDouble("region.pos2.y"));
        pos2.setzCoordinate(arenaConfig.getDouble("region.pos2.z"));
        convertedArena.setPos2(pos2);
        return convertedArena;
    }
}
