package com.roboter5123.robogames.listener;

import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.PlayerRepository;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SelectPosListener implements Listener {

    private final LanguageRepository languageRepository;
    private final PlayerRepository playerRepository;

    public SelectPosListener(LanguageRepository languageRepository, PlayerRepository playerRepository) {
        this.languageRepository = languageRepository;
        this.playerRepository = playerRepository;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.BLAZE_ROD || !item.hasItemMeta() || !Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals(this.languageRepository.getMessage("arena.stick-name"))) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            this.languageRepository.loadLanguageConfig(player);
            player.sendMessage(this.languageRepository.getMessage("setarena.first-pos-1") + Objects.requireNonNull(event.getClickedBlock()).getX() + this.languageRepository.getMessage("setarena.first-pos-2") + event.getClickedBlock().getY() + this.languageRepository.getMessage("setarena.first-pos-3") + event.getClickedBlock().getZ());
            this.playerRepository.setMetaDataOnPlayer(player, "arena_pos1", event.getClickedBlock().getLocation());
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            this.languageRepository.loadLanguageConfig(player);
            player.sendMessage(this.languageRepository.getMessage("setarena.second-pos-1") + Objects.requireNonNull(event.getClickedBlock()).getX() + this.languageRepository.getMessage("setarena.second-pos-2") + event.getClickedBlock().getY() + this.languageRepository.getMessage("setarena.second-pos-3") + event.getClickedBlock().getZ());
            this.playerRepository.setMetaDataOnPlayer(player, "arena_pos2", event.getClickedBlock().getLocation());
        }
        event.setCancelled(true);

    }
}
