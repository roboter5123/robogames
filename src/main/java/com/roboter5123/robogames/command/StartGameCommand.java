package com.roboter5123.robogames.command;

import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.PlayerService;
import com.roboter5123.robogames.service.SchedulerService;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import com.roboter5123.robogames.tasks.GameLoopTask;

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
		this.gameService.setTimerTicks(0L);

		String message20S = languageService.getMessage("startgame.20-s");
		BukkitRunnable gameStartsIn20S = new BroadCastIngameTask(this.playerService, message20S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn20S, 0L);

		String message15S = languageService.getMessage("startgame.15-s");
		BukkitRunnable gameStartsIn15S = new BroadCastIngameTask(this.playerService, message15S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn15S, 20L * 5);

		String message10S = languageService.getMessage("startgame.10-s");
		BukkitRunnable gameStartsIn10S = new BroadCastIngameTask(this.playerService, message10S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn10S, 20L * 10);

		String message5S = languageService.getMessage("startgame.5-s");
		BukkitRunnable gameStartsIn5S = new BroadCastIngameTask(this.playerService, message5S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn5S, 20L * 15);

		String message4S = languageService.getMessage("startgame.4-s");
		BukkitRunnable gameStartsIn4S = new BroadCastIngameTask(this.playerService, message4S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn4S, 20L * 16);

		String message3S = languageService.getMessage("startgame.3-s");
		BukkitRunnable gameStartsIn3S = new BroadCastIngameTask(this.playerService, message3S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn3S, 20L * 17);

		String message2S = languageService.getMessage("startgame.2-s");
		BukkitRunnable gameStartsIn2S = new BroadCastIngameTask(this.playerService, message2S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn2S, 20L * 18);

		String message1S = languageService.getMessage("startgame.1-s");
		BukkitRunnable gameStartsIn1S = new BroadCastIngameTask(this.playerService, message1S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn1S, 20L * 19);

		String message0S = languageService.getMessage("game.game-start");
		BukkitRunnable gameStartsIn0S = new BroadCastIngameTask(this.playerService, message0S);
		this.schedulerService.scheduleDelayedTask(gameStartsIn0S, 20L * 20);

		GameService gameService = this.gameService;
		BukkitRunnable startGameTask = new BukkitRunnable() {
			public void run() {
				gameService.setGameStarted(true);
				gameService.setGameStarting(false);
			}
		};
		this.schedulerService.scheduleDelayedTask(startGameTask, 20L * 20);

		BukkitRunnable gameCheckTask = new GameLoopTask(gameService, playerService, schedulerService, spawnService, languageService, 20L);
		this.schedulerService.scheduleRepeatingTask(gameCheckTask, 0L, 20L);
	}
}
