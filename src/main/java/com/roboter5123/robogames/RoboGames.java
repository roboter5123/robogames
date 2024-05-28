package com.roboter5123.robogames;

import com.roboter5123.robogames.handler.CommandHandler;
import com.roboter5123.robogames.listener.MoveDisableListener;
import com.roboter5123.robogames.listener.SelectPosListener;
import com.roboter5123.robogames.listener.SetSpawnListener;
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

    public RoboGames() {
        super();
        this.arenaService = new ArenaService(this);
        this.configService = new ConfigService(this);
        this.gameService = new GameService();
        this.languageService = new LanguageService(this);
        this.playerService = new PlayerService(this);
        this.schedulerService = new SchedulerService(this);
        this.spawnService = new SpawnService(this);
        this.metadataService = new MetadataService(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.languageService.saveLanguageFiles(getFile());
        this.languageService.updateLanguageKeys();
        this.configService.checkConfigKeys();
        this.spawnService.loadSpawnConfig();
        this.arenaService.loadArenaConfig();
        registerListeners();
    }

    private void registerListeners() {
        Objects.requireNonNull(getCommand("robogames")).setExecutor(new CommandHandler(this.languageService, this.playerService, this.spawnService, this.arenaService, this.gameService, this.schedulerService));
        getServer().getPluginManager().registerEvents(new SelectPosListener(this.languageService, this.metadataService), this);
        getServer().getPluginManager().registerEvents(new SetSpawnListener(this.languageService, this.spawnService, this.configService, this.gameService, this.arenaService), this);
        getServer().getPluginManager().registerEvents(new MoveDisableListener(this.playerService, this.gameService, this.arenaService), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
