package com.roboter5123.robogames.handler;

import com.roboter5123.robogames.command.CreateArenaCommand;
import com.roboter5123.robogames.command.SelectArenaCommand;
import com.roboter5123.robogames.command.SetSpawnCommand;
import com.roboter5123.robogames.service.ArenaService;
import com.roboter5123.robogames.service.GameService;
import com.roboter5123.robogames.service.LanguageService;
import com.roboter5123.robogames.service.SpawnService;
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

    public ArenaCommandHandler(LanguageService languageService, ArenaService arenaService, SpawnService spawnService, GameService gameService) {
        this.languageService = languageService;
        this.arenaService = arenaService;
        this.spawnService = spawnService;
        this.gameService = gameService;
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
            case "select" -> selectArena(player);
            case "create" -> createArena(player, args[1]);
            case "setspawn" -> setSpawn(player, args[1]);
            default -> {
                commandSender.sendMessage(this.languageService.getMessage("unknown-subcommand") + args[0]);
                yield false;
            }
        };
    }

    private boolean selectArena(Player player) {
        if (!player.hasPermission("robogames.select")) {
            player.sendMessage(this.languageService.getMessage(""));
            return true;
        }
        new SelectArenaCommand(player, this.languageService).run();
        return true;
    }

    private boolean createArena(Player player, String arenaName) {
        if (!player.hasPermission("robogames.create")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }

        if (arenaName == null || arenaName.isEmpty()) {
            return true;
        }

        if (this.arenaService.getArenaNames().contains(arenaName)) {
            player.sendMessage(arenaName + languageService.getMessage("arena.arena-exists"));
            return true;
        }
        new CreateArenaCommand(player, this.languageService, arenaService, arenaName).run();
        return true;
    }

    private boolean setSpawn(Player player, String arenaName) {
        if (!player.hasPermission("robogames.setspawn")) {
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
        String[] commands = {"create", "select", "setspawn"};
        for (String cmd : commands) {
            if (!commandSender.hasPermission("robogames.arena." + cmd)) {
                continue;
            }
            completions.add(cmd);
        }
        return completions;
    }
}
