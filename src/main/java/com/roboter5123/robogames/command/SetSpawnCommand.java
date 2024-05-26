package com.roboter5123.robogames.command;

import com.roboter5123.robogames.model.SpawnPoint;
import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.SpawnService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class SetSpawnCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageService languageService;
    private final SpawnService spawnService;
    private final GameService gameService;

    public SetSpawnCommand(Player player, LanguageService languageService, SpawnService spawnService, GameService gameService) {
        this.player = player;
        this.languageService = languageService;
        this.spawnService = spawnService;
        this.gameService = gameService;
    }

    @Override
    public void run() {

        Map<Player, SpawnPoint> playerSpawnPoints = this.spawnService.getPlayerSpawnPoints();
        if (!playerSpawnPoints.isEmpty()) {
            player.sendMessage(this.languageService.getMessage("setspawn.game-not-empty"));
            return;
        }

        if (this.gameService.isGameStarting() || this.gameService.isGameStarted()) {
            player.sendMessage(this.languageService.getMessage("setspawn.game-in-progress"));
            return;
        }

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.languageService.getMessage("setspawn.stick-name"));
        stick.setItemMeta(meta);
        player.getInventory().addItem(stick);
        player.sendMessage(this.languageService.getMessage("setspawn.given-stick"));
        this.spawnService.clearAllSpawns();
        player.sendMessage(this.languageService.getMessage("setspawn.spawn-reset"));
    }
}
