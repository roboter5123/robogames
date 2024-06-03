package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.World;

public class WorldServiceImpl implements WorldService {

    private final RoboGames roboGames;

    public WorldServiceImpl(RoboGames roboGames) {
        this.roboGames = roboGames;
    }

    @Override
    public World getWorld(String worldName) {
        return roboGames.getServer().getWorld(worldName);
    }
}
