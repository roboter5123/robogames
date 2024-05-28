package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.model.Arena;
import org.bukkit.Location;
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
            this.roboGames.saveResource(arenaFileName, false);
            this.arena = null;
            return;
        }
        YamlConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
        if (!arenaConfig.contains("region")) {
            this.arena = null;
            return;
        }
        this.arena = convertToArena(arenaConfig);
    }

    public Arena getArena() {
        return this.arena;
    }

    public void createArena(Arena arena) {
        File arenaFile = new File(this.roboGames.getDataFolder(), arenaFileName);
        if (!arenaFile.exists()) {
            arenaFile.getParentFile().mkdirs();
            this.roboGames.saveResource(arenaFileName, false);
        }
        YamlConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);

        arenaConfig.set("region.world", arena.getWorldName());
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

        double pos1X = arenaConfig.getDouble("region.pos1.x");
        double pos1Y = arenaConfig.getDouble("region.pos1.y");
        double pos1Z = arenaConfig.getDouble("region.pos1.z");
        World arenaWorld = this.roboGames.getServer().getWorld(arenaWorldName);
        Location pos1 = new Location(arenaWorld, pos1X, pos1Y, pos1Z);
        convertedArena.setPos1(pos1);

        double pos2X = arenaConfig.getDouble("region.pos2.x");
        double pos2Y = arenaConfig.getDouble("region.pos2.y");
        double pos2Z = arenaConfig.getDouble("region.pos2.z");
        Location pos2 = new Location(arenaWorld, pos2X, pos2Y, pos2Z);
        convertedArena.setPos2(pos2);

        return convertedArena;
    }
}
