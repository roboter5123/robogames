package com.roboter5123.robogames.service;

import com.roboter5123.robogames.model.ChestLootTable;
import org.bukkit.inventory.ItemStack;

public interface ItemService {
    ItemStack getRandomChestItem(ChestLootTable lootTable);
}
