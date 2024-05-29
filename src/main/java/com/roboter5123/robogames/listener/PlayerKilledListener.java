package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.PlayerService;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerKilledListener implements Listener {

    private final LanguageService languageService;

    private final PlayerService playerService;

    public PlayerKilledListener(LanguageService languageService, PlayerService playerService) {
        this.languageService = languageService;
        this.playerService = playerService;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        String arenaName = this.playerService.getArenaNameByPlayer(player);

        if (!this.playerService.getInGamePlayers(arenaName).contains(player)) {
            return;
        }

        if (!this.playerService.getAlivePlayers(arenaName).contains(player)) {
            return;
        }

        double healthAfterDamage = player.getHealth() - event.getFinalDamage();
        if (healthAfterDamage > 0) {
            return;
        }

        this.playerService.removeAlivePlayer(arenaName, player);
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(20);
        String message = player.getName() + this.languageService.getMessage("death-message");
        new BroadCastIngameTask(this.playerService, message, arenaName).run();
        event.setCancelled(true);
    }
}
