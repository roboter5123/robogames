package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class MetadataServiceImpl implements MetadataService{

    private final RoboGames roboGames;

    public MetadataServiceImpl(RoboGames roboGames) {
        this.roboGames = roboGames;
    }

    public void setMetaDataOnPlayer(Player player, String key, Object value) {
        player.setMetadata(key, new FixedMetadataValue(roboGames, value));
    }
}
