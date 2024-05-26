package com.roboter5123.robogames.handler;

import com.roboter5123.robogames.command.*;
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
import java.util.Random;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private final LanguageService languageService;
    private final PlayerService playerService;

    private final Random random;
    private final SpawnService spawnService;
    private final ArenaService arenaService;
    private final GameService gameService;

    private final SchedulerService schedulerService;

    public CommandHandler(LanguageService languageService, PlayerService playerService, SpawnService spawnService, ArenaService arenaService, GameService gameService, SchedulerService schedulerService) {
        this.languageService = languageService;
        this.playerService = playerService;
        this.spawnService = spawnService;
        this.arenaService = arenaService;
        this.gameService = gameService;
        this.schedulerService = schedulerService;
        this.random = new Random();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (args.length == 0) {
            commandSender.sendMessage(this.languageService.getMessage("usage"));
            return false;
        }

        if (!(commandSender instanceof Player player)) {
            return false;
        }

        this.languageService.loadLanguageConfig(player);
        switch (args[0].toLowerCase()) {
            case "select":
                return selectArena(player);
            case "create":
                return createArena(player);
            case "setspawn":
                return setSpawn(player);
            case "join":
                return joinGame(player);
            case "leave":
                return leaveGame(player);
            case "start":
                return startGame(player);
            default:
                commandSender.sendMessage(this.languageService.getMessage("unknown-subcommand") + args[0]);
                return false;
        }
    }

    private boolean startGame(Player player) {
        if (!player.hasPermission("hungergames.start")){
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        new StartGameCommand(this.gameService, this.schedulerService, this. languageService, this.playerService).run();
        return true;
    }

    private boolean setSpawn(Player player) {
        if (!player.hasPermission("robogames.setspawn")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return false;
        }
        new SetSpawnCommand(player, this.languageService, this.spawnService, this.gameService).run();
        return true;
    }

    private boolean createArena(Player player) {
        if (!player.hasPermission("robogames.create")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return false;
        }
        new CreateArenaCommand(player, languageService, arenaService).run();
        return true;
    }

    private boolean selectArena(Player player) {
        if (!player.hasPermission("robogames.select")) {
            player.sendMessage(this.languageService.getMessage(""));
            return false;
        }
        new SelectArenaCommand(player, this.languageService).run();
        return true;
    }

    private boolean leaveGame(Player player) {
        new LeaveGameCommand(player, languageService, gameService, playerService, spawnService).run();
        return true;
    }

    private boolean joinGame(Player player) {
        new JoinGameCommand(player, languageService, gameService, playerService, spawnService, arenaService).run();
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            String[] commands = {"join", "leave", "start", "end", "create", "select", "setspawn"};
            for (String cmd : commands) {
                if (sender.hasPermission("robogames." + cmd)) {
                    completions.add(cmd);
                }
            }
            return completions;
        }

        if (args[0].equalsIgnoreCase("border")) {
            List<String> completions = new ArrayList<>();
            if (args.length == 2) {
                completions.add(this.languageService.getMessage("border.args-1"));
            } else if (args.length == 3) {
                completions.add(this.languageService.getMessage("border.args-2"));
            } else if (args.length == 4) {
                completions.add(this.languageService.getMessage("border.args-3"));
            }
            return completions;
        }
        return new ArrayList<>();
    }


}
