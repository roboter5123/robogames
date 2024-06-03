package com.roboter5123.robogames;

import com.roboter5123.robogames.handler.ArenaCommandHandler;
import com.roboter5123.robogames.handler.RoboGamesCommandHandler;
import com.roboter5123.robogames.listener.*;
import com.roboter5123.robogames.service.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class RoboGames extends JavaPlugin {

    private final SchedulerService schedulerService;
    private final ArenaService arenaService;
    private final ConfigService configService;
    private final PlayerService playerService;
    private final LanguageService languageService;
    private final GameService gameService;
    private final SpawnService spawnService;
    private final LobbyService lobbyService;
    private final ChestService chestService;
    private final ItemService itemService;

    public RoboGames() {
        super();
        this.configService = new ConfigServiceImpl(this);
        this.lobbyService = new LobbyServiceImpl(this, this.configService);
        this.arenaService = new ArenaServiceImpl(this, this.configService);
        this.spawnService = new SpawnServiceImpl(this, this.configService);
        this.chestService = new ChestServiceImpl(this, this.configService);
        this.gameService = new GameServiceImpl();
        this.languageService = new LanguageServiceImpl(this);
        this.playerService = new PlayerServiceImpl(this);
        this.schedulerService = new SchedulerServiceImpl(this);
        this.itemService = new ItemServiceImpl(this, configService);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfigs();
        registerListeners();
    }

    private void loadConfigs() {
        this.languageService.saveLanguageFiles(getFile());
        this.languageService.updateLanguageKeys();
        this.configService.checkConfigKeys();
        this.spawnService.loadSpawnConfig();
        this.arenaService.loadArenaConfig();
        this.lobbyService.loadLobbiesConfig();
        this.chestService.loadChestConfig();
        this.itemService.loadItemsConfig();
    }

    private void registerListeners() {
        Objects.requireNonNull(getCommand("robogames")).setExecutor(new RoboGamesCommandHandler(this.languageService, this.arenaService, this.spawnService, this.gameService, this.playerService, this.schedulerService, this.lobbyService, this.chestService, this.itemService, this.configService));
        Objects.requireNonNull(getCommand("arena")).setExecutor(new ArenaCommandHandler(this.languageService, this.arenaService, this.spawnService, this.gameService, this.chestService));
        getServer().getPluginManager().registerEvents(new SelectPosListener(this.languageService, this.playerService), this);
        getServer().getPluginManager().registerEvents(new SetSpawnListener(this.languageService, this.spawnService, this.configService, this.gameService, this.arenaService), this);
        getServer().getPluginManager().registerEvents(new MoveDisableListener(this.playerService, this.gameService, this.arenaService), this);
        getServer().getPluginManager().registerEvents(new PlayerKilledListener(this.languageService, this.playerService), this);
        getServer().getPluginManager().registerEvents(new PlayerLeftListener(this.languageService, this.playerService, this.spawnService, this.gameService, this.arenaService, this.lobbyService), this);
    }

    @Override
    public void onDisable() {
        Set<String> arenaNames = this.arenaService.getArenaNames();
        for (String arenaName : arenaNames) {
            List<Player> inGamePlayers = this.playerService.getInGamePlayers(arenaName);
            teleportAllPlayersToLobby(arenaName, inGamePlayers);
        }
    }

    private void teleportAllPlayersToLobby(String arenaName, List<Player> inGamePlayers) {
        for (Player inGamePlayer : inGamePlayers) {
            if (this.arenaService.getArena(arenaName).getLobbyName() != null) {
                Location lobby = this.lobbyService.getLobby(arenaName);
                inGamePlayer.teleport(lobby);
                continue;
            }
            World world = inGamePlayer.getWorld();
            Location worldSpawn = world.getSpawnLocation();
            inGamePlayer.teleport(worldSpawn);
        }
    }
}
