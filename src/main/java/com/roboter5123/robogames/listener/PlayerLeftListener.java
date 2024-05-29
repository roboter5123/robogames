package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.command.LeaveGameCommand;
import com.roboter5123.robogames.service.*;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeftListener implements Listener {

    private final LanguageService languageService;

    private final PlayerService playerService;
    private final SpawnService spawnService;
    private final GameService gameService;
    private final ArenaService arenaService;
    private final LobbyService lobbyService;

    public PlayerLeftListener(LanguageService languageService, PlayerService playerService, SpawnService spawnService, GameService gameService, ArenaService arenaService, LobbyService lobbyService) {
        this.languageService = languageService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.gameService = gameService;
        this.arenaService = arenaService;
        this.lobbyService = lobbyService;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        new LeaveGameCommand(player, this.languageService, this.gameService, this.playerService, this.spawnService, this.arenaService, this.lobbyService).run();
    }
}
