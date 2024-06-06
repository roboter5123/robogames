package com.roboter5123.robogames.service;

import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WandServiceImpl implements WandService {

    private final LanguageRepository languageRepository;
    private final SpawnRepository spawnRepository;
    private final GameRepository gameRepository;
    

    public WandServiceImpl(LanguageRepository languageRepository, SpawnRepository spawnRepository, GameRepository gameRepository) {
        this.languageRepository = languageRepository;
        this.spawnRepository = spawnRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void giveArenaWand(Player player, String arenaName) {
        ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = blazeRod.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.languageRepository.getMessage("arena.stick-name"));
        List<String> lore = new ArrayList<>();
        lore.add(this.languageRepository.getMessage("arena.stick-left"));
        lore.add(this.languageRepository.getMessage("arena.stick-right"));
        meta.setLore(lore);
        blazeRod.setItemMeta(meta);
        player.getInventory().addItem(blazeRod);
        player.sendMessage(this.languageRepository.getMessage("arena.given-stick"));
    }

    @Override
    public void giveSpawnWand(Player player, String arenaName) {
        Map<Player, Location> playerSpawnPoints = this.spawnRepository.getPlayerSpawns(arenaName);

        if (!playerSpawnPoints.isEmpty()) {
            player.sendMessage(this.languageRepository.getMessage("setspawn.game-not-empty"));
            return;
        }

        if (this.gameRepository.isGameStarting(arenaName) || this.gameRepository.isGameStarted(arenaName)) {
            player.sendMessage(this.languageRepository.getMessage("setspawn.game-in-progress"));
            return;
        }

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.languageRepository.getMessage("setspawn.stick-name"));
        List<String> lore = new ArrayList<>();
        lore.add(arenaName);
        meta.setLore(lore);
        stick.setItemMeta(meta);
        player.getInventory().addItem(stick);
        player.sendMessage(this.languageRepository.getMessage("setspawn.given-stick"));
        try {
            this.spawnRepository.clearAllSpawns("arena");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(this.languageRepository.getMessage("setspawn.spawn-reset"));
    }
}
