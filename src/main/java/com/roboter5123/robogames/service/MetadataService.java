package com.roboter5123.robogames.service;

import org.bukkit.entity.Player;

public interface MetadataService {

    void setMetaDataOnPlayer(Player player, String key, Object value);
}
