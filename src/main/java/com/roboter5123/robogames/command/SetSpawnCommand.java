package com.roboter5123.robogames.command;

import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.SpawnService;
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
    private final LanguageService languageService;
    private final SpawnService spawnService;
    private final GameService gameService;
    private final String arenName;

    public SetSpawnCommand(Player player, LanguageService languageService, SpawnService spawnService, GameService gameService, String arenName) {
        this.player = player;
        this.languageService = languageService;
        this.spawnService = spawnService;
        this.gameService = gameService;
        this.arenName = arenName;
    }

    @Override
    public void run() {

        Map<Player, Location> playerSpawnPoints = this.spawnService.getPlayerSpawns(this.arenName);

        if (!playerSpawnPoints.isEmpty()) {
            player.sendMessage(this.languageService.getMessage("setspawn.game-not-empty"));
            return;
        }

        if (this.gameService.isGameStarting(this.arenName) || this.gameService.isGameStarted(this.arenName)) {
            player.sendMessage(this.languageService.getMessage("setspawn.game-in-progress"));
            return;
        }

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.languageService.getMessage("setspawn.stick-name"));
        List<String> lore = new ArrayList<>();
        lore.add(this.arenName);
        meta.setLore(lore);
        stick.setItemMeta(meta);
        player.getInventory().addItem(stick);
        player.sendMessage(this.languageService.getMessage("setspawn.given-stick"));
        try {
            this.spawnService.clearAllSpawns("arena");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(this.languageService.getMessage("setspawn.spawn-reset"));
    }
}
