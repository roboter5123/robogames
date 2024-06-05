package com.roboter5123.robogames.tasks.command;

import com.roboter5123.robogames.repository.LanguageRepository;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GiveWandCommand extends BukkitRunnable {

    private final Player player;
    private final LanguageRepository languageRepository;

    public GiveWandCommand(Player player, LanguageRepository languageRepository) {
        this.player = player;
        this.languageRepository = languageRepository;
    }

    @Override
    public void run() {
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
}
