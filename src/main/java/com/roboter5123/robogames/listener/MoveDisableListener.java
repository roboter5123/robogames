package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.service.ArenaService;
import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.PlayerService;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveDisableListener implements Listener {

    private final PlayerService playerService;
    private final GameService gameService;
    private final ArenaService arenaService;

    public MoveDisableListener(PlayerService playerService, GameService gameService, ArenaService arenaService) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.arenaService = arenaService;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        String arenaName = this.playerService.getArenaNameByPlayer(player);
        if (arenaName == null) {
            return;
        }

        if (this.gameService.isGameStarted(arenaName)) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to.getX() == from.getX() && to.getZ() == from.getZ()) {
            return;
        }

        if (!this.arenaService.isInArenaBounds(arenaName, to)) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        event.setCancelled(true);
    }
}
