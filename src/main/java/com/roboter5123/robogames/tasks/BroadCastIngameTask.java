package com.roboter5123.robogames.tasks;

import com.roboter5123.robogames.repository.PlayerRepository;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastIngameTask extends BukkitRunnable {

    private final PlayerRepository playerRepository;
    private final String message;
    private final String arenaName;

    public BroadCastIngameTask(PlayerRepository playerRepository, String message, String arenaName) {
        this.playerRepository = playerRepository;
        this.message = message;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {
        this.playerRepository.getInGamePlayers(arenaName).forEach(player -> player.sendMessage(this.message));
    }
}
