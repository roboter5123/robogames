package com.roboter5123.robogames.command;

import com.roboter5123.robogames.service.*;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
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
    private final ArenaService arenaService;
    private final LobbyService lobbyService;

    public LeaveGameCommand(Player player, LanguageService languageService, GameService gameService, PlayerService playerService, SpawnService spawnService, ArenaService arenaService, LobbyService lobbyService) {
        this.player = player;
        this.languageService = languageService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.arenaService = arenaService;
        this.lobbyService = lobbyService;
    }

    @Override
    public void run() {

        String arenaName = this.playerService.getArenaNameByPlayer(this.player);

        if (arenaName == null){
            player.sendMessage(this.languageService.getMessage("leave.not-joined"));
            return;
        }

        if (this.gameService.isGameStarting(arenaName)) {
            player.sendMessage(this.languageService.getMessage("leave.game-is-starting"));
            return;
        }

        if (!playerService.getInGamePlayers(arenaName).contains(player)) {
            player.sendMessage(this.languageService.getMessage("leave.not-joined"));
            return;
        }

        this.spawnService.removePlayerSpawnPoint(arenaName, player);
        this.playerService.removeIngamePlayer(arenaName, player);

        if (this.arenaService.getArena(arenaName).getLobbyName() != null) {
            Location lobby = this.lobbyService.getLobby(arenaName);
            player.teleport(lobby);
        }else {
            World world = player.getWorld();
            Location worldSpawn = world.getSpawnLocation();
            player.teleport(worldSpawn);
        }
        player.sendMessage(this.languageService.getMessage("leave.success"));
        new BroadCastIngameTask(this.playerService, this.languageService.getMessage("leave.broadcast"), arenaName).run();
    }

}
