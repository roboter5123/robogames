package com.roboter5123.robogames.handler;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.service.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArenaCommandHandler implements CommandExecutor, TabCompleter {

    private final LanguageRepository languageRepository;
    private final ArenaRepository arenaRepository;
    private final ArenaService arenaService;
    private final WandService wandService;

    public ArenaCommandHandler(LanguageRepository languageRepository, ArenaRepository arenaRepository,
                               ArenaService arenaService, WandService wandService) {
        this.languageRepository = languageRepository;
        this.arenaRepository = arenaRepository;
        this.arenaService = arenaService;
        this.wandService = wandService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(this.languageRepository.getMessage("usage"));
            return false;
        }

        if (!(commandSender instanceof Player player)) {
            return false;
        }

        this.languageRepository.loadLanguageConfig(player);
        return switch (args[0].toLowerCase()) {
            case "wand" -> giveWand(player);
            case "create" -> createArena(player, args[1]);
            case "setspawn" -> setSpawn(player, args[1]);
            case "scan" -> scanArena(player, args[1]);
            default -> {
                commandSender.sendMessage(this.languageRepository.getMessage("unknown-subcommand") + args[0]);
                yield false;
            }
        };
    }

    private boolean scanArena(Player player, String arenaName) {
        if (!player.hasPermission("robogames.arena.scan")) {
            player.sendMessage(this.languageRepository.getMessage(""));
            return true;
        }
        this.arenaService.scanArena(player, arenaName);
        return true;
    }

    private boolean giveWand(Player player) {
        if (!player.hasPermission("robogames.arena.wand")) {
            player.sendMessage(this.languageRepository.getMessage(""));
            return true;
        }
        this.wandService.giveArenaWand(player, null);
        return true;
    }

    private boolean createArena(Player player, String arenaName) {
        if (!player.hasPermission("robogames.arena.create")) {
            player.sendMessage(this.languageRepository.getMessage("no-permission"));
            return true;
        } else if (arenaName == null || arenaName.isEmpty()) {
            return false;
        } else if (this.arenaRepository.getArenaNames().contains(arenaName)) {
            player.sendMessage(arenaName + languageRepository.getMessage("arena.arena-exists"));
            return true;
        }

        this.arenaService.createArena(player, arenaName);
        return true;
    }

    private boolean setSpawn(Player player, String arenaName) {
        if (!player.hasPermission("robogames.arena.setspawn")) {
            player.sendMessage(this.languageRepository.getMessage("no-permission"));
            return true;
        }
        this.wandService.giveSpawnWand(player, arenaName);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 1) {
            return new ArrayList<>();
        }

        List<String> completions = new ArrayList<>();
        String[] commands = {"create", "wand", "setspawn"};
        for (String cmd : commands) {
            if (!commandSender.hasPermission("robogames.arena." + cmd)) {
                continue;
            }
            completions.add(cmd);
        }
        return completions;
    }
}
