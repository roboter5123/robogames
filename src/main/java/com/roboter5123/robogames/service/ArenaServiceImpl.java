package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.model.Arena;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ArenaServiceImpl implements ArenaService{

    private final RoboGames roboGames;
    private final ConfigService configService;
    private final Map<String, Arena> arenas;
    private static final String ARENAS_FILE_NAME = "arenas.yml";

    public ArenaServiceImpl(RoboGames roboGames, ConfigService configService) {
        this.roboGames = roboGames;
        this.configService = configService;
        this.arenas = new HashMap<>();
    }

    public void loadArenaConfig() {
        this.arenas.clear();
        File arenaFile = this.configService.loadConfigFile(ARENAS_FILE_NAME);
        YamlConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
        Set<String> arenaNames = arenaConfig.getConfigurationSection("").getKeys(false);
        for (String arenaName : arenaNames) {
            ConfigurationSection arenaObject = arenaConfig.getConfigurationSection(arenaName);
            if (arenaObject == null) {
                continue;
            }
            this.arenas.put(arenaName, convertToArena(arenaName, arenaObject));
        }
    }

    public Arena getArena(String arena) {
        return this.arenas.get(arena);
    }

    public void createArena(Arena arena) throws IOException {
        File arenaFile = this.configService.loadConfigFile(ARENAS_FILE_NAME);
        YamlConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
        ConfigurationSection configSection = convertToConfigurationSection(arena);
        arenaConfig.set(arena.getName(), configSection);
        arenaConfig.save(arenaFile);
        this.arenas.put(arena.getName(), arena);
    }

    public Set<String> getArenaNames() {
        return this.arenas.keySet();
    }

    @Override
    public boolean isInArenaBounds(String arenaName, Location location) {
        Arena arena = this.arenas.get(arenaName);
        if (arena == null){
            return false;
        }
        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();
        boolean isInXArenaBounds = location.getX() >= Math.min(pos1.getX(), pos2.getX()) && location.getX() <= Math.max(pos1.getX(), pos2.getX());
        boolean isInYArenaBounds = location.getY() >= Math.min(pos1.getY(), pos2.getY()) && location.getY() <= Math.max(pos1.getY(), pos2.getY());
        boolean isInZArenaBounds = location.getZ() >= Math.min(pos1.getZ(), pos2.getZ()) && location.getZ() <= Math.max(pos1.getZ(), pos2.getZ());
        return isInXArenaBounds && isInYArenaBounds && isInZArenaBounds;
    }

    private ConfigurationSection convertToConfigurationSection(Arena arena) {
        ConfigurationSection configurationSection = new YamlConfiguration();
        configurationSection.set("world", arena.getWorldName());
        configurationSection.set("lobby", arena.getLobbyName());

        configurationSection.set("pos1.x", arena.getPos1().getX());
        configurationSection.set("pos1.y", arena.getPos1().getY());
        configurationSection.set("pos1.z", arena.getPos1().getZ());

        configurationSection.set("pos2.x", arena.getPos2().getX());
        configurationSection.set("pos2.y", arena.getPos2().getY());
        configurationSection.set("pos2.z", arena.getPos2().getZ());
        return configurationSection;
    }

    private Arena convertToArena(String arenaName, ConfigurationSection arenaConfig) {

        Arena convertedArena = new Arena();
        convertedArena.setName(arenaName);
        String arenaWorldName = arenaConfig.getString("world");
        convertedArena.setWorldName(arenaWorldName);

        convertedArena.setLobbyName(arenaConfig.getString("lobby"));

        double pos1X = arenaConfig.getDouble("pos1.x");
        double pos1Y = arenaConfig.getDouble("pos1.y");
        double pos1Z = arenaConfig.getDouble("pos1.z");
        World arenaWorld = this.roboGames.getServer().getWorld(arenaWorldName);
        Location pos1 = new Location(arenaWorld, pos1X, pos1Y, pos1Z);
        convertedArena.setPos1(pos1);

        double pos2X = arenaConfig.getDouble("pos2.x");
        double pos2Y = arenaConfig.getDouble("pos2.y");
        double pos2Z = arenaConfig.getDouble("pos2.z");
        Location pos2 = new Location(arenaWorld, pos2X, pos2Y, pos2Z);
        convertedArena.setPos2(pos2);

        return convertedArena;
    }
}
