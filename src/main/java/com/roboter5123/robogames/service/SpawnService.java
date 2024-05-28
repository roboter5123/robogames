package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.model.SpawnPoint;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpawnService {

    private final RoboGames roboGames;
    private List<SpawnPoint> freeSpawnPoints;

    private final Map<Player, SpawnPoint> playerSpawnPoints;
    private List<SpawnPoint> allSpawns;

    public SpawnService(RoboGames roboGames) {
        this.roboGames = roboGames;
        this.playerSpawnPoints = new HashMap<>();
        this.allSpawns = new ArrayList<>();
    }

    public void loadSpawnConfig() {
        YamlConfiguration spawnConfig = getSpawnConfig();
        this.allSpawns = new ArrayList<>(spawnConfig.getStringList("spawnpoints").stream().map(this::convertToSpawnPoint).toList());
        this.freeSpawnPoints = new ArrayList<>(this.allSpawns);
        this.playerSpawnPoints.clear();
    }

    public void clearAllSpawns() {
        this.freeSpawnPoints.clear();
        this.playerSpawnPoints.clear();
        this.allSpawns.clear();
        saveSpawnsToConfig();
    }

    public void addSpawn(SpawnPoint newSpawnPoint) {
        this.allSpawns.add(newSpawnPoint);
        this.freeSpawnPoints.add(newSpawnPoint);
        saveSpawnsToConfig();
    }

    public void setPlayerSpawn(Player player, SpawnPoint spawnPoint) {
        this.playerSpawnPoints.put(player, spawnPoint);
        this.freeSpawnPoints.remove(spawnPoint);
    }

    public void removePlayerSpawnPoint(Player player) {
        SpawnPoint removedSpawnPoint = this.playerSpawnPoints.remove(player);
        this.freeSpawnPoints.add(removedSpawnPoint);
    }

    public List<SpawnPoint> getSpawnFreePoints() {
        return this.freeSpawnPoints;
    }

    public Map<Player, SpawnPoint> getPlayerSpawnPoints() {
        return playerSpawnPoints;
    }

    public List<SpawnPoint> getAllSpawns() {
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

    private String convertToString(SpawnPoint spawn) {
        Location location = spawn.getLocation();
        return spawn.getWorld() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

    @NotNull
    private YamlConfiguration getSpawnConfig() {
        File spawnFile = new File(roboGames.getDataFolder(), "setspawn.yml");
        if (!spawnFile.exists()) {
            spawnFile.getParentFile().mkdirs();
            roboGames.saveResource("setspawn.yml", false);
        }

        YamlConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
        return spawnConfig;
    }

    private SpawnPoint convertToSpawnPoint(String spawnPointString) {
        String[] fields = spawnPointString.split(",");
        SpawnPoint spawnPoint = new SpawnPoint();
        spawnPoint.setWorld(fields[0]);
        Location location = new Location();
        location.setx(Double.parseDouble(fields[1]));
        location.sety(Double.parseDouble(fields[2]));
        location.setz(Double.parseDouble(fields[3]));
        spawnPoint.setLocation(location);
        return spawnPoint;
    }

    public void clearPlayerSpawns() {
        this.freeSpawnPoints = new ArrayList<>(this.allSpawns);
        this.playerSpawnPoints.clear();
    }
}
