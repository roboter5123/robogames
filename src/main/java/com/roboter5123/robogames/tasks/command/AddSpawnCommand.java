package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.service.model.Arena;
import com.roboter5123.robogames.service.ArenaService;
import com.roboter5123.robogames.service.ConfigService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.SpawnService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AddSpawnCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageService languageService;
    private final ArenaService arenaService;
    private final Location newSpawnPoint;
    private final ConfigService configService;
    private final SpawnService spawnService;

    public AddSpawnCommand(Player player, LanguageService languageService, ArenaService arenaService, ConfigService configService, SpawnService spawnService, Location newSpawnPoint) {
        this.player = player;
        this.languageService = languageService;
        this.arenaService = arenaService;
        this.newSpawnPoint = newSpawnPoint;
        this.configService = configService;
        this.spawnService = spawnService;
    }

    @Override
    public void run() {

        Optional<Arena> arenaOptional = this.arenaService.getArenaNames().stream()
                .map(this.arenaService::getArena)
                .filter(arena -> newSpawnPoint.getWorld().getName().equals(arena.getWorldName()))
                .findFirst();
        if (arenaOptional.isEmpty()) {
            return;
        }
        Arena arena = arenaOptional.get();
        List<Location> allSpawns = this.spawnService.getAllSpawns(arena.getName());

        if (allSpawns.contains(newSpawnPoint)) {
            player.sendMessage(this.languageService.getMessage("setspawnhandler.duplicate"));
            return;
        }

        if (!this.arenaService.isInArenaBounds(arena.getName(), newSpawnPoint)) {
            player.sendMessage(this.languageService.getMessage("setspawnhandler.not-in-arena"));
            return;
        }

        if (allSpawns.size() == this.configService.getMaxPlayers()) {
            player.sendMessage(ChatColor.RED + this.languageService.getMessage("setspawnhandler.max-spawn"));
            return;
        }

        try {
            this.spawnService.createSpawn(arena.getName(), newSpawnPoint);
        } catch (IOException e) {
            player.sendMessage(languageService.getMessage("arena.save-error"));
        }
        player.sendMessage(this.languageService.getMessage("setspawnhandler.position-set") + allSpawns.size() + this.languageService.getMessage("setspawnhandler.set-at") + newSpawnPoint.getBlockX() + this.languageService.getMessage("setspawnhandler.coord-y") + newSpawnPoint.getBlockY() + this.languageService.getMessage("setspawnhandler.coord-z") + newSpawnPoint.getBlockZ());
    }
}
