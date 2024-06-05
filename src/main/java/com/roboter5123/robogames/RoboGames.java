package com.roboter5123.robogames;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.roboter5123.robogames.handler.ArenaCommandHandler;
import com.roboter5123.robogames.handler.RoboGamesCommandHandler;
import com.roboter5123.robogames.listener.MoveDisableListener;
import com.roboter5123.robogames.listener.PlayerKilledListener;
import com.roboter5123.robogames.listener.PlayerLeftListener;
import com.roboter5123.robogames.listener.SelectPosListener;
import com.roboter5123.robogames.listener.SetSpawnListener;
import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.ArenaRepositoryImpl;
import com.roboter5123.robogames.repository.ChestRepository;
import com.roboter5123.robogames.repository.ChestRepositoryImpl;
import com.roboter5123.robogames.service.ArenaService;
import com.roboter5123.robogames.service.ArenaServiceImpl;
import com.roboter5123.robogames.repository.ConfigRepository;
import com.roboter5123.robogames.repository.ConfigRepositoryImpl;
import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.GameRepositoryImpl;
import com.roboter5123.robogames.repository.ItemRepository;
import com.roboter5123.robogames.repository.ItemRepositoryImpl;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.LanguageRepositoryImpl;
import com.roboter5123.robogames.repository.LobbyRepository;
import com.roboter5123.robogames.repository.LobbyRepositoryImpl;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.repository.PlayerRepositoryImpl;
import com.roboter5123.robogames.repository.SchedulerRepository;
import com.roboter5123.robogames.repository.SchedulerRepositoryImpl;
import com.roboter5123.robogames.repository.SpawnRepository;
import com.roboter5123.robogames.repository.SpawnRepositoryImpl;

public final class RoboGames extends JavaPlugin {

	private final SchedulerRepository schedulerRepository;
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

	public RoboGames() {
		super();
		this.configRepository = new ConfigRepositoryImpl(this);
		this.lobbyRepository = new LobbyRepositoryImpl(this, this.configRepository);
		this.arenaRepository = new ArenaRepositoryImpl(this, this.configRepository);
		this.spawnRepository = new SpawnRepositoryImpl(this, this.configRepository);
		this.chestRepository = new ChestRepositoryImpl(this, this.configRepository);
		this.gameRepository = new GameRepositoryImpl();
		this.languageRepository = new LanguageRepositoryImpl(this);
		this.playerRepository = new PlayerRepositoryImpl(this);
		this.schedulerRepository = new SchedulerRepositoryImpl(this);
		this.itemRepository = new ItemRepositoryImpl(this, configRepository);
		this.arenaService = new ArenaServiceImpl(this.languageRepository, this.arenaRepository, this.configRepository, this.spawnRepository);
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
		Objects.requireNonNull(getCommand("robogames"))
			.setExecutor(new RoboGamesCommandHandler(this.languageRepository, this.arenaRepository, this.spawnRepository, this.gameRepository, this.playerRepository,
				this.schedulerRepository, this.lobbyRepository, this.chestRepository, this.itemRepository, this.configRepository));
		Objects.requireNonNull(getCommand("arena"))
			.setExecutor(new ArenaCommandHandler(this.languageRepository, this.arenaRepository, this.spawnRepository, this.gameRepository, this.chestRepository, this.arenaService));
		getServer().getPluginManager().registerEvents(new SelectPosListener(this.languageRepository, this.playerRepository), this);
		getServer().getPluginManager().registerEvents(new SetSpawnListener(this.languageRepository, this.spawnRepository, this.gameRepository, this.arenaService), this);
		getServer().getPluginManager().registerEvents(new MoveDisableListener(this.playerRepository, this.gameRepository, this.arenaRepository), this);
		getServer().getPluginManager().registerEvents(new PlayerKilledListener(this.languageRepository, this.playerRepository), this);
		getServer().getPluginManager()
			.registerEvents(new PlayerLeftListener(this.languageRepository, this.playerRepository, this.spawnRepository, this.gameRepository, this.arenaRepository,
				this.lobbyRepository), this);
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
