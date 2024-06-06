package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.service.GameService;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeftListener implements Listener {

    private final GameService gameService;

    public PlayerLeftListener(GameService gameService) {
        this.gameService = gameService;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        this.gameService.leaveGame(player);
    }
}
