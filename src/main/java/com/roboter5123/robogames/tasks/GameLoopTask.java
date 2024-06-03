package com.roboter5123.robogames.tasks;

import com.roboter5123.robogames.tasks.command.EndGameCommand;
import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.PlayerService;
import com.roboter5123.robogames.service.SpawnService;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameLoopTask extends BukkitRunnable {

    private final GameService gameService;
    private final PlayerService playerService;
    private final LanguageService languageService;
    private final SpawnService spawnService;
    private final Long intervalTicks;
    private final String arenaName;

    public GameLoopTask(GameService gameService, PlayerService playerService, SpawnService spawnService, LanguageService languageService, Long tickInterval, String arenaName) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.intervalTicks = tickInterval;
        this.languageService = languageService;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {
        if (!this.gameService.isGameStarted(arenaName)) {
            cancel();
            return;
        }

        if (this.playerService.getAlivePlayers(arenaName).size() == 1) {
            endGameWithWinner();
            return;
        }

        if (this.playerService.getAlivePlayers(arenaName).isEmpty()) {
            endGameWithoutWinner();
            return;
        }

        long timerTicks = this.gameService.getTimer(arenaName) + this.intervalTicks;
        this.gameService.setTimerTicks(arenaName,timerTicks);
    }

    private void endGameWithWinner() {
        Player winner = this.playerService.getAlivePlayers(arenaName).get(0);
        String message = winner.getName() + this.languageService.getMessage("game.winner-text");
        new BroadCastIngameTask(this.playerService, message, this.arenaName).run();
        new EndGameCommand(this.gameService, this.spawnService, this.playerService, this.arenaName).run();
        cancel();
    }

    private void endGameWithoutWinner() {
        new EndGameCommand(this.gameService, this.spawnService, this.playerService, this.arenaName).run();
        cancel();
    }
}
