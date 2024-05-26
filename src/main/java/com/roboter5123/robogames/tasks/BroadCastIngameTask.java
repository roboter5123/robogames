package com.roboter5123.robogames.tasks;

import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.PlayerService;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastIngameTask extends BukkitRunnable {

    private final LanguageService languageService;
    private final PlayerService playerService;
    private final String messageKey;

    public BroadCastIngameTask(LanguageService languageService, PlayerService playerService, String messageKey) {
        this.languageService = languageService;
        this.playerService = playerService;
        this.messageKey = messageKey;
    }

    @Override
    public void run() {

        for (Player inGamePlayer : this.playerService.getInGamePlayers()) {
            inGamePlayer.sendMessage(this.languageService.getMessage(this.messageKey));
        }
    }
}
