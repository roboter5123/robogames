package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.repository.SpawnRepository;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class EndGameCommand extends BukkitRunnable {

    private final GameRepository gameRepository;
    private final SpawnRepository spawnRepository;
    private final PlayerRepository playerRepository;
    private final String arenaName;

    public EndGameCommand(GameRepository gameRepository, SpawnRepository spawnRepository, PlayerRepository playerRepository, String arenaName) {
        this.gameRepository = gameRepository;
        this.spawnRepository = spawnRepository;
        this.playerRepository = playerRepository;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {
        this.gameRepository.setGameStarted(this.arenaName, false);
        this.gameRepository.setGameStarting(this.arenaName,false);
        this.spawnRepository.clearPlayerSpawns(this.arenaName);

        List<Player> inGamePlayers = this.playerRepository.getInGamePlayers(this.arenaName);
        for (Player inGamePlayer : inGamePlayers) {
            inGamePlayer.setGameMode(GameMode.ADVENTURE);
            inGamePlayer.getInventory().clear();
            World world = inGamePlayer.getWorld();
            inGamePlayer.teleport(world.getSpawnLocation());
        }
        this.playerRepository.clearInGamePlayers(this.arenaName);
    }

}
