package com.roboter5123.robogames.repository;

import com.roboter5123.robogames.service.model.ChestLootTable;
import org.bukkit.inventory.ItemStack;

public interface ItemRepository {
    void loadItemsConfig();
    ItemStack getRandomChestItem(String arenaName, ChestLootTable lootTable);
}
