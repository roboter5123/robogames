package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.service.model.Arena;
import com.roboter5123.robogames.service.model.ChestLootTable;
import com.roboter5123.robogames.service.*;
import com.roboter5123.robogames.tasks.BroadCastIngameTask;
import com.roboter5123.robogames.tasks.GameLoopTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class StartGameCommand extends BukkitRunnable {

    private final GameService gameService;
    private final SchedulerService schedulerService;
    private final LanguageService languageService;
    private final PlayerService playerService;
    private final SpawnService spawnService;
    private final String arenaName;
    private final ChestService chestService;
    private final WorldService worldService;
    private final ArenaService arenaService;
    private final Random random;
    private final ItemService itemService;

    public StartGameCommand(GameService gameService, SchedulerService schedulerService, LanguageService languageService, PlayerService playerService, SpawnService spawnService, ChestService chestService, WorldService worldService, ArenaService arenaService, ItemService itemService, String arenaName) {
        this.gameService = gameService;
        this.schedulerService = schedulerService;
        this.languageService = languageService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.chestService = chestService;
        this.worldService = worldService;
        this.arenaService = arenaService;
        this.arenaName = arenaName;
        this.itemService = itemService;
        this.random = new Random();
    }

    @Override
    public void run() {

        if (this.gameService.isGameStarted(arenaName) || this.gameService.isGameStarting(arenaName)){
            return;
        }
        this.gameService.setGameStarting(this.arenaName, true);
        this.gameService.setTimerTicks(this.arenaName, 0L);
        fillChests();
        broadcastCountdown("startgame.20-s", 0L);
        broadcastCountdown("startgame.15-s", 20L * 5);
        broadcastCountdown("startgame.10-s", 20L * 10);
        broadcastCountdown("startgame.5-s", 20L * 15);
        broadcastCountdown("startgame.4-s", 20L * 16);
        broadcastCountdown("startgame.3-s", 20L * 17);
        broadcastCountdown("startgame.2-s", 20L * 18);
        broadcastCountdown("startgame.1-s", 20L * 19);
        broadcastCountdown("game.game-start", 20L * 20);

        BukkitRunnable startGameTask = new BukkitRunnable() {
            public void run() {
                gameService.setGameStarted(arenaName, true);
                gameService.setGameStarting(arenaName, false);
            }
        };
        this.schedulerService.scheduleDelayedTask(startGameTask, 20L * 20);

        BukkitRunnable gameCheckTask = new GameLoopTask(gameService, playerService, spawnService, languageService, 20L, arenaName);
        this.schedulerService.scheduleRepeatingTask(gameCheckTask, 20L * 20 + 2, 20L);
    }

    private void fillChests() {

        List<Location> chestLocations = this.chestService.getChestLocations(this.arenaName);
        Arena arena = this.arenaService.getArena(arenaName);
        World world = this.worldService.getWorld(arena.getWorldName());
        for (Location chestLocation : chestLocations) {
            Block block = world.getBlockAt(chestLocation);
            Inventory inventory;
            ChestLootTable lootTable;
            if (block.getState() instanceof Chest chest && block.getType() == Material.CHEST) {
                inventory = chest.getBlockInventory();
                lootTable = ChestLootTable.CHEST;
            } else if (block.getState() instanceof Chest chest && block.getType() == Material.TRAPPED_CHEST) {
                inventory = chest.getBlockInventory();
                lootTable = ChestLootTable.TRAPPED_CHEST;
            } else if (block.getState() instanceof Barrel barrel) {
                inventory = barrel.getInventory();
                lootTable = ChestLootTable.BARREL;
            } else continue;
            // TODO: Change max items and min items based on config
            int itemCount = random.nextInt(1, 10);
            inventory.clear();
            for (int i = 0; i < itemCount; i++) {
                ItemStack randomChestItem = this.itemService.getRandomChestItem(this.arenaName, lootTable);
                inventory.setItem(i, randomChestItem);
            }
        }
    }

    private void broadcastCountdown(String messageKey, long ticksUntil) {
        String message = languageService.getMessage(messageKey);
        BukkitRunnable gameStartsInTask = new BroadCastIngameTask(this.playerService, message, this.arenaName);
        this.schedulerService.scheduleDelayedTask(gameStartsInTask, ticksUntil);
    }
}
