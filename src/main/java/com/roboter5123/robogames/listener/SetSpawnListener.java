package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.tasks.command.AddSpawnCommand;
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

    private final LanguageService languageService;
    private final SpawnService spawnService;
    private final ConfigService configService;
    private final GameService gameService;
    private final ArenaService arenaService;

    public SetSpawnListener(LanguageService languageService, SpawnService spawnService, ConfigService configService, GameService gameService, ArenaService arenaService) {
        this.languageService = languageService;
        this.spawnService = spawnService;
        this.configService = configService;
        this.gameService = gameService;
        this.arenaService = arenaService;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        this.languageService.loadLanguageConfig(player);
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.STICK || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals(this.languageService.getMessage("setspawn.stick-name"))) {
            return;
        }
        String arenaName = item.getItemMeta().getLore().getFirst();

        if (!this.spawnService.getPlayerSpawns(arenaName).isEmpty()) {
            player.sendMessage(this.languageService.getMessage("setspawn.game-not-empty"));
            event.setCancelled(true);
            return;
        }

        if (this.gameService.isGameStarted(arenaName) || this.gameService.isGameStarting(arenaName)) {
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
        Location newSpawnPoint = event.getClickedBlock().getLocation();
        newSpawnPoint.add(0.5, 1.5, 0.5);
        new AddSpawnCommand(player, this.languageService, this.arenaService, this.configService, this.spawnService, newSpawnPoint).run();
    }
}
