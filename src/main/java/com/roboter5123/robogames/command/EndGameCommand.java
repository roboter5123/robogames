package com.roboter5123.robogames.command;

import java.util.List;

import javax.xml.stream.Location;

import com.roboter5123.robogames.model.Coordinate;
import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.PlayerService;
import com.roboter5123.robogames.service.SpawnService;

import org.bukkit.scheduler.BukkitRunnable;

public class EndGameCommand extends BukkitRunnable {

	private final GameService gameService;
	private final SpawnService spawnService;
	private final PlayerService playerService;

	public EndGameCommand(GameService gameService, SpawnService spawnService, PlayerService playerService) {
		this.gameService = gameService;
		this.spawnService = spawnService;
		this.playerService = playerService;
	}

	@Override
	public void run() {
		this.gameService.setGameStarted(false);
		this.gameService.setGameStarting(false);
		this.spawnService.clearPlayerSpawns();
		this.playerService.clearInGamePlayers();

		List<Player> inGamePlayers = this.playerService.getInGamePlayers();
		for (Player inGamePlayer : inGamePlayers) {
			inGamePlayer.setGameMode(GameMode.ADVENTURE);
			World world = inGamePlayer.getWorld;
			this.playerService.teleportPlayer(inGamePlayer, world, convertToCoordinate(world.getSpawnLocation()));
		}
	}

	private Coordinate convertToCoordinate(Location location) {
		// TODO Implement
	}
}
