package com.roboter5123.robogames.tasks;

import com.roboter5123.robogames.command.EndGameCommand;
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

    public GameLoopTask(GameService gameService, PlayerService playerService, SpawnService spawnService, LanguageService languageService, Long tickInterval) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.intervalTicks = tickInterval;
        this.languageService = languageService;
    }

    @Override
    public void run() {
        if (!this.gameService.isGameStarted()) {
            cancel();
            return;
        }

        if (this.playerService.getAlivePlayers().size() == 1) {
            endGameWithWinner();
            return;
        }

        if (this.playerService.getAlivePlayers().isEmpty()) {
            endGameWithoutWinner();
            return;
        }

        long timerTicks = this.gameService.getTimer() + this.intervalTicks;
        this.gameService.setTimerTicks(timerTicks);
    }

    private void endGameWithWinner() {
        Player winner = this.playerService.getAlivePlayers().get(0);
        String message = winner.getDisplayName() + this.languageService.getMessage("game.winner-text");
        new BroadCastIngameTask(this.playerService, message).run();
        new EndGameCommand(this.gameService, this.spawnService, this.playerService).run();
        cancel();
    }

    private void endGameWithoutWinner() {
        new EndGameCommand(this.gameService, this.spawnService, this.playerService).run();
        cancel();
    }
}
