package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.LobbyRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaveGameCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageRepository languageRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final SpawnRepository spawnRepository;
    private final ArenaRepository arenaRepository;
    private final LobbyRepository lobbyRepository;

    public LeaveGameCommand(Player player, LanguageRepository languageRepository, GameRepository gameRepository, PlayerRepository playerRepository, SpawnRepository spawnRepository, ArenaRepository arenaRepository, LobbyRepository lobbyRepository) {
        this.player = player;
        this.languageRepository = languageRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.spawnRepository = spawnRepository;
        this.arenaRepository = arenaRepository;
        this.lobbyRepository = lobbyRepository;
    }

    @Override
    public void run() {

        String arenaName = this.playerRepository.getArenaNameByPlayer(this.player);

        if (arenaName == null){
            player.sendMessage(this.languageRepository.getMessage("leave.not-joined"));
            return;
        }

        if (this.gameRepository.isGameStarting(arenaName)) {
            player.sendMessage(this.languageRepository.getMessage("leave.game-is-starting"));
            return;
        }

        if (!playerRepository.getInGamePlayers(arenaName).contains(player)) {
            player.sendMessage(this.languageRepository.getMessage("leave.not-joined"));
            return;
        }

        this.spawnRepository.removePlayerSpawnPoint(arenaName, player);
        this.playerRepository.removeIngamePlayer(arenaName, player);

        if (this.arenaRepository.getArena(arenaName).getLobbyName() != null) {
            Location lobby = this.lobbyRepository.getLobby(arenaName);
            player.teleport(lobby);
        }else {
            World world = player.getWorld();
            Location worldSpawn = world.getSpawnLocation();
            player.teleport(worldSpawn);
        }
        player.sendMessage(this.languageRepository.getMessage("leave.success"));
        new BroadCastIngameTask(this.playerRepository, this.languageRepository.getMessage("leave.broadcast"), arenaName).run();
    }

}
