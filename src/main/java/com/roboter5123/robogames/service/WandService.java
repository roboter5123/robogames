package com.roboter5123.robogames.service;

import org.bukkit.entity.Player;

public interface WandService {
    void giveArenaWand(Player player, String arenaName);

    void giveSpawnWand(Player player, String arenaName);
}
