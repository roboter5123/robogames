package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChestServiceImpl implements ChestService {

    private final RoboGames roboGames;
    private final ConfigService configService;
    private final Map<String, List<Location>> chests;
    private static final String CHESTS_FILE_NAME = "chests.yml";
    public ChestServiceImpl(RoboGames roboGames, ConfigService configService) {
        this.roboGames = roboGames;
        this.configService = configService;
        this.chests = new HashMap<>();
    }

    @Override
    public void loadChestConfig() {
        this.chests.clear();
        File chestsFile = this.configService.loadConfigFile(CHESTS_FILE_NAME);
        YamlConfiguration chestsConfig = YamlConfiguration.loadConfiguration(chestsFile);
        Set<String> arenaNames = Objects.requireNonNull(chestsConfig.getConfigurationSection("")).getKeys(false);
        for (String arenaName : arenaNames) {
            List<Map<?, ?>> chestMaps = chestsConfig.getMapList(arenaName);
            if (chestMaps.isEmpty()){
                continue;
            }
            List<Location> chestLocations = chestMaps.stream().map(this::convertToLocation).toList();
            this.chests.put(arenaName, new ArrayList<>(chestLocations));
        }
    }

    @Override
    public List<Location> getChestLocations(String arenaName) {
        this.chests.computeIfAbsent(arenaName, k -> new ArrayList<>());
        return this.chests.get(arenaName);
    }

    @Override
    public void addChest(String arenaName, Chest chest) throws IOException {
        if (!this.chests.containsKey(arenaName)) {
            List<Location> chestsLocations = new ArrayList<>();
            chestsLocations.add(chest.getLocation());
            this.chests.put(arenaName, chestsLocations);
        } else {
            List<Location> locations = this.chests.get(arenaName);
            locations.add(chest.getLocation());
        }
        File chestsFile = this.configService.loadConfigFile(CHESTS_FILE_NAME);
        YamlConfiguration chestsConfig = YamlConfiguration.loadConfiguration(chestsFile);
        List<Map<String, String>> yamlChests = this.chests.get(arenaName).stream().map(this::convertToYaml).toList();
        chestsConfig.set(arenaName, yamlChests);
        chestsConfig.save(chestsFile);
    }



    @Override
    public void removeAllChests(String arenaName) throws IOException {
        if (this.chests.containsKey(arenaName)) {
            this.chests.get(arenaName).clear();
        }
        File chestFile = this.configService.loadConfigFile(CHESTS_FILE_NAME);
        YamlConfiguration chestsConfig = YamlConfiguration.loadConfiguration(chestFile);
        if (chestsConfig.contains(arenaName)) {
            chestsConfig.set(arenaName, null);
            chestsConfig.save(chestFile);
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

    private Location convertToLocation(Map<?, ?> map) {

        String worldName = (String) map.get("world");
        World world = this.roboGames.getServer().getWorld(worldName);
        double x = Double.parseDouble((String) map.get("x"));
        double y = Double.parseDouble((String) map.get("y"));
        double z = Double.parseDouble((String) map.get("z"));
        return new Location(world, x, y, z);
    }
}
