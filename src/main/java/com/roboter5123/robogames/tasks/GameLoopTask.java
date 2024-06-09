package com.roboter5123.robogames.tasks;

import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.service.GameService;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameLoopTask extends BukkitRunnable {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final LanguageRepository languageRepository;
    private final Long intervalTicks;
    private final String arenaName;
    private final GameService gameService;

    public GameLoopTask(GameRepository gameRepository, PlayerRepository playerRepository, LanguageRepository languageRepository, Long tickInterval, String arenaName, GameService gameService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.intervalTicks = tickInterval;
        this.languageRepository = languageRepository;
        this.arenaName = arenaName;
        this.gameService = gameService;
    }

    @Override
    public void run() {
        if (!this.gameRepository.isGameStarted(arenaName)) {
            cancel();
            return;
        }

        if (this.playerRepository.getAlivePlayersByArenaName(arenaName).size() == 1) {
            endGameWithWinner();
            return;
        }

        if (this.playerRepository.getAlivePlayersByArenaName(arenaName).isEmpty()) {
            endGameWithoutWinner();
            return;
        }

        long timerTicks = this.gameRepository.getTimer(arenaName) + this.intervalTicks;
        this.gameRepository.setTimerTicks(arenaName,timerTicks);
    }

    private void endGameWithWinner() {
        Player winner = this.playerRepository.getAlivePlayersByArenaName(arenaName).get(0);
        String message = winner.getName() + this.languageRepository.getMessage("game.winner-text");
        this.playerRepository.getInGamePlayersByArenaName(arenaName).forEach(player -> player.sendMessage(message));
        this.gameService.endGame(arenaName);
        cancel();
    }

    private void endGameWithoutWinner() {
        this.gameService.endGame(arenaName);
        cancel();
    }
}
