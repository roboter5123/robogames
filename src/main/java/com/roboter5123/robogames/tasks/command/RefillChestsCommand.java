package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.ChestRepository;
import com.roboter5123.robogames.repository.ItemRepository;
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

    private final ChestRepository chestRepository;
    private final ArenaRepository arenaRepository;
    private final ItemRepository itemRepository;
    private final String arenaName;
    private final Random random;


    public RefillChestsCommand(ChestRepository chestRepository, ArenaRepository arenaRepository, ItemRepository itemRepository, String arenaName) {
        this.chestRepository = chestRepository;
        this.arenaRepository = arenaRepository;
        this.arenaName = arenaName;
        this.random = new Random();
        this.itemRepository = itemRepository;
    }

    @Override
    public void run() {
        List<Location> chestLocations = this.chestRepository.getChestLocations(this.arenaName);
        Arena arena = this.arenaRepository.getArena(arenaName);
        World world = this.arenaRepository.getWorld(arena.getWorldName());
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
                ItemStack randomChestItem = this.itemRepository.getRandomChestItem(this.arenaName, lootTable);
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
