package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LobbyServiceImpl implements LobbyService{

    private final RoboGames roboGames;
    private final Map<String, Location> lobbies;
    private static final String SPAWNS_FILE_NAME = "lobbies.yml";


    public LobbyServiceImpl(RoboGames roboGames) {
        this.roboGames = roboGames;
        this.lobbies = new HashMap<>();
    }

    public void loadLobbiesConfig() {
        this.lobbies.clear();
        File lobbiesFile = loadLobbiesFile();
        YamlConfiguration lobbiesConfig = YamlConfiguration.loadConfiguration(lobbiesFile);
        Set<String> lobbyNames = lobbiesConfig.getConfigurationSection("").getKeys(false);
        for (String lobbyName : lobbyNames) {
            ConfigurationSection lobby = lobbiesConfig.getConfigurationSection(lobbyName);
            if (lobby == null) {
                continue;
            }
            this.lobbies.put(lobbyName, convertToLocation(lobby));
        }
    }

    public Location getLobby(String arenaName){
        return this.lobbies.get(arenaName);
    }
    public void createLobby(String lobbyName, Location lobby) throws IOException {
        File lobbiesFile = loadLobbiesFile();
        YamlConfiguration lobbiesConfig = YamlConfiguration.loadConfiguration(lobbiesFile);
        ConfigurationSection configSection = convertToConfigurationSection(lobby);
        lobbiesConfig.set(lobbyName, configSection);
        lobbiesConfig.save(lobbiesFile);
        this.lobbies.put(lobbyName, lobby);
    }

    @NotNull
    private File loadLobbiesFile() {
        File spawnsFile = new File(this.roboGames.getDataFolder(), SPAWNS_FILE_NAME);
        if (!spawnsFile.exists()) {
            spawnsFile.getParentFile().mkdirs();
            this.roboGames.saveResource(SPAWNS_FILE_NAME, false);
            return new File(this.roboGames.getDataFolder(), SPAWNS_FILE_NAME);
        }
        return spawnsFile;
    }

    private ConfigurationSection convertToConfigurationSection(Location location) {
        ConfigurationSection configurationSection = new YamlConfiguration();
        configurationSection.set("world", location.getWorld().getName());

        configurationSection.set("x", location.getX());
        configurationSection.set("y", location.getY());
        configurationSection.set("z", location.getZ());
        return configurationSection;
    }

    private Location convertToLocation(ConfigurationSection lobbyConfig) {

        String worldName = lobbyConfig.getString("world");
        World world = this.roboGames.getServer().getWorld(worldName);
        double x = lobbyConfig.getDouble("x");
        double y = lobbyConfig.getDouble("y");
        double z = lobbyConfig.getDouble("z");
        return new Location(world, x, y, z);
    }
}
