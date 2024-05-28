package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.model.Arena;
import com.roboter5123.robogames.service.*;
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
        Location newSpawnPoint = event.getClickedBlock().getLocation();
        newSpawnPoint.add(0.5, 1.5, 0.5);
        List<Location> allSpawns = this.spawnService.getAllSpawns();

        if (allSpawns.contains(newSpawnPoint)) {
            player.sendMessage(this.languageService.getMessage("setspawnhandler.duplicate"));
            return;
        }

        Arena arena = this.arenaService.getArena();

        if (arena == null) {
            player.sendMessage(this.languageService.getMessage("setspawnhandler.not-in-arena"));
            return;
        }

        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();
        boolean isInXArenaBounds = newSpawnPoint.getX() >= Math.min(pos1.getX(), pos2.getX()) && newSpawnPoint.getX() <= Math.max(pos1.getX(), pos2.getX());
        boolean isInYArenaBounds = newSpawnPoint.getY() >= Math.min(pos1.getY(), pos2.getY()) && newSpawnPoint.getY() <= Math.max(pos1.getY(), pos2.getY());
        boolean isInZArenaBounds = newSpawnPoint.getZ() >= Math.min(pos1.getZ(), pos2.getZ()) && newSpawnPoint.getZ() <= Math.max(pos1.getZ(), pos2.getZ());
        if (!isInXArenaBounds || !isInYArenaBounds || !isInZArenaBounds) {
            player.sendMessage(this.languageService.getMessage("setspawnhandler.not-in-arena"));
            return;
        }

        if (allSpawns.size() == this.configService.getMaxPlayers()) {
            player.sendMessage(ChatColor.RED + this.languageService.getMessage("setspawnhandler.max-spawn"));
            return;
        }

        this.spawnService.addSpawn(newSpawnPoint);
        player.sendMessage(this.languageService.getMessage("setspawnhandler.position-set") + allSpawns.size() + this.languageService.getMessage("setspawnhandler.set-at") + newSpawnPoint.getBlockX() + this.languageService.getMessage("setspawnhandler.coord-y") + newSpawnPoint.getBlockY() + this.languageService.getMessage("setspawnhandler.coord-z") + newSpawnPoint.getBlockZ());
    }
}
