package com.roboter5123.robogames.command;

import com.roboter5123.robogames.model.Arena;
import com.roboter5123.robogames.service.ArenaService;
import com.roboter5123.robogames.service.LanguageService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class CreateArenaCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageService languageService;
    private final ArenaService arenaService;
    private final String arenaName;


    public CreateArenaCommand(Player player, LanguageService languageService, ArenaService arenaService, String arenaName) {
        this.player = player;
        this.languageService = languageService;
        this.arenaService = arenaService;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {

        if (!player.hasMetadata("arena_pos1") || !player.hasMetadata("arena_pos2")) {
            player.sendMessage(this.languageService.getMessage("arena.no-values"));
            return;
        }

        Location pos1 = (Location) player.getMetadata("arena_pos1").get(0).value();
        Location pos2 = (Location) player.getMetadata("arena_pos2").get(0).value();
        if (pos1 == null || pos2 == null) {
            player.sendMessage(this.languageService.getMessage("arena.invalid-values"));
            return;
        }

        if (this.arenaService.getArenaNames().contains(this.arenaName)){
            player.sendMessage(this.languageService.getMessage("arena.arena-exists"));
            return;
        }


        Arena arena = new Arena();
        arena.setName(this.arenaName);
        arena.setWorldName(player.getWorld().getName());
        arena.setPos1(pos1);
        arena.setPos2(pos2);
        try {
            this.arenaService.createArena(arena);
        } catch (IOException e) {
            player.sendMessage(this.languageService.getMessage("arena.creation-failed"));
        }
        player.sendMessage(this.languageService.getMessage("arena.region-created"));
    }
}
