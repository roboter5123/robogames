package com.roboter5123.robogames.tasks;

import com.roboter5123.robogames.tasks.command.EndGameCommand;
import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameLoopTask extends BukkitRunnable {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final LanguageRepository languageRepository;
    private final SpawnRepository spawnRepository;
    private final Long intervalTicks;
    private final String arenaName;

    public GameLoopTask(GameRepository gameRepository, PlayerRepository playerRepository, SpawnRepository spawnRepository, LanguageRepository languageRepository, Long tickInterval, String arenaName) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.spawnRepository = spawnRepository;
        this.intervalTicks = tickInterval;
        this.languageRepository = languageRepository;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {
        if (!this.gameRepository.isGameStarted(arenaName)) {
            cancel();
            return;
        }

        if (this.playerRepository.getAlivePlayers(arenaName).size() == 1) {
            endGameWithWinner();
            return;
        }

        if (this.playerRepository.getAlivePlayers(arenaName).isEmpty()) {
            endGameWithoutWinner();
            return;
        }

        long timerTicks = this.gameRepository.getTimer(arenaName) + this.intervalTicks;
        this.gameRepository.setTimerTicks(arenaName,timerTicks);
    }

    private void endGameWithWinner() {
        Player winner = this.playerRepository.getAlivePlayers(arenaName).get(0);
        String message = winner.getName() + this.languageRepository.getMessage("game.winner-text");
        new BroadCastIngameTask(this.playerRepository, message, this.arenaName).run();
        new EndGameCommand(this.gameRepository, this.spawnRepository, this.playerRepository, this.arenaName).run();
        cancel();
    }

    private void endGameWithoutWinner() {
        new EndGameCommand(this.gameRepository, this.spawnRepository, this.playerRepository, this.arenaName).run();
        cancel();
    }
}
