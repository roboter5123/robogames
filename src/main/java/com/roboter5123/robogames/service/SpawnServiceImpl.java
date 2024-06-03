package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpawnServiceImpl implements SpawnService {

    private final RoboGames roboGames;
    private final Map<String, List<Location>> spawns;
    private final Map<String, Map<Player, Location>> playerSpawns;

    private final Map<String, List<Location>> freeSpawnPoints;
    private static final String SPAWNS_FILE_NAME = "spawns.yml";
    private final ConfigService configService;


    public SpawnServiceImpl(RoboGames roboGames, ConfigService configService) {
        this.roboGames = roboGames;
        this.configService = configService;
        this.spawns = new HashMap<>();
        this.playerSpawns = new HashMap<>();
        this.freeSpawnPoints = new HashMap<>();
    }

    public void loadSpawnConfig() {
        this.spawns.clear();
        File spawnFile = this.configService.loadConfigFile(SPAWNS_FILE_NAME);
        YamlConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
        Set<String> arenaNames = spawnConfig.getConfigurationSection("").getKeys(false);
        for (String arenaName : arenaNames) {
            List<Map<?, ?>> spawnMaps = spawnConfig.getMapList(arenaName);
            if (spawnMaps.isEmpty()) {
                continue;
            }
            List<Location> spawnLocations = spawnMaps.stream().map(this::convertToLocation).toList();
            this.spawns.put(arenaName, new ArrayList<>(spawnLocations));
            this.freeSpawnPoints.put(arenaName, new ArrayList<>(spawnLocations));
            this.playerSpawns.put(arenaName, new HashMap<>());
        }
    }

    public List<Location> getAllSpawns(String arenaName) {
        spawnGuard(arenaName);
        return this.spawns.get(arenaName);
    }

    public void createSpawn(String arenaName, Location spawn) throws IOException {
        if (!this.spawns.containsKey(arenaName)) {
            List<Location> spawnPoints = new ArrayList<>();
            spawnPoints.add(spawn);
            this.spawns.put(arenaName, spawnPoints);
            this.freeSpawnPoints.put(arenaName, spawnPoints);
            this.playerSpawns.put(arenaName, new HashMap<>());
        } else {
            List<Location> locations = this.spawns.get(arenaName);
            locations.add(spawn);
            this.freeSpawnPoints.get(arenaName).add(spawn);
        }
        File spawnFile = this.configService.loadConfigFile(SPAWNS_FILE_NAME);
        YamlConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
        List<Map<String, String>> yamlSpawns = this.spawns.get(arenaName).stream().map(this::convertToYaml).toList();
        roboGames.getServer().getLogger().info(yamlSpawns.toString());
        roboGames.getServer().getLogger().info(this.spawns.toString());
        spawnConfig.set(arenaName, yamlSpawns);
        spawnConfig.save(spawnFile);
    }

    public void clearAllSpawns(String arenaName) throws IOException {
        if (this.spawns.containsKey(arenaName)) {
            this.spawns.get(arenaName).clear();
        }
        if (this.playerSpawns.containsKey(arenaName)) {
            this.playerSpawns.get(arenaName).clear();
        }
        if (this.freeSpawnPoints.containsKey(arenaName)) {
            this.freeSpawnPoints.get(arenaName).clear();
        }
        File spawnFile = this.configService.loadConfigFile(SPAWNS_FILE_NAME);
        YamlConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
        if (spawnConfig.contains(arenaName)) {
            spawnConfig.set(arenaName, null);
            spawnConfig.save(spawnFile);
        }
    }

    public Map<Player, Location> getPlayerSpawns(String arena) {
        playerSpawnsGuard(arena);
        return this.playerSpawns.get(arena);
    }

    public void addPlayerSpawn(String arenaName, Player player, Location spawn) {
        this.freeSpawnPoints.get(arenaName).remove(spawn);
        this.playerSpawns.get(arenaName).put(player, spawn);
    }

    public void removePlayerSpawnPoint(String arenaName, Player player) {
        Location spawn = this.playerSpawns.get(arenaName).remove(player);
        this.freeSpawnPoints.get(arenaName).add(spawn);
    }

    public void clearPlayerSpawns(String arenaName) {
        this.playerSpawns.get(arenaName).clear();
    }

    private void playerSpawnsGuard(String arena) {
        if (!playerSpawns.containsKey(arena)) {
            this.playerSpawns.put(arena, new HashMap<>());
        }
    }

    private void spawnGuard(String arenaName) {
        if (!this.spawns.containsKey(arenaName)) {
            this.spawns.put(arenaName, new ArrayList<>());
            this.freeSpawnPoints.put(arenaName, new ArrayList<>());
            this.playerSpawns.put(arenaName, new HashMap<>());
        }
    }

    private Map<String, String> convertToYaml(Location location) {
        Map<String, String> map = new HashMap<>();
        map.put("world", location.getWorld().getName());
        map.put("x", String.valueOf(location.getX()));
        map.put("y", String.valueOf(location.getY()));
        map.put("z", String.valueOf(location.getZ()));
        return map;
    }

    public List<Location> getSpawnFreePoints(String arenaName) {
        return this.freeSpawnPoints.get(arenaName);
    }

    private Location convertToLocation(Map<?, ?> map) {

        String worldName = (String) map.get("world");
        World world = this.roboGames.getServer().getWorld(worldName);
        double x = Double.parseDouble((String) map.get("x"));
        double y = Double.parseDouble((String) map.get("y"));
        double z = Double.parseDouble((String) map.get("z"));
        return new Location(world, x, y, z);
    }
}
