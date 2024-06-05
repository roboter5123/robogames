package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveDisableListener implements Listener {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final ArenaRepository arenaRepository;

    public MoveDisableListener(PlayerRepository playerRepository, GameRepository gameRepository, ArenaRepository arenaRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.arenaRepository = arenaRepository;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        String arenaName = this.playerRepository.getArenaNameByPlayer(player);
        if (arenaName == null) {
            return;
        }

        if (this.gameRepository.isGameStarted(arenaName)) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to.getX() == from.getX() && to.getZ() == from.getZ()) {
            return;
        }

        if (!this.arenaRepository.isInArenaBounds(arenaName, to)) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        event.setCancelled(true);
    }
}
