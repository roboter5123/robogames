package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.ChestRepository;
import com.roboter5123.robogames.repository.ConfigRepository;
import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.ItemRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.repository.SchedulerRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import com.roboter5123.robogames.tasks.GameLoopTask;
import org.bukkit.scheduler.BukkitRunnable;

public class StartGameCommand extends BukkitRunnable {

    private final GameRepository gameRepository;
    private final SchedulerRepository schedulerRepository;
    private final LanguageRepository languageRepository;
    private final PlayerRepository playerRepository;
    private final SpawnRepository spawnRepository;
    private final String arenaName;
    private final ChestRepository chestRepository;
    private final ArenaRepository arenaRepository;
    private final ItemRepository itemRepository;
    private final ConfigRepository configRepository;

    public StartGameCommand(GameRepository gameRepository, SchedulerRepository schedulerRepository, LanguageRepository languageRepository, PlayerRepository playerRepository, SpawnRepository spawnRepository, ChestRepository chestRepository, ArenaRepository arenaRepository, ItemRepository itemRepository, String arenaName, ConfigRepository configRepository) {
        this.gameRepository = gameRepository;
        this.schedulerRepository = schedulerRepository;
        this.languageRepository = languageRepository;
        this.playerRepository = playerRepository;
        this.spawnRepository = spawnRepository;
        this.chestRepository = chestRepository;
        this.arenaRepository = arenaRepository;
        this.arenaName = arenaName;
        this.itemRepository = itemRepository;
        this.configRepository = configRepository;
    }

    @Override
    public void run() {

        if (this.gameRepository.isGameStarted(arenaName) || this.gameRepository.isGameStarting(arenaName)){
            return;
        }

        if (this.configRepository.getMinPlayers() > this.playerRepository.getAlivePlayers(this.arenaName).size()){
            return;
        }

        this.gameRepository.setGameStarting(this.arenaName, true);
        this.gameRepository.setTimerTicks(this.arenaName, 0L);
        new RefillChestsCommand(this.chestRepository, this.arenaRepository, this.itemRepository, this.arenaName).run();
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
                gameRepository.setGameStarted(arenaName, true);
                gameRepository.setGameStarting(arenaName, false);
            }
        };
        this.schedulerRepository.scheduleDelayedTask(startGameTask, 20L * 20);

        BukkitRunnable gameCheckTask = new GameLoopTask(gameRepository, playerRepository, spawnRepository, languageRepository, 20L, arenaName);
        this.schedulerRepository.scheduleRepeatingTask(gameCheckTask, 20L * 20 + 2, 20L);
    }

    private void broadcastCountdown(String messageKey, long ticksUntil) {
        String message = languageRepository.getMessage(messageKey);
        BukkitRunnable gameStartsInTask = new BroadCastIngameTask(this.playerRepository, message, this.arenaName);
        this.schedulerRepository.scheduleDelayedTask(gameStartsInTask, ticksUntil);
    }
}
