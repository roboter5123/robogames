package com.roboter5123.robogames.tasks;

import com.roboter5123.robogames.service.PlayerService;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastIngameTask extends BukkitRunnable {

    private final PlayerService playerService;
    private final String message;
    private final String arenaName;

    public BroadCastIngameTask(PlayerService playerService, String message, String arenaName) {
        this.playerService = playerService;
        this.message = message;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {
        this.playerService.getInGamePlayers(arenaName).forEach(player -> player.sendMessage(this.message));
    }
}
