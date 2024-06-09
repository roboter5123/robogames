package com.roboter5123.robogames.repository;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LobbyRepositoryImpl implements LobbyRepository {

    private final RoboGames roboGames;
    private final ConfigRepository configRepository;
    private final Map<String, Location> lobbies;
    private static final String LOBBIES_FILE_NAME = "lobbies.yml";


    public LobbyRepositoryImpl(RoboGames roboGames, ConfigRepository configRepository) {
        this.roboGames = roboGames;
        this.configRepository = configRepository;
        this.lobbies = new HashMap<>();
    }

    public void loadLobbiesConfig() {
        this.lobbies.clear();
        File lobbiesFile = this.configRepository.loadConfigFile(LOBBIES_FILE_NAME);
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
        File lobbiesFile = this.configRepository.loadConfigFile(LOBBIES_FILE_NAME);
        YamlConfiguration lobbiesConfig = YamlConfiguration.loadConfiguration(lobbiesFile);
        ConfigurationSection configSection = convertToConfigurationSection(lobby);
        lobbiesConfig.set(lobbyName, configSection);
        lobbiesConfig.save(lobbiesFile);
        this.lobbies.put(lobbyName, lobby);
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
