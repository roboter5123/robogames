package com.roboter5123.robogames.handler;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.service.GameService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RoboGamesCommandHandler implements CommandExecutor, TabCompleter {

    private final LanguageRepository languageRepository;
    private final GameRepository gameRepository;
    private static final String NO_PERMISSION_MESSAGE_KEY = "no-permission";
    private final GameService gameService;
    private final ArenaRepository arenaRepository;


    public RoboGamesCommandHandler(LanguageRepository languageRepository, GameRepository gameRepository, GameService gameService, ArenaRepository arenaRepository) {
        this.languageRepository = languageRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.arenaRepository = arenaRepository;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 0) {
            commandSender.sendMessage(this.languageRepository.getMessage("usage"));
            return false;
        }

        if (!(commandSender instanceof Player player)) {
            return false;
        }

        this.languageRepository.loadLanguageConfig(player);
        return switch (args[0].toLowerCase()) {
            case "start" -> startGame(player, args[1]);
            case "end" -> endGame(player, args[1]);
            case "join" -> joinGame(player, args[1]);
            case "leave" -> leaveGame(player);
            default -> {
                commandSender.sendMessage(this.languageRepository.getMessage("unknown-subcommand") + args[0]);
                yield false;
            }
        };
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
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

        if (!commandSender.hasPermission("robogames.game." + args[0]) && !commandSender.hasPermission("robogames.game")) {
            return new ArrayList<>();
        }

        if (args.length == 2 && !"leave".equals(args[0])) {
            Set<String> arenaNames = this.arenaRepository.getArenaNames();
            return new ArrayList<>(arenaNames);
        }
        return new ArrayList<>();
    }

    private boolean endGame(Player player, String arenaName) {
        if (!player.hasPermission("hungergames.game.end")) {
            player.sendMessage(this.languageRepository.getMessage(NO_PERMISSION_MESSAGE_KEY));
            return true;
        }
        if (this.gameRepository.isGameStarting(arenaName) || !this.gameRepository.isGameStarted(arenaName)) {
            player.sendMessage(this.languageRepository.getMessage("endgame.not-started"));
            return true;
        }
        this.gameService.endGame(arenaName);
        return true;
    }

    private boolean startGame(Player player, String arenaName) {
        if (!player.hasPermission("hungergames.game.start")) {
            player.sendMessage(this.languageRepository.getMessage(NO_PERMISSION_MESSAGE_KEY));
            return true;
        }
        this.gameService.startGame(arenaName);
        return true;
    }

    private boolean leaveGame(Player player) {
        if (!player.hasPermission("hungergames.game.leave")) {
            player.sendMessage(this.languageRepository.getMessage(NO_PERMISSION_MESSAGE_KEY));
            return true;
        }
        this.gameService.leaveGame(player);
        return true;
    }

    private boolean joinGame(Player player, String arenaName) {
        if (!player.hasPermission("hungergames.game.join")) {
            player.sendMessage(this.languageRepository.getMessage(NO_PERMISSION_MESSAGE_KEY));
            return true;
        }
        this.gameService.joinGame(player, arenaName);
        return true;
    }

}
