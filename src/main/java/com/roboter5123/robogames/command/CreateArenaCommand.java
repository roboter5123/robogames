package com.roboter5123.robogames.command;

import com.roboter5123.robogames.model.Arena;
import com.roboter5123.robogames.model.Coordinate;
import com.roboter5123.robogames.service.ArenaService;
import com.roboter5123.robogames.service.LanguageService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateArenaCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageService languageService;

    private final ArenaService arenaService;


    public CreateArenaCommand(Player player, LanguageService languageService, ArenaService arenaService) {
        this.player = player;
        this.languageService = languageService;
        this.arenaService = arenaService;
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
        Arena arena = new Arena();
        arena.setWorld(player.getWorld().getName());
        arena.setPos1(convertToCoordinate(pos1));
        arena.setPos2(convertToCoordinate(pos2));
        this.arenaService.createArena(arena);
        player.sendMessage(this.languageService.getMessage("arena.region-created"));
    }

    private Coordinate convertToCoordinate(Location location) {
        Coordinate coordinate = new Coordinate();
        coordinate.setxCoordinate(location.getX());
        coordinate.setyCoordinate(location.getY());
        coordinate.setzCoordinate(location.getZ());
        return coordinate;
    }
}
