package com.roboter5123.robogames.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.ChestRepository;
import com.roboter5123.robogames.repository.ConfigRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import com.roboter5123.robogames.service.model.Arena;
import org.bukkit.ChatColor;

public class ArenaServiceImpl implements ArenaService {

	private final LanguageRepository languageRepository;
	private final ArenaRepository arenaRepository;
	private final ConfigRepository configRepository;
	private final SpawnRepository spawnRepository;
	private final ChestRepository chestRepository;

	public ArenaServiceImpl(LanguageRepository languageRepository, ArenaRepository arenaRepository, ConfigRepository configRepository, SpawnRepository spawnRepository,
		ChestRepository chestRepository) {
		this.languageRepository = languageRepository;
		this.arenaRepository = arenaRepository;
		this.configRepository = configRepository;
		this.spawnRepository = spawnRepository;
		this.chestRepository = chestRepository;
	}

	@Override
	public void addSpawn(Player player, Location newSpawnPoint) {

		Optional<Arena> arenaOptional = this.arenaRepository.getArenaNames().stream().map(this.arenaRepository::getArena).filter(arena -> newSpawnPoint.getWorld()
			.getName().equals(arena.getWorldName())).findFirst();
		if (arenaOptional.isEmpty()) {
			return;
		}
		Arena arena = arenaOptional.get();
		List<Location> allSpawns = this.spawnRepository.getAllSpawns(arena.getName());

		if (allSpawns.contains(newSpawnPoint)) {
			player.sendMessage(this.languageRepository.getMessage("setspawnhandler.duplicate"));
			return;
		}

		if (!this.arenaRepository.isInArenaBounds(arena.getName(), newSpawnPoint)) {
			player.sendMessage(this.languageRepository.getMessage("setspawnhandler.not-in-arena"));
			return;
		}

		if (allSpawns.size() == this.configRepository.getMaxPlayers()) {
			player.sendMessage(ChatColor.RED + this.languageRepository.getMessage("setspawnhandler.max-spawn"));
			return;
		}

		try {
			this.spawnRepository.createSpawn(arena.getName(), newSpawnPoint);
		} catch (IOException e) {
			player.sendMessage(languageRepository.getMessage("arena.save-error"));
		}
		player.sendMessage(
			this.languageRepository.getMessage("setspawnhandler.position-set") + allSpawns.size() + this.languageRepository.getMessage("setspawnhandler.set-at") +
				newSpawnPoint.getBlockX() + this.languageRepository.getMessage("setspawnhandler.coord-y") + newSpawnPoint.getBlockY() +
				this.languageRepository.getMessage("setspawnhandler.coord-z") + newSpawnPoint.getBlockZ());

	}

	@Override
	public void createArena(Player player, String arenaName) {

		if (!player.hasMetadata("arena_pos1") || !player.hasMetadata("arena_pos2")) {
			player.sendMessage(this.languageRepository.getMessage("arena.no-values"));
			return;
		}

		Location pos1 = (Location) player.getMetadata("arena_pos1").get(0).value();
		Location pos2 = (Location) player.getMetadata("arena_pos2").get(0).value();
		if (pos1 == null || pos2 == null) {
			player.sendMessage(this.languageRepository.getMessage("arena.invalid-values"));
			return;
		}

		if (this.arenaRepository.getArenaNames().contains(arenaName)){
			player.sendMessage(this.languageRepository.getMessage("arena.arena-exists"));
			return;
		}


		Arena arena = new Arena();
		arena.setName(arenaName);
		arena.setWorldName(player.getWorld().getName());
		arena.setPos1(pos1);
		arena.setPos2(pos2);
		try {
			this.arenaRepository.createArena(arena);
		} catch (IOException e) {
			player.sendMessage(this.languageRepository.getMessage("arena.creation-failed"));
		}
		player.sendMessage(this.languageRepository.getMessage("arena.region-created"));

	}

	@Override
	public void scanArena(Player player, String arenaName) {
		Arena arena = this.arenaRepository.getArena(arenaName);
		if (arena == null) {
			player.sendMessage(this.languageRepository.getMessage("arena-doesnt-exists"));
			return;
		}

		ScanArenaCommand.LowHighCoordinates lowHighCoordinates = getLowHighCoordinates(arena);
		World world = this.arenaRepository.getWorld(arena.getWorldName());
		try {
			this.chestRepository.removeAllChests(arenaName);
		} catch (Exception e) {
			player.sendMessage(Arrays.toString(e.getStackTrace()));
			player.sendMessage(this.languageRepository.getMessage("scanarena.failed-locations"));
			return;
		}

		for (int x = lowHighCoordinates.lowX(); x < lowHighCoordinates.highX(); x++) {
			for (int y = lowHighCoordinates.lowY(); y < lowHighCoordinates.highY(); y++) {
				for (int z = lowHighCoordinates.lowZ(); z < lowHighCoordinates.highZ(); z++) {
					checkBlock(world, x, y, z);
				}
			}
		}
		player.sendMessage(languageRepository.getMessage("scanarena.saved-locations"));
	}

	private void checkBlock(World world, int x, int y, int z) {
		Block block = world.getBlockAt(x, y, z);
		if (!(block.getState() instanceof Chest chest)) {
			return;
		}
		chest.getBlockInventory().clear();
		try {
			this.chestRepository.addChest(arenaName, chest);
		} catch (IOException e) {
			player.sendMessage(this.languageRepository.getMessage("scanarena.failed-locations"));
		}
	}

	private static ArenaServiceImpl.LowHighCoordinates getLowHighCoordinates(Arena arena) {
		Location pos1 = arena.getPos1();
		Location pos2 = arena.getPos2();

		int lowX;
		int highX;
		if (pos1.getX() > pos2.getX()) {
			highX = (int) pos1.getX();
			lowX = (int) pos2.getX();
		} else {
			highX = (int) pos2.getX();
			lowX = (int) pos1.getX();
		}

		int lowY;
		int highY;
		if (pos1.getY() > pos2.getY()) {
			highY = (int) pos1.getY();
			lowY = (int) pos2.getY();
		} else {
			highY = (int) pos2.getY();
			lowY = (int) pos1.getY();
		}

		int lowZ;
		int highZ;
		if (pos1.getZ() > pos2.getZ()) {
			highZ = (int) pos1.getZ();
			lowZ = (int) pos2.getZ();
		} else {
			highZ = (int) pos2.getZ();
			lowZ = (int) pos1.getZ();
		}
		return new ArenaServiceImpl.LowHighCoordinates(lowX, highX, lowY, highY, lowZ, highZ);
	}

	private record LowHighCoordinates(int lowX, int highX, int lowY, int highY, int lowZ, int highZ) {

	}

}
