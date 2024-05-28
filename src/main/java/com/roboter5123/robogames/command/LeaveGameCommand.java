package com.roboter5123.robogames.command;

import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.PlayerService;
import com.roboter5123.robogames.service.SpawnService;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaveGameCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageService languageService;

    private final GameService gameService;
    private final PlayerService playerService;
    private final SpawnService spawnService;

    public LeaveGameCommand(Player player, LanguageService languageService, GameService gameService, PlayerService playerService, SpawnService spawnService) {
        this.player = player;
        this.languageService = languageService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.spawnService = spawnService;
    }

    @Override
    public void run() {
        if (this.gameService.isGameStarting()) {
            player.sendMessage(languageService.getMessage("leave.game-is-starting"));
            return;
        }

        if (!playerService.getInGamePlayers().contains(player)) {
            player.sendMessage(languageService.getMessage("leave.not-joined"));
            return;
        }
        this.spawnService.removePlayerSpawnPoint(player);
        this.playerService.removeIngamePlayer(player);

        World world = player.getWorld();
        Location worldSpawn = world.getSpawnLocation();
        this.playerService.teleportPlayer(player, world, worldSpawn);
        player.sendMessage(this.languageService.getMessage("leave.success"));
    }

}
