package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.service.ArenaService;
import com.roboter5123.robogames.service.ChestService;
import com.roboter5123.robogames.service.ItemService;
import com.roboter5123.robogames.service.model.Arena;
import com.roboter5123.robogames.service.model.ChestLootTable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RefillChestsCommand extends BukkitRunnable {

    private final ChestService chestService;
    private final ArenaService arenaService;
    private final ItemService itemService;
    private final String arenaName;
    private final Random random;


    public RefillChestsCommand(ChestService chestService, ArenaService arenaService, ItemService itemService, String arenaName) {
        this.chestService = chestService;
        this.arenaService = arenaService;
        this.arenaName = arenaName;
        this.random = new Random();
        this.itemService = itemService;
    }

    @Override
    public void run() {
        List<Location> chestLocations = this.chestService.getChestLocations(this.arenaName);
        Arena arena = this.arenaService.getArena(arenaName);
        World world = this.arenaService.getWorld(arena.getWorldName());
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
            int itemCount = random.nextInt(3, 7);
            inventory.clear();
            List<Integer> usedSlots = new ArrayList<>();
            for (int i = 0; i < itemCount; i++) {
                ItemStack randomChestItem = this.itemService.getRandomChestItem(this.arenaName, lootTable);
                int slot;
                do {
                    slot = random.nextInt(inventory.getSize());
                } while (usedSlots.contains(slot));
                inventory.setItem(slot, randomChestItem);
                usedSlots.add(slot);
            }
        }
    }
}
