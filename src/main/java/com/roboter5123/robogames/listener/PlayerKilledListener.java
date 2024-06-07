package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerKilledListener implements Listener {

    private final LanguageRepository languageRepository;

    private final PlayerRepository playerRepository;

    public PlayerKilledListener(LanguageRepository languageRepository, PlayerRepository playerRepository) {
        this.languageRepository = languageRepository;
        this.playerRepository = playerRepository;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        String arenaName = this.playerRepository.getArenaNameByPlayer(player);

        if (!this.playerRepository.getInGamePlayersByArenaName(arenaName).contains(player)) {
            return;
        }

        if (!this.playerRepository.getAlivePlayersByArenaName(arenaName).contains(player)) {
            return;
        }

        double healthAfterDamage = player.getHealth() - event.getFinalDamage();
        if (healthAfterDamage > 0) {
            return;
        }

        this.playerRepository.removeAlivePlayerByArenaName(arenaName, player);
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(20);
        String message = player.getName() + this.languageRepository.getMessage("death-message");
        this.playerRepository.getInGamePlayersByArenaName(arenaName).forEach(ingamePlayer -> ingamePlayer.sendMessage(message));
        event.setCancelled(true);
    }
}
