package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;

public class ConfigServiceImpl implements ConfigService{

    private final RoboGames roboGames;

    public ConfigServiceImpl(RoboGames roboGames) {
        this.roboGames = roboGames;
    }

    public void checkConfigKeys() {
        YamlConfiguration pluginConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.roboGames.getResource("config.yml")));
        File serverConfigFile = new File(this.roboGames.getDataFolder(), "config.yml");
        YamlConfiguration serverConfig = YamlConfiguration.loadConfiguration(serverConfigFile);
        Set<String> keys = pluginConfig.getKeys(true);

        for (String key : keys) {
            if (!serverConfig.contains(key)) {
                serverConfig.set(key, pluginConfig.get(key));
                this.roboGames.getLogger().log(Level.WARNING,"&cMissing key: {}", key);
            }
        }

        try {
            serverConfig.save(serverConfigFile);
        } catch (IOException e) {
            this.roboGames.getServer().getLogger().severe(Arrays.toString(e.getStackTrace()));
        }
    }

    public FileConfiguration getConfig() {
        return this.roboGames.getConfig();
    }


    public int getMaxPlayers() {
        return getConfig().getInt("max-players");
    }

    @Override
    public File loadConfigFile(String fileName) {
        File configFile = new File(this.roboGames.getDataFolder(), fileName);
        if (configFile.exists()) {
            return configFile;
        }
        configFile.getParentFile().mkdirs();
        this.roboGames.saveResource(fileName, false);
        return new File(this.roboGames.getDataFolder(), fileName);
    }
}
