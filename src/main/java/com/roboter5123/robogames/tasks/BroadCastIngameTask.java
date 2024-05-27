package com.roboter5123.robogames.tasks;

import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.PlayerService;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastIngameTask extends BukkitRunnable {

    private final PlayerService playerService;
    private final String message;

    public BroadCastIngameTask( PlayerService playerService, String message) {
        this.playerService = playerService;
        this.message = message;
    }

    @Override
    public void run() {
        this.playerService.getInGamePlayers().forEach(player -> player.sendMessage(this.message));
    }
}
