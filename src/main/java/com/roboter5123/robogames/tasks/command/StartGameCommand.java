package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.service.*;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import com.roboter5123.robogames.tasks.GameLoopTask;
import org.bukkit.scheduler.BukkitRunnable;

public class StartGameCommand extends BukkitRunnable {

    private final GameService gameService;
    private final SchedulerService schedulerService;
    private final LanguageService languageService;
    private final PlayerService playerService;
    private final SpawnService spawnService;
    private final String arenaName;
    private final ChestService chestService;
    private final ArenaService arenaService;
    private final ItemService itemService;
    private final ConfigService configService;

    public StartGameCommand(GameService gameService, SchedulerService schedulerService, LanguageService languageService, PlayerService playerService, SpawnService spawnService, ChestService chestService, ArenaService arenaService, ItemService itemService, String arenaName, ConfigService configService) {
        this.gameService = gameService;
        this.schedulerService = schedulerService;
        this.languageService = languageService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.chestService = chestService;
        this.arenaService = arenaService;
        this.arenaName = arenaName;
        this.itemService = itemService;
        this.configService = configService;
    }

    @Override
    public void run() {

        if (this.gameService.isGameStarted(arenaName) || this.gameService.isGameStarting(arenaName)){
            return;
        }

        if (this.configService.getMinPlayers() > this.playerService.getAlivePlayers(this.arenaName).size()){
            return;
        }

        this.gameService.setGameStarting(this.arenaName, true);
        this.gameService.setTimerTicks(this.arenaName, 0L);
        new RefillChestsCommand(this.chestService, this.arenaService, this.itemService, this.arenaName).run();
        broadcastCountdown("startgame.20-s", 0L);
        broadcastCountdown("startgame.15-s", 20L * 5);
        broadcastCountdown("startgame.10-s", 20L * 10);
        broadcastCountdown("startgame.5-s", 20L * 15);
        broadcastCountdown("startgame.4-s", 20L * 16);
        broadcastCountdown("startgame.3-s", 20L * 17);
        broadcastCountdown("startgame.2-s", 20L * 18);
        broadcastCountdown("startgame.1-s", 20L * 19);
        broadcastCountdown("game.game-start", 20L * 20);

        BukkitRunnable startGameTask = new BukkitRunnable() {
            public void run() {
                gameService.setGameStarted(arenaName, true);
                gameService.setGameStarting(arenaName, false);
            }
        };
        this.schedulerService.scheduleDelayedTask(startGameTask, 20L * 20);

        BukkitRunnable gameCheckTask = new GameLoopTask(gameService, playerService, spawnService, languageService, 20L, arenaName);
        this.schedulerService.scheduleRepeatingTask(gameCheckTask, 20L * 20 + 2, 20L);
    }

    private void broadcastCountdown(String messageKey, long ticksUntil) {
        String message = languageService.getMessage(messageKey);
        BukkitRunnable gameStartsInTask = new BroadCastIngameTask(this.playerService, message, this.arenaName);
        this.schedulerService.scheduleDelayedTask(gameStartsInTask, ticksUntil);
    }
}
