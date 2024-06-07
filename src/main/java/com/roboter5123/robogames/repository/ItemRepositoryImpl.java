package com.roboter5123.robogames.repository;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.service.model.ChestLootTable;
import com.roboter5123.robogames.service.model.LootTableEntry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemRepositoryImpl implements ItemRepository {

	private final Random random;
	private final RoboGames roboGames;
	private final ConfigRepository configRepository;
	private final Map<String, Map<ChestLootTable, List<LootTableEntry>>> lootTables;
	private final Map<String, Map<ChestLootTable, List<LootTableEntry>>> weightedItemsList;
	private static final String ITEMS_FILE_NAME = "items.yml";

	public ItemRepositoryImpl(RoboGames roboGames, ConfigRepository configRepository) {
		this.roboGames = roboGames;
		this.configRepository = configRepository;
		this.lootTables = new HashMap<>();
		this.weightedItemsList = new HashMap<>();
		this.random = new Random();
	}

	@Override
	public void loadItemsConfig() {
		// TODO: Check for bugs
		this.lootTables.clear();
		this.weightedItemsList.clear();
		File itemsFile = this.configRepository.loadConfigFile(ITEMS_FILE_NAME);
		YamlConfiguration itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);
		Set<String> arenaNames = itemsConfig.getConfigurationSection("").getKeys(false);
		for (String arenaName : arenaNames) {
			parseItemConfigForArena(arenaName, itemsConfig);
		}
	}

	private void parseItemConfigForArena(String arenaName, YamlConfiguration itemsConfig) {
		this.lootTables.put(arenaName, new HashMap<>());
		this.weightedItemsList.put(arenaName, new HashMap<>());
		Set<String> lootTableNames = itemsConfig.getConfigurationSection(arenaName).getKeys(false);
		// Parse and add items to lootTable and weighted items list
		for (String lootTableName : lootTableNames) {
			ChestLootTable lootTableLabel = ChestLootTable.valueOf(lootTableName);
			this.lootTables.get(arenaName).put(lootTableLabel, new ArrayList<>());
			this.weightedItemsList.get(arenaName).put(lootTableLabel, new ArrayList<>());
			ConfigurationSection arenaItemsConfig = itemsConfig.getConfigurationSection(arenaName);
			List<Map<?, ?>> itemMaps = arenaItemsConfig.getMapList(lootTableName);
			List<LootTableEntry> lootTable = itemMaps.stream().map(this::convertToLootTableEntry).collect(Collectors.toList());
			this.lootTables.get(arenaName).put(lootTableLabel, lootTable);
			// Add item to weighted list the amount of weight it has
			for (LootTableEntry lootTableEntry : lootTable) {
				for (int j = 0; j < lootTableEntry.getWeight(); j++) {
					this.weightedItemsList.get(arenaName).get(lootTableLabel).add(lootTableEntry);
				}
			}
		}
	}

	@Override
	public ItemStack getRandomChestItem(String arenaName, ChestLootTable lootTable) {
		// TODO: Check for bugs
		List<LootTableEntry> lootTableEntries = this.weightedItemsList.get(arenaName).get(lootTable);
		LootTableEntry lootTableEntry = lootTableEntries.get(random.nextInt(lootTableEntries.size()));
		return new ItemStack(lootTableEntry.getType(), lootTableEntry.getAmount());
	}

	private LootTableEntry convertToLootTableEntry(Map<?, ?> map) {
		LootTableEntry lootTableEntry = new LootTableEntry();
		lootTableEntry.setType(Material.valueOf((String) map.get("type")));
		lootTableEntry.setAmount((Integer) map.get("amount"));
		lootTableEntry.setWeight((Integer) map.get("weight"));
		return lootTableEntry;
	}
}