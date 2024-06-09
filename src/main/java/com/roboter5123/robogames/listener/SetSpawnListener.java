package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import com.roboter5123.robogames.service.*;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SetSpawnListener implements Listener {

	private final LanguageRepository languageRepository;
	private final SpawnRepository spawnRepository;
	private final GameRepository gameRepository;
	private final ArenaService arenaService;

	public SetSpawnListener(LanguageRepository languageRepository, SpawnRepository spawnRepository, GameRepository gameRepository, ArenaService arenaService) {
		this.languageRepository = languageRepository;
		this.spawnRepository = spawnRepository;
		this.gameRepository = gameRepository;
		this.arenaService = arenaService;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		this.languageRepository.loadLanguageConfig(player);
		ItemStack item = event.getItem();
		if (item == null || item.getType() != Material.STICK || !item.hasItemMeta() ||
			!item.getItemMeta().getDisplayName().equals(this.languageRepository.getMessage("setspawn.stick-name"))) {
			return;
		}
		String arenaName = item.getItemMeta().getLore().get(0);

		if (!this.spawnRepository.getPlayerSpawns(arenaName).isEmpty()) {
			player.sendMessage(this.languageRepository.getMessage("setspawn.game-not-empty"));
			event.setCancelled(true);
			return;
		}

		if (this.gameRepository.isGameStarted(arenaName) || this.gameRepository.isGameStarting(arenaName)) {
			player.sendMessage(this.languageRepository.getMessage("setspawn.game-in-progress"));
			event.setCancelled(true);
			return;
		}

		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			addSpawnPoint(event, player);
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			removeSpawnPoint(event, player);
		}
		event.setCancelled(true);
	}

	private void removeSpawnPoint(PlayerInteractEvent event, Player player) {
		// TODO Implement SpawnPoint removal
	}

	private void addSpawnPoint(PlayerInteractEvent event, Player player) {
		Location newSpawnPoint = event.getClickedBlock().getLocation();
		newSpawnPoint.add(0.5, 1.5, 0.5);
		this.arenaService.addSpawn(player, newSpawnPoint);
	}
}
