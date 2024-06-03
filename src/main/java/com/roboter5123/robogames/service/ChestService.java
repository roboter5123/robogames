package com.roboter5123.robogames.service;

import org.bukkit.Location;
import org.bukkit.block.Chest;

import java.io.IOException;
import java.util.List;

public interface ChestService {

    void loadChestConfig();
    List<Location> getChestLocations(String arenaName);

    void addChest(String arenaName, Chest chest) throws IOException;

    void removeAllChests(String arenaName) throws IOException;
}
