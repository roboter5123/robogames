package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.service.*;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class EndGameCommand extends BukkitRunnable {

    private final GameService gameService;
    private final SpawnService spawnService;
    private final PlayerService playerService;
    private final String arenaName;

    public EndGameCommand(GameService gameService, SpawnService spawnService, PlayerService playerService, String arenaName) {
        this.gameService = gameService;
        this.spawnService = spawnService;
        this.playerService = playerService;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {
        this.gameService.setGameStarted(this.arenaName, false);
        this.gameService.setGameStarting(this.arenaName,false);
        this.spawnService.clearPlayerSpawns(this.arenaName);

        List<Player> inGamePlayers = this.playerService.getInGamePlayers(this.arenaName);
        for (Player inGamePlayer : inGamePlayers) {
            inGamePlayer.setGameMode(GameMode.ADVENTURE);
            inGamePlayer.getInventory().clear();
            World world = inGamePlayer.getWorld();
            inGamePlayer.teleport(world.getSpawnLocation());
        }
        this.playerService.clearInGamePlayers(this.arenaName);
    }

}
