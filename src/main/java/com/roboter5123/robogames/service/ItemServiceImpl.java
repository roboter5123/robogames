package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.service.model.ChestLootTable;
import com.roboter5123.robogames.service.model.LootTableEntry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ItemServiceImpl implements ItemService {

    private final Random random;
    private final RoboGames roboGames;
    private final ConfigService configService;
    private final Map<String, Map<ChestLootTable, LootTableEntry>> lootTables;
    private static final String ITEMS_FILE_NAME = "items.yml";

    public ItemServiceImpl(RoboGames roboGames, ConfigService configService) {
        this.roboGames = roboGames;
        this.configService = configService;
        this.lootTables = new HashMap<>();
        this.random = new Random();
    }

    @Override
    public void loadItemsConfig() {
        this.lootTables.clear();
        File itemsFile = this.configService.loadConfigFile(ITEMS_FILE_NAME);
        YamlConfiguration itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);
        Set<String> arenaNames = itemsConfig.getConfigurationSection("").getKeys(false);
        for (String arenaName : arenaNames) {
            YamlConfiguration arenaItemsConfig = itemsConfig.getConfigurationSection(arenaName);
            List<Map<?, ?>> itemMaps = arenaItemsConfig.getMapList(arenaName);

        }
    }

    @Override
    public ItemStack getRandomChestItem(ChestLootTable lootTable) {
        // TODO: Actually Implement
        return new ItemStack(Material.CHAINMAIL_BOOTS, 1);
    }
}
