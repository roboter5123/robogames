package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.model.ChestLootTable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ItemServiceImpl implements ItemService {

    private final Random random;
    private final RoboGames roboGames;

    public ItemServiceImpl(RoboGames roboGames) {
        this.roboGames = roboGames;
        this.random = new Random();
    }

    @Override
    public ItemStack getRandomChestItem(ChestLootTable lootTable) {
        // TODO: Actually Implement
        return new ItemStack(Material.CHAINMAIL_BOOTS, 1);
    }
}
