package com.roboter5123.robogames.handler;

import com.roboter5123.robogames.tasks.command.EndGameCommand;
import com.roboter5123.robogames.tasks.command.JoinGameCommand;
import com.roboter5123.robogames.tasks.command.LeaveGameCommand;
import com.roboter5123.robogames.tasks.command.StartGameCommand;
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

public class RoboGamesCommandHandler implements CommandExecutor, TabCompleter {

    private final LanguageService languageService;
    private final GameService gameService;
    private final SpawnService spawnService;
    private final PlayerService playerService;
    private final SchedulerService schedulerService;
    private final ArenaService arenaService;
    private final LobbyService lobbyService;
    private final ChestService chestService;
    private final WorldService worldService;
    private final ItemService itemService;


    public RoboGamesCommandHandler(LanguageService languageService, ArenaService arenaService, SpawnService spawnService, GameService gameService, PlayerService playerService, SchedulerService schedulerService, LobbyService lobbyService, ChestService chestService, WorldService worldService, ItemService itemService) {
        this.languageService = languageService;
        this.gameService = gameService;
        this.spawnService = spawnService;
        this.playerService = playerService;
        this.schedulerService = schedulerService;
        this.arenaService = arenaService;
        this.lobbyService = lobbyService;
        this.chestService = chestService;
        this.worldService = worldService;
        this.itemService = itemService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 0) {
            commandSender.sendMessage(this.languageService.getMessage("usage"));
            return false;
        }

        if (!(commandSender instanceof Player player)) {
            return false;
        }

        this.languageService.loadLanguageConfig(player);
        return switch (args[0].toLowerCase()) {
            case "start" -> startGame(player, args[1]);
            case "end" -> endGame(player, args[1]);
            case "join" -> joinGame(player, args[1]);
            case "leave" -> leaveGame(player);
            default -> {
                commandSender.sendMessage(this.languageService.getMessage("unknown-subcommand") + args[0]);
                yield false;
            }
        };
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length != 1) {
            return new ArrayList<>();
        }

        List<String> completions = new ArrayList<>();
        String[] commands = {"join", "leave", "start", "end"};
        for (String cmd : commands) {
            if (!commandSender.hasPermission("robogames.game." + cmd) && !commandSender.hasPermission("robogames.game")) {
                continue;
            }
            completions.add(cmd);
        }
        return completions;
    }

    private boolean endGame(Player player, String arenaName) {
        if (!player.hasPermission("hungergames.game.end")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        if (this.gameService.isGameStarting(arenaName) || !this.gameService.isGameStarted(arenaName)) {
            player.sendMessage(this.languageService.getMessage("endgame.not-started"));
            return true;
        }
        new EndGameCommand(this.gameService, this.spawnService, this.playerService, arenaName).run();
        return true;
    }

    private boolean startGame(Player player, String arenaName) {
        if (!player.hasPermission("hungergames.game.start")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        new StartGameCommand(this.gameService, this.schedulerService, this.languageService, this.playerService, this.spawnService, this.chestService, this.worldService, this.arenaService, this.itemService, arenaName).run();
        return true;
    }

    private boolean leaveGame(Player player) {
        if (!player.hasPermission("hungergames.game.leave")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        new LeaveGameCommand(player, this.languageService, this.gameService, this.playerService, this.spawnService, this.arenaService, this.lobbyService).run();
        return true;
    }

    private boolean joinGame(Player player, String arenaName) {
        if (!player.hasPermission("hungergames.game.join")) {
            player.sendMessage(this.languageService.getMessage("no-permission"));
            return true;
        }
        new JoinGameCommand(player, languageService, gameService, playerService, spawnService, arenaName).run();
        return true;
    }

}
