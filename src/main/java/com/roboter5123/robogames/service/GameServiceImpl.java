package com.roboter5123.robogames.service;

import com.roboter5123.robogames.repository.*;
import com.roboter5123.robogames.tasks.GameLoopTask;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class GameServiceImpl implements GameService {
    
    private final LanguageRepository languageRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final SpawnRepository spawnRepository;
    private final SchedulerService schedulerService;
    private final LobbyRepository lobbyRepository;
    private final ConfigRepository configRepository;
    private final ArenaService arenaService;
    private final Random random;

    public GameServiceImpl(LanguageRepository languageRepository, GameRepository gameRepository, PlayerRepository playerRepository, SpawnRepository spawnRepository, LobbyRepository lobbyRepository, SchedulerService schedulerService, ConfigRepository configRepository, ArenaService arenaService) {
        this.languageRepository = languageRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.spawnRepository = spawnRepository;
        this.lobbyRepository = lobbyRepository;
        this.schedulerService = schedulerService;
        this.configRepository = configRepository;
        this.arenaService = arenaService;
        this.random = new Random();
    }

    @Override
    public void joinGame(Player player, String arenaName) {
        if (arenaName == null){
            player.sendMessage(this.languageRepository.getMessage("join.arena-doesnt-exist"));
            return;
        }

        if (this.gameRepository.isGameStarted(arenaName) || this.gameRepository.isGameStarting(arenaName)) {
            player.sendMessage(this.languageRepository.getMessage("join.game-started"));
            return;
        }

        if (playerRepository.getInGamePlayersByArenaName(arenaName).contains(player)) {
            player.sendMessage(this.languageRepository.getMessage("join.already-joined"));
            return;
        }

        List<Location> freeSpawnPoints = this.spawnRepository.getSpawnFreePoints(arenaName);
        if (freeSpawnPoints.isEmpty()) {
            player.sendMessage(this.languageRepository.getMessage("join.not-enough-spawns"));
            return;
        }
        Location spawnPoint = freeSpawnPoints.get(this.random.nextInt(0, freeSpawnPoints.size()));
        this.spawnRepository.addPlayerSpawn(arenaName, player, spawnPoint);
        player.teleport(spawnPoint);

        this.playerRepository.createIngamePlayer(arenaName, player);
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.sendMessage(this.languageRepository.getMessage("join.success"));
    }

    @Override
    public void leaveGame(Player player) {
        String arenaName = this.playerRepository.getArenaNameByPlayer(player);

        if (arenaName == null){
            player.sendMessage(this.languageRepository.getMessage("leave.not-joined"));
            return;
        }

        if (this.gameRepository.isGameStarting(arenaName)) {
            player.sendMessage(this.languageRepository.getMessage("leave.game-is-starting"));
            return;
        }

        if (!playerRepository.getInGamePlayersByArenaName(arenaName).contains(player)) {
            player.sendMessage(this.languageRepository.getMessage("leave.not-joined"));
            return;
        }

        this.spawnRepository.removePlayerSpawnPoint(arenaName, player);
        this.playerRepository.removeIngamePlayerByArenaName(arenaName, player);

        if (this.arenaService.getArena(arenaName).getLobbyName() != null) {
            Location lobby = this.lobbyRepository.getLobby(arenaName);
            player.teleport(lobby);
        }else {
            World world = player.getWorld();
            Location worldSpawn = world.getSpawnLocation();
            player.teleport(worldSpawn);
        }
        player.sendMessage(this.languageRepository.getMessage("leave.success"));

        this.playerRepository.getInGamePlayersByArenaName(arenaName).forEach(ingamePlayer -> ingamePlayer.sendMessage(this.languageRepository.getMessage("leave.broadcast")));
    }

    @Override
    public void startGame(String arenaName) {

        if (this.gameRepository.isGameStarted(arenaName) || this.gameRepository.isGameStarting(arenaName)){
            return;
        }

        if (this.configRepository.getMinPlayers() > this.playerRepository.getAlivePlayersByArenaName(arenaName).size()){
            return;
        }

        this.gameRepository.setGameStarting(arenaName, true);
        this.gameRepository.setTimerTicks(arenaName, 0L);
        this.arenaService.refillChests(arenaName);
        broadcastCountdown("startgame.20-s", 0L, arenaName);
        broadcastCountdown("startgame.15-s", 20L * 5, arenaName);
        broadcastCountdown("startgame.10-s", 20L * 10, arenaName);
        broadcastCountdown("startgame.5-s", 20L * 15, arenaName);
        broadcastCountdown("startgame.4-s", 20L * 16, arenaName);
        broadcastCountdown("startgame.3-s", 20L * 17, arenaName);
        broadcastCountdown("startgame.2-s", 20L * 18, arenaName);
        broadcastCountdown("startgame.1-s", 20L * 19, arenaName);
        broadcastCountdown("game.game-start", 20L * 20, arenaName);

        BukkitRunnable startGameTask = new BukkitRunnable() {
            public void run() {
                gameRepository.setGameStarted(arenaName, true);
                gameRepository.setGameStarting(arenaName, false);
            }
        };
        this.schedulerService.scheduleDelayedTask(startGameTask, 20L * 20);

        BukkitRunnable gameCheckTask = new GameLoopTask(gameRepository, playerRepository, languageRepository, 20L, arenaName, this);
        this.schedulerService.scheduleRepeatingTask(gameCheckTask, 20L * 20 + 2, 20L);
    }

    @Override
    public void endGame(String arenaName) {
        this.gameRepository.setGameStarted(arenaName, false);
        this.gameRepository.setGameStarting(arenaName,false);
        this.spawnRepository.clearPlayerSpawns(arenaName);

        List<Player> inGamePlayers = this.playerRepository.getInGamePlayersByArenaName(arenaName);
        for (Player inGamePlayer : inGamePlayers) {
            inGamePlayer.setGameMode(GameMode.ADVENTURE);
            inGamePlayer.getInventory().clear();
            World world = inGamePlayer.getWorld();
            inGamePlayer.teleport(world.getSpawnLocation());
        }
        this.playerRepository.removeAllIngamePlayersByArenaName(arenaName);
    }

    private void broadcastCountdown(String messageKey, long ticksUntil, String arenaName) {
        String message = languageRepository.getMessage(messageKey);
        BukkitRunnable gameStartsInTask = new BukkitRunnable() {
            @Override
            public void run() {
                playerRepository.getInGamePlayersByArenaName(arenaName).forEach(player -> player.sendMessage(message));
            }
        };
        this.schedulerService.scheduleDelayedTask(gameStartsInTask, ticksUntil);
    }
}
