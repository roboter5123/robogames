package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetSpawnCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageRepository languageRepository;
    private final SpawnRepository spawnRepository;
    private final GameRepository gameRepository;
    private final String arenName;

    public SetSpawnCommand(Player player, LanguageRepository languageRepository, SpawnRepository spawnRepository, GameRepository gameRepository, String arenName) {
        this.player = player;
        this.languageRepository = languageRepository;
        this.spawnRepository = spawnRepository;
        this.gameRepository = gameRepository;
        this.arenName = arenName;
    }

    @Override
    public void run() {

        Map<Player, Location> playerSpawnPoints = this.spawnRepository.getPlayerSpawns(this.arenName);

        if (!playerSpawnPoints.isEmpty()) {
            player.sendMessage(this.languageRepository.getMessage("setspawn.game-not-empty"));
            return;
        }

        if (this.gameRepository.isGameStarting(this.arenName) || this.gameRepository.isGameStarted(this.arenName)) {
            player.sendMessage(this.languageRepository.getMessage("setspawn.game-in-progress"));
            return;
        }

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.languageRepository.getMessage("setspawn.stick-name"));
        List<String> lore = new ArrayList<>();
        lore.add(this.arenName);
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
