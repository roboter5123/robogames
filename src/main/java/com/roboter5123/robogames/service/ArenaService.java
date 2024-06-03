package com.roboter5123.robogames.service;

import com.roboter5123.robogames.service.model.Arena;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.Set;

public interface ArenaService {

    void loadArenaConfig();

    Arena getArena(String arena);

    void createArena(Arena arena) throws IOException;

    Set<String> getArenaNames();

    boolean isInArenaBounds(String arenaName, Location location);

    World getWorld(String worldName);
}
