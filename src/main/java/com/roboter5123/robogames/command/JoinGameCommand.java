package com.roboter5123.robogames.command;

import com.roboter5123.robogames.service.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class JoinGameCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageService languageService;

    private final GameService gameService;
    private final PlayerService playerService;
    private final SpawnService spawnService;
    private final ArenaService arenaService;
    private final Random random;

    public JoinGameCommand(Player player, LanguageService languageService, GameService gameService, PlayerService playerService, SpawnService spawnService, ArenaService arenaService) {
        this.player = player;
        this.languageService = languageService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.arenaService = arenaService;
        this.random = new Random();
    }

    @Override
    public void run() {
        if (this.gameService.isGameStarted() || this.gameService.isGameStarting()) {
            player.sendMessage(languageService.getMessage("join.game-started"));
            return;
        }

        if (playerService.getInGamePlayers().contains(player)) {
            player.sendMessage(languageService.getMessage("join.already-joined"));
            return;
        }

        List<Location> freeSpawnPoints = this.spawnService.getSpawnFreePoints();
        if (freeSpawnPoints.isEmpty()) {
            player.sendMessage(languageService.getMessage("join.not-enough-spawns"));
            return;
        }
        Location spawnPoint = freeSpawnPoints.get(this.random.nextInt(0, freeSpawnPoints.size()));
        this.spawnService.setPlayerSpawn(player, spawnPoint);
        this.playerService.teleportPlayer(player, spawnPoint);

        this.playerService.addInGamePlayer(player);
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(languageService.getMessage("join.success"));
    }
}
