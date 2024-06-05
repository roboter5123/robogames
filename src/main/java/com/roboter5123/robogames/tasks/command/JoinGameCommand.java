package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.repository.SpawnRepository;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class JoinGameCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageRepository languageRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final SpawnRepository spawnRepository;
    private final Random random;
    private final String arenaName;

    public JoinGameCommand(Player player, LanguageRepository languageRepository, GameRepository gameRepository, PlayerRepository playerRepository, SpawnRepository spawnRepository, String arenaName) {
        this.player = player;
        this.languageRepository = languageRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.spawnRepository = spawnRepository;
        this.arenaName = arenaName;
        this.random = new Random();
    }

    @Override
    public void run() {

        if (this.arenaName == null){
            player.sendMessage(this.languageRepository.getMessage("join.arena-doesnt-exist"));
            return;
        }

        if (this.gameRepository.isGameStarted(this.arenaName) || this.gameRepository.isGameStarting(this.arenaName)) {
            player.sendMessage(this.languageRepository.getMessage("join.game-started"));
            return;
        }

        if (playerRepository.getInGamePlayers(this.arenaName).contains(player)) {
            player.sendMessage(this.languageRepository.getMessage("join.already-joined"));
            return;
        }

        List<Location> freeSpawnPoints = this.spawnRepository.getSpawnFreePoints(this.arenaName);
        if (freeSpawnPoints.isEmpty()) {
            player.sendMessage(this.languageRepository.getMessage("join.not-enough-spawns"));
            return;
        }
        Location spawnPoint = freeSpawnPoints.get(this.random.nextInt(0, freeSpawnPoints.size()));
        this.spawnRepository.addPlayerSpawn(this.arenaName, player, spawnPoint);
        player.teleport(spawnPoint);

        this.playerRepository.addInGamePlayer(this.arenaName, player);
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.sendMessage(this.languageRepository.getMessage("join.success"));
    }
}
