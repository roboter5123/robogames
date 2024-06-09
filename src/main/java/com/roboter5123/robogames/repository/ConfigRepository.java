package com.roboter5123.robogames.repository;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public interface ConfigRepository {

    void checkConfigKeys();

    FileConfiguration getConfig();

    int getMinPlayers();

    int getMaxPlayers();

    File loadConfigFile(String fileName);
}
