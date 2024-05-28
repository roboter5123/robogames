package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.model.Arena;
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
        if (this.gameService.isGameStarted()) {
            return;
        }
        if (!this.playerService.getInGamePlayers().contains(player)) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getX() == to.getX() || from.getZ() == to.getZ()) {
            return;
        }

        Arena arena = this.arenaService.getArena();
        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();
        boolean isInXArenaBounds = to.getX() >= Math.min(pos1.getX(), pos2.getX()) && to.getX() <= Math.max(pos1.getX(), pos2.getX());
        boolean isInYArenaBounds = to.getY() >= Math.min(pos1.getY(), pos2.getY()) && to.getY() <= Math.max(pos1.getY(), pos2.getY());
        boolean isInZArenaBounds = to.getZ() >= Math.min(pos1.getZ(), pos2.getZ()) && to.getZ() <= Math.max(pos1.getZ(), pos2.getZ());

        if (!isInXArenaBounds || !isInYArenaBounds || !isInZArenaBounds) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        event.setCancelled(true);
    }
}
