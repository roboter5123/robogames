package com.roboter5123.robogames.service;

import com.roboter5123.robogames.service.model.ChestLootTable;
import org.bukkit.inventory.ItemStack;

public interface ItemService {
    void loadItemsConfig();
    ItemStack getRandomChestItem(String arenaName, ChestLootTable lootTable);
}
