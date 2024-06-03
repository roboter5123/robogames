package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.service.LanguageService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GiveWandCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageService languageService;

    public GiveWandCommand(Player player, LanguageService languageService) {
        this.player = player;
        this.languageService = languageService;
    }

    @Override
    public void run() {
        ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = blazeRod.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.languageService.getMessage("arena.stick-name"));
        List<String> lore = new ArrayList<>();
        lore.add(this.languageService.getMessage("arena.stick-left"));
        lore.add(this.languageService.getMessage("arena.stick-right"));
        meta.setLore(lore);
        blazeRod.setItemMeta(meta);
        player.getInventory().addItem(blazeRod);
        player.sendMessage(this.languageService.getMessage("arena.given-stick"));
    }
}
