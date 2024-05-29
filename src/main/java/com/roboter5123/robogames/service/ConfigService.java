package com.roboter5123.robogames.service;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigService {

    void reloadConfig();

    void checkConfigKeys();

    FileConfiguration getConfig();

    int getMaxPlayers();
}
