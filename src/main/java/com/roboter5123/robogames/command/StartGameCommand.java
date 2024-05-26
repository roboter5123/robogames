package com.roboter5123.robogames.command;

import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.PlayerService;
import com.roboter5123.robogames.service.SchedulerService;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import org.bukkit.scheduler.BukkitRunnable;

public class StartGameCommand extends BukkitRunnable {
    private final GameService gameService;
    private final SchedulerService schedulerService;
    private final LanguageService languageService;
    private final PlayerService playerService;

    public StartGameCommand(GameService gameService, SchedulerService schedulerService, LanguageService languageService, PlayerService playerService) {
        this.gameService = gameService;
        this.schedulerService = schedulerService;
        this.languageService = languageService;
        this.playerService = playerService;
    }

    @Override
    public void run() {
        this.gameService.setGameStarting(true);

        BukkitRunnable gameStartsIn20S = new BroadCastIngameTask(this.languageService, this.playerService, "startgame.20-s");
        this.schedulerService.scheduleDelayedTask(gameStartsIn20S, 0L);

        BukkitRunnable gameStartsIn15S = new BroadCastIngameTask(this.languageService, this.playerService, "startgame.15-s");
        this.schedulerService.scheduleDelayedTask(gameStartsIn15S, 20L * 5);

        BukkitRunnable gameStartsIn10S = new BroadCastIngameTask(this.languageService, this.playerService, "startgame.10-s");
        this.schedulerService.scheduleDelayedTask(gameStartsIn10S, 20L * 10);

        BukkitRunnable gameStartsIn5S = new BroadCastIngameTask(this.languageService, this.playerService, "startgame.5-s");
        this.schedulerService.scheduleDelayedTask(gameStartsIn5S, 20L * 15);

        BukkitRunnable gameStartsIn4S = new BroadCastIngameTask(this.languageService, this.playerService, "startgame.4-s");
        this.schedulerService.scheduleDelayedTask(gameStartsIn4S, 20L * 16);

        BukkitRunnable gameStartsIn3S = new BroadCastIngameTask(this.languageService, this.playerService, "startgame.3-s");
        this.schedulerService.scheduleDelayedTask(gameStartsIn3S, 20L * 17);

        BukkitRunnable gameStartsIn2S = new BroadCastIngameTask(this.languageService, this.playerService, "startgame.2-s");
        this.schedulerService.scheduleDelayedTask(gameStartsIn2S, 20L * 18);

        BukkitRunnable gameStartsIn1S = new BroadCastIngameTask(this.languageService, this.playerService, "startgame.1-s");
        this.schedulerService.scheduleDelayedTask(gameStartsIn1S, 20L * 19);

        GameService gameService = this.gameService;
        BukkitRunnable startGameTask = new BukkitRunnable() {
            public void run() {
                gameService.setGameStarted(true);
                gameService.setGameStarting(false);
            }
        };
        this.schedulerService.scheduleDelayedTask(startGameTask, 20L * 20);
    }
}
