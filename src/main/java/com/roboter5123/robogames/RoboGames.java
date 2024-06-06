package com.roboter5123.robogames;

import com.roboter5123.robogames.handler.ArenaCommandHandler;
import com.roboter5123.robogames.handler.RoboGamesCommandHandler;
import com.roboter5123.robogames.listener.*;
import com.roboter5123.robogames.repository.*;
import com.roboter5123.robogames.service.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class RoboGames extends JavaPlugin {

    private final ArenaRepository arenaRepository;
    private final ConfigRepository configRepository;
    private final PlayerRepository playerRepository;
    private final LanguageRepository languageRepository;
    private final GameRepository gameRepository;
    private final SpawnRepository spawnRepository;
    private final LobbyRepository lobbyRepository;
    private final ChestRepository chestRepository;
    private final ItemRepository itemRepository;
    private final ArenaService arenaService;
    private final GameService gameService;
    private final WandService wandService;

    public RoboGames() {
        super();
        this.configRepository = new ConfigRepositoryImpl(this);
        this.lobbyRepository = new LobbyRepositoryImpl(this, this.configRepository);
        this.arenaRepository = new ArenaRepositoryImpl(this, this.configRepository);
        this.spawnRepository = new SpawnRepositoryImpl(this, this.configRepository);
        this.chestRepository = new ChestRepositoryImpl(this, this.configRepository);
        this.itemRepository = new ItemRepositoryImpl(this, configRepository);
        this.gameRepository = new GameRepositoryImpl();
        this.playerRepository = new PlayerRepositoryImpl(this);
        SchedulerRepository schedulerRepository = new SchedulerRepositoryImpl(this);
        this.languageRepository = new LanguageRepositoryImpl(this);
        this.arenaService = new ArenaServiceImpl(this.languageRepository, this.arenaRepository, this.configRepository, this.spawnRepository, this.chestRepository, this.itemRepository);
        this.gameService = new GameServiceImpl(this.languageRepository, this.gameRepository, this.playerRepository, this.spawnRepository, this.lobbyRepository, schedulerRepository, this.configRepository, this.arenaService);
        this.wandService = new WandServiceImpl(this.languageRepository, this.spawnRepository, this.gameRepository);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfigs();
        registerListeners();
    }

    private void loadConfigs() {
        this.languageRepository.saveLanguageFiles(getFile());
        this.languageRepository.updateLanguageKeys();
        this.configRepository.checkConfigKeys();
        this.spawnRepository.loadSpawnConfig();
        this.arenaRepository.loadArenaConfig();
        this.lobbyRepository.loadLobbiesConfig();
        this.chestRepository.loadChestConfig();
        this.itemRepository.loadItemsConfig();
    }

    private void registerListeners() {
        Objects.requireNonNull(getCommand("robogames")).setExecutor(new RoboGamesCommandHandler(this.languageRepository, this.gameRepository, this.gameService));
        Objects.requireNonNull(getCommand("arena")).setExecutor(new ArenaCommandHandler(this.languageRepository, this.arenaRepository, this.arenaService, this.wandService));
        getServer().getPluginManager().registerEvents(new SelectPosListener(this.languageRepository, this.playerRepository), this);
        getServer().getPluginManager().registerEvents(new SetSpawnListener(this.languageRepository, this.spawnRepository, this.gameRepository, this.arenaService), this);
        getServer().getPluginManager().registerEvents(new MoveDisableListener(this.playerRepository, this.gameRepository, this.arenaRepository), this);
        getServer().getPluginManager().registerEvents(new PlayerKilledListener(this.languageRepository, this.playerRepository), this);
        getServer().getPluginManager().registerEvents(new PlayerLeftListener(this.gameService), this);
    }

    @Override
    public void onDisable() {
        Set<String> arenaNames = this.arenaRepository.getArenaNames();
        for (String arenaName : arenaNames) {
            List<Player> inGamePlayers = this.playerRepository.getInGamePlayers(arenaName);
            teleportAllPlayersToLobby(arenaName, inGamePlayers);
        }
    }

    private void teleportAllPlayersToLobby(String arenaName, List<Player> inGamePlayers) {
        for (Player inGamePlayer : inGamePlayers) {
            if (this.arenaRepository.getArena(arenaName).getLobbyName() != null) {
                Location lobby = this.lobbyRepository.getLobby(arenaName);
                inGamePlayer.teleport(lobby);
                continue;
            }
            World world = inGamePlayer.getWorld();
            Location worldSpawn = world.getSpawnLocation();
            inGamePlayer.teleport(worldSpawn);
        }
    }
}
