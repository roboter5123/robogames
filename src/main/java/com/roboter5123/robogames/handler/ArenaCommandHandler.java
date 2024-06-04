package com.roboter5123.robogames.handler;

import com.roboter5123.robogames.tasks.command.CreateArenaCommand;
import com.roboter5123.robogames.tasks.command.GiveWandCommand;
import com.roboter5123.robogames.tasks.command.ScanArenaCommand;
import com.roboter5123.robogames.tasks.command.SetSpawnCommand;
import com.roboter5123.robogames.tasks.command.RefillChestCommand;
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

    private final LanguageService languageService;
    private final ArenaService arenaService;
    private final SpawnService spawnService;
    private final GameService gameService;
    private final ChestService chestService;
    private final ItemService itemService;

    public ArenaCommandHandler(LanguageService languageService, ArenaService arenaService, SpawnService spawnService, GameService gameService, ChestService chestService, ItemService itemService) {
        this.languageService = languageService;
        this.arenaService = arenaService;
        this.spawnService = spawnService;
        this.gameService = gameService;
        this.chestService = chestService;
        this.itemService = itemService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(this.languageService.getMessage("usage"));
            return false;
        }

        if (!(commandSender instanceof Player player)) {
            return false;
        }

        this.languageService.loadLanguageConfig(player);
        return switch (args[0].toLowerCase()) {
            case "wand" -> giveWand(player);
            case "create" -> createArena(player, args[1]);
            case "setspawn" -> setSpawn(player, args[1]);
            case "scan" -> scanArena(player, args[1]);
            case "refill" -> refillArena(player, args[1]);
            default -> {
                commandSender.sendMessage(this.languageService.getMessage("unknown-subcommand") + args[0]);
                yield false;
            }
        };
    }

    private boolean refillArena(Player player, String arenaName) {
        if (!player.hasPermission("robogames.arena.refill")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        new RefillChestsCommand(this.chestService, this.arenaService, this.itemService, arenaName).run();
        return true;
    }

    private boolean scanArena(Player player, String arenaName) {
        if (!player.hasPermission("robogames.arena.scan")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        new ScanArenaCommand(player, this.languageService, this.arenaService, this.chestService, arenaName).run();
        return true;
    }

    private boolean giveWand(Player player) {
        if (!player.hasPermission("robogames.arena.wand")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        new GiveWandCommand(player, this.languageService).run();
        return true;
    }

    private boolean createArena(Player player, String arenaName) {
        if (!player.hasPermission("robogames.arena.create")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        } else if (arenaName == null || arenaName.isEmpty()) {
            return false;
        } else if (this.arenaService.getArenaNames().contains(arenaName)) {
            player.sendMessage(arenaName + languageService.getMessage("arena.arena-exists"));
            return true;
        }

        new CreateArenaCommand(player, this.languageService, arenaService, arenaName).run();
        return true;
    }

    private boolean setSpawn(Player player, String arenaName) {
        if (!player.hasPermission("robogames.arena.setspawn")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        new SetSpawnCommand(player, this.languageService, this.spawnService, this.gameService, arenaName).run();
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
