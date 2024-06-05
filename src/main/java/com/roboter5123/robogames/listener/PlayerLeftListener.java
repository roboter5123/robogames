package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.LobbyRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import com.roboter5123.robogames.tasks.command.LeaveGameCommand;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeftListener implements Listener {

    private final LanguageRepository languageRepository;

    private final PlayerRepository playerRepository;
    private final SpawnRepository spawnRepository;
    private final GameRepository gameRepository;
    private final ArenaRepository arenaRepository;
    private final LobbyRepository lobbyRepository;

    public PlayerLeftListener(LanguageRepository languageRepository, PlayerRepository playerRepository, SpawnRepository spawnRepository, GameRepository gameRepository, ArenaRepository arenaRepository, LobbyRepository lobbyRepository) {
        this.languageRepository = languageRepository;
        this.playerRepository = playerRepository;
        this.spawnRepository = spawnRepository;
        this.gameRepository = gameRepository;
        this.arenaRepository = arenaRepository;
        this.lobbyRepository = lobbyRepository;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        new LeaveGameCommand(player, this.languageRepository, this.gameRepository, this.playerRepository, this.spawnRepository, this.arenaRepository, this.lobbyRepository).run();
    }
}
