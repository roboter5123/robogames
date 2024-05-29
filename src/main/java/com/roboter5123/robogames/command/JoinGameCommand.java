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
    private final Random random;
    private final String arenaName;

    public JoinGameCommand(Player player, LanguageService languageService, GameService gameService, PlayerService playerService, SpawnService spawnService, String arenaName) {
        this.player = player;
        this.languageService = languageService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.arenaName = arenaName;
        this.random = new Random();
    }

    @Override
    public void run() {
        if (this.gameService.isGameStarted(this.arenaName) || this.gameService.isGameStarting(this.arenaName)) {
            player.sendMessage(this.languageService.getMessage("join.game-started"));
            return;
        }

        if (playerService.getInGamePlayers(this.arenaName).contains(player)) {
            player.sendMessage(this.languageService.getMessage("join.already-joined"));
            return;
        }

        List<Location> freeSpawnPoints = this.spawnService.getSpawnFreePoints(this.arenaName);
        if (freeSpawnPoints.isEmpty()) {
            player.sendMessage(this.languageService.getMessage("join.not-enough-spawns"));
            return;
        }
        Location spawnPoint = freeSpawnPoints.get(this.random.nextInt(0, freeSpawnPoints.size()));
        this.spawnService.addPlayerSpawn(this.arenaName, player, spawnPoint);
        player.teleport(spawnPoint);

        this.playerService.addInGamePlayer(this.arenaName, player);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.sendMessage(this.languageService.getMessage("join.success"));
    }
}
