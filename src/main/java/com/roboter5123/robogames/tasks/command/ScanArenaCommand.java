package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.service.model.Arena;
import com.roboter5123.robogames.service.ArenaService;
import com.roboter5123.robogames.service.ChestService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.WorldService;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ScanArenaCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageService languageService;
    private final ArenaService arenaService;
    private final ChestService chestService;
    private final WorldService worldService;
    private final String arenaName;

    public ScanArenaCommand(Player player, LanguageService languageService, ArenaService arenaService, ChestService chestService, WorldService worldService, String arenaName) {
        this.player = player;
        this.languageService = languageService;
        this.arenaService = arenaService;
        this.chestService = chestService;
        this.worldService = worldService;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {
        Arena arena = this.arenaService.getArena(this.arenaName);
        if (arena == null) {
            player.sendMessage(this.languageService.getMessage("arena-doesnt-exists"));
            return;
        }

        LowHighCoordinates lowHighCoordinates = getLowHighCoordinates(arena);
        World world = this.worldService.getWorld(arena.getWorldName());
        try {
            this.chestService.removeAllChests(this.arenaName);
        } catch (Exception e) {
            this.player.sendMessage(this.languageService.getMessage("scanarena.failed-locations"));
            return;
        }

        for (int x = lowHighCoordinates.lowX(); x < lowHighCoordinates.highX(); x++) {
            for (int y = lowHighCoordinates.lowY(); y < lowHighCoordinates.highY(); y++) {
                for (int z = lowHighCoordinates.lowZ(); z < lowHighCoordinates.highZ(); z++) {
                    checkBlock(world, x, y, z);
                }
            }
        }
        player.sendMessage(languageService.getMessage("scanarena.saved-locations"));
    }

    private void checkBlock(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        if (!(block.getState() instanceof Chest chest)) {
            return;
        }
        chest.getBlockInventory().clear();
        try {
            this.chestService.addChest(this.arenaName, chest);
        } catch (IOException e) {
            this.player.sendMessage(this.languageService.getMessage("scanarena.failed-locations"));
        }
    }

    @NotNull
    private static LowHighCoordinates getLowHighCoordinates(Arena arena) {
        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();

        int lowX;
        int highX;
        if (pos1.getX() > pos2.getX()) {
            highX = (int) pos1.getX();
            lowX = (int) pos2.getX();
        } else {
            highX = (int) pos2.getX();
            lowX = (int) pos1.getX();
        }

        int lowY;
        int highY;
        if (pos1.getY() > pos2.getY()) {
            highY = (int) pos1.getY();
            lowY = (int) pos2.getY();
        } else {
            highY = (int) pos2.getY();
            lowY = (int) pos1.getY();
        }

        int lowZ;
        int highZ;
        if (pos1.getZ() > pos2.getZ()) {
            highZ = (int) pos1.getZ();
            lowZ = (int) pos2.getZ();
        } else {
            highZ = (int) pos2.getZ();
            lowZ = (int) pos1.getZ();
        }
        return new LowHighCoordinates(lowX, highX, lowY, highY, lowZ, highZ);
    }

    private record LowHighCoordinates(int lowX, int highX, int lowY, int highY, int lowZ, int highZ) {
    }
}
