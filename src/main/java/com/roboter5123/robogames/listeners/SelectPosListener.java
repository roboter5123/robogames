package com.roboter5123.robogames.listeners;

import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.MetadataService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SelectPosListener implements Listener {

    private final LanguageService languageService;

    private final MetadataService metadataService;

    public SelectPosListener(LanguageService languageService, MetadataService metadataService) {
        this.languageService = languageService;
        this.metadataService = metadataService;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.BLAZE_ROD || !item.hasItemMeta() || !Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals(this.languageService.getMessage("arena.stick-name"))) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            this.languageService.loadLanguageConfig(player);
            player.sendMessage(this.languageService.getMessage("setarena.first-pos-1") + Objects.requireNonNull(event.getClickedBlock()).getX() + this.languageService.getMessage("setarena.first-pos-2") + event.getClickedBlock().getY() + this.languageService.getMessage("setarena.first-pos-3") + event.getClickedBlock().getZ());
            this.metadataService.setMetaDataOnPlayer(player, "arena_pos1", event.getClickedBlock().getLocation());
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            this.languageService.loadLanguageConfig(player);
            player.sendMessage(this.languageService.getMessage("setarena.second-pos-1") + Objects.requireNonNull(event.getClickedBlock()).getX() + this.languageService.getMessage("setarena.second-pos-2") + event.getClickedBlock().getY() + this.languageService.getMessage("setarena.second-pos-3") + event.getClickedBlock().getZ());
            this.metadataService.setMetaDataOnPlayer(player, "arena_pos2", event.getClickedBlock().getLocation());
        }

    }
}
