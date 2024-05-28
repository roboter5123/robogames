package com.roboter5123.robogames.listener;

import org.bukkit.Location;

import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.PlayerService;
import com.roboter5123.robogames.service.SchedulerService;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import org.bukkit.event.Listener;

public class PlayerKilledListener implements Listener {

	private final LanguageService languageService;

	private final PlayerService playerService;

	public PlayerKilledListener(LanguageService languageService, PlayerService playerService, SchedulerService schedulerService) {
		this.languageService = languageService;
		this.playerService = playerService;
		this.schedulerService = schedulerService;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {

		if (!(event.getEntity instanceof Player player)) {
			return;
		}

		if (!this.playerService.getInGamePlayers().contains(player)) {
			return;
		}

		if (!this.playerService.getAlivePlayers().contains(player)) {
			return;
		}

		double healthAfterDamage = player.getHealth() - event.getFinalDamage();
		if (healthAfterDamage > 0) {
			return;
		}

		this.playerService.removeAlivePlayers(player);
		player.setGameMode(GameMode.SPECTATOR);
		String message = player.getDisplayName() + this.languageService.getMessage("death-message");
		new BroadCastIngameTask(this.playerService, message).run();
		event.cancel();
	}
}
