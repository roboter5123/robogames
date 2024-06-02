package com.roboter5123.robogames.service;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public interface ConfigService {

    void checkConfigKeys();

    FileConfiguration getConfig();

    int getMaxPlayers();

    File loadConfigFile(String fileName);
}
