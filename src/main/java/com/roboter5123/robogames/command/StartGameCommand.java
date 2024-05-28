package com.roboter5123.robogames.command;

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

    public StartGameCommand(GameService gameService, SchedulerService schedulerService, LanguageService languageService, PlayerService playerService, SpawnService spawnService) {
        this.gameService = gameService;
        this.schedulerService = schedulerService;
        this.languageService = languageService;
        this.playerService = playerService;
        this.spawnService = spawnService;
    }

    @Override
    public void run() {
        this.gameService.setGameStarting(true);
        this.gameService.setTimerTicks(0L);

        broadcastCountdown("startgame.20-s", 0L);
        broadcastCountdown("startgame.15-s", 20L * 5);
        broadcastCountdown("startgame.10-s", 20L * 10);
        broadcastCountdown("startgame.5-s", 20L * 15);
        broadcastCountdown("startgame.4-s", 20L * 16);
        broadcastCountdown("startgame.3-s", 20L * 17);
        broadcastCountdown("startgame.2-s", 20L * 18);
        broadcastCountdown("startgame.1-s", 20L * 19);
        broadcastCountdown("game.game-start", 20L * 20);

        GameService gameService = this.gameService;
        BukkitRunnable startGameTask = new BukkitRunnable() {
            public void run() {
                gameService.setGameStarted(true);
                gameService.setGameStarting(false);
            }
        };
        this.schedulerService.scheduleDelayedTask(startGameTask, 20L * 20);

        BukkitRunnable gameCheckTask = new GameLoopTask(gameService, playerService, spawnService, languageService, 20L);
        this.schedulerService.scheduleRepeatingTask(gameCheckTask, 20L * 20 + 2, 20L);
    }

    private void broadcastCountdown(String messageKey, long ticksUntil) {
        String message = languageService.getMessage(messageKey);
        BukkitRunnable gameStartsInTask = new BroadCastIngameTask(this.playerService, message);
        this.schedulerService.scheduleDelayedTask(gameStartsInTask, ticksUntil);
    }
}
