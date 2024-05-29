package com.roboter5123.robogames;

import com.roboter5123.robogames.handler.ArenaCommandHandler;
import com.roboter5123.robogames.handler.RoboGamesCommandHandler;
import com.roboter5123.robogames.listener.*;
import com.roboter5123.robogames.service.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RoboGames extends JavaPlugin {

    private final SchedulerService schedulerService;
    private final ArenaService arenaService;
    private final ConfigService configService;
    private final PlayerService playerService;
    private final LanguageService languageService;
    private final GameService gameService;
    private final SpawnService spawnService;
    private final MetadataService metadataService;
    private final LobbyService lobbyService;

    public RoboGames() {
        super();
        this.lobbyService = new LobbyServiceImpl(this);
        this.arenaService = new ArenaServiceImpl(this);
        this.configService = new ConfigServiceImpl(this);
        this.gameService = new GameServiceImpl();
        this.languageService = new LanguageServiceImpl(this);
        this.playerService = new PlayerServiceImpl(this);
        this.schedulerService = new SchedulerServiceImpl(this);
        this.spawnService = new SpawnServiceImpl(this);
        this.metadataService = new MetadataServiceImpl(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.languageService.saveLanguageFiles(getFile());
        this.languageService.updateLanguageKeys();
        this.configService.checkConfigKeys();
        this.spawnService.loadSpawnConfig();
        this.arenaService.loadArenaConfig();
        this.lobbyService.loadLobbiesConfig();
        registerListeners();
    }

    private void registerListeners() {
        Objects.requireNonNull(getCommand("robogames")).setExecutor(new RoboGamesCommandHandler(this.languageService,this.arenaService, this.spawnService, this.gameService, this.playerService,this.schedulerService, this.lobbyService));
        Objects.requireNonNull(getCommand("arena")).setExecutor(new ArenaCommandHandler(this.languageService,this.arenaService, this.spawnService, this.gameService));
        getServer().getPluginManager().registerEvents(new SelectPosListener(this.languageService, this.metadataService), this);
        getServer().getPluginManager().registerEvents(new SetSpawnListener(this.languageService, this.spawnService, this.configService, this.gameService, this.arenaService), this);
        getServer().getPluginManager().registerEvents(new MoveDisableListener(this.playerService, this.gameService, this.arenaService), this);
        getServer().getPluginManager().registerEvents(new PlayerKilledListener(this.languageService, this.playerService), this);
        getServer().getPluginManager().registerEvents(new PlayerLeftListener(this.languageService, this.playerService, this.spawnService, this.gameService, this.arenaService, this.lobbyService), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
