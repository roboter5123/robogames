package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpawnService {

    private final RoboGames roboGames;
    private List<Location> freeSpawnPoints;

    private final Map<Player, Location> playerSpawnPoints;
    private List<Location> allSpawns;

    public SpawnService(RoboGames roboGames) {
        this.roboGames = roboGames;
        this.playerSpawnPoints = new HashMap<>();
        this.allSpawns = new ArrayList<>();
    }

    public void loadSpawnConfig() {
        YamlConfiguration spawnConfig = getSpawnConfig();
        this.allSpawns = new ArrayList<>(spawnConfig.getStringList("spawnpoints").stream().map(this::convertToLocation).toList());
        this.freeSpawnPoints = new ArrayList<>(this.allSpawns);
        this.playerSpawnPoints.clear();
    }

    public void clearAllSpawns() {
        this.freeSpawnPoints.clear();
        this.playerSpawnPoints.clear();
        this.allSpawns.clear();
        saveSpawnsToConfig();
    }

    public void addSpawn(Location newSpawnPoint) {
        this.allSpawns.add(newSpawnPoint);
        this.freeSpawnPoints.add(newSpawnPoint);
        saveSpawnsToConfig();
    }

    public void setPlayerSpawn(Player player, Location spawnPoint) {
        this.playerSpawnPoints.put(player, spawnPoint);
        this.freeSpawnPoints.remove(spawnPoint);
    }

    public void removePlayerSpawnPoint(Player player) {
        Location removedSpawnPoint = this.playerSpawnPoints.remove(player);
        this.freeSpawnPoints.add(removedSpawnPoint);
    }

    public List<Location> getSpawnFreePoints() {
        return this.freeSpawnPoints;
    }

    public Map<Player, Location> getPlayerSpawnPoints() {
        return playerSpawnPoints;
    }

    public List<Location> getAllSpawns() {
        return this.allSpawns;
    }

    private void saveSpawnsToConfig() {
        List<String> yamlSpawns = allSpawns.stream().map(this::convertToString).toList();
        YamlConfiguration spawnConfig = getSpawnConfig();
        spawnConfig.set("spawnpoints", yamlSpawns);
        File spawnFile = new File(roboGames.getDataFolder(), "setspawn.yml");
        try {
            spawnConfig.save(spawnFile);
        } catch (Exception ignored) {
        }
    }

    private String convertToString(Location spawn) {
        return spawn.getWorld().getName() + "," + spawn.getX() + "," + spawn.getY() + "," + spawn.getZ();
    }

    @NotNull
    private YamlConfiguration getSpawnConfig() {
        File spawnFile = new File(roboGames.getDataFolder(), "setspawn.yml");
        if (!spawnFile.exists()) {
            spawnFile.getParentFile().mkdirs();
            roboGames.saveResource("setspawn.yml", false);
        }

        return YamlConfiguration.loadConfiguration(spawnFile);
    }

    public void clearPlayerSpawns() {
        this.freeSpawnPoints = new ArrayList<>(this.allSpawns);
        this.playerSpawnPoints.clear();
    }

    private Location convertToLocation(String spawnPointString) {
        String[] fields = spawnPointString.split(",");
        double spawnPointX = Double.parseDouble(fields[1]);
        double spawnPointY = Double.parseDouble(fields[2]);
        double spawnPointZ = Double.parseDouble(fields[3]);
        World spawnPointWorld = this.roboGames.getServer().getWorld(fields[0]);
        return new Location(spawnPointWorld, spawnPointX, spawnPointY, spawnPointZ);
    }
}
