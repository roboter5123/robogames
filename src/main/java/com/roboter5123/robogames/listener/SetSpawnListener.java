package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.model.SpawnPoint;
import com.roboter5123.robogames.service.ConfigService;
import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.SpawnService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SetSpawnListener implements Listener {

    private final LanguageService languageService;
    private final SpawnService spawnService;
    private final ConfigService configService;
    private final GameService gameService;

    public SetSpawnListener(LanguageService languageService, SpawnService spawnService, ConfigService configService, GameService gameService) {
        this.languageService = languageService;
        this.spawnService = spawnService;
        this.configService = configService;
        this.gameService = gameService;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        this.languageService.loadLanguageConfig(player);
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.STICK || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals(this.languageService.getMessage("setspawn.stick-name"))) {
            return;
        }

        if (!this.spawnService.getPlayerSpawnPoints().isEmpty()) {
            player.sendMessage(this.languageService.getMessage("setspawn.game-not-empty"));
            event.setCancelled(true);
            return;
        }

        if (this.gameService.isGameStarted() || this.gameService.isGameStarting()) {
            player.sendMessage(this.languageService.getMessage("setspawn.game-in-progress"));
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
        Location location = event.getClickedBlock().getLocation();
        List<SpawnPoint> allSpawns = this.spawnService.getAllSpawns();
        SpawnPoint newSpawnPoint = new SpawnPoint();
        newSpawnPoint.setWorld(location.getWorld().getName());
        newSpawnPoint.setLocation(location);

        if (allSpawns.contains(newSpawnPoint)) {
            player.sendMessage(this.languageService.getMessage("setspawnhandler.duplicate"));
            return;
        }

        if (allSpawns.size() == this.configService.getMaxPlayers()) {
            player.sendMessage(ChatColor.RED + this.languageService.getMessage("setspawnhandler.max-spawn"));
            return;
        }

        this.spawnService.addSpawn(newSpawnPoint);
        player.sendMessage(this.languageService.getMessage("setspawnhandler.position-set") + allSpawns.size() + this.languageService.getMessage("setspawnhandler.set-at") + location.getBlockX() + this.languageService.getMessage("setspawnhandler.coord-y") + location.getBlockY() + this.languageService.getMessage("setspawnhandler.coord-z") + location.getBlockZ());
    }
}
