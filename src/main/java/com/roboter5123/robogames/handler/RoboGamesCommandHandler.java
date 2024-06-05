package com.roboter5123.robogames.handler;

import com.roboter5123.robogames.repository.ArenaRepository;
import com.roboter5123.robogames.repository.ChestRepository;
import com.roboter5123.robogames.repository.ConfigRepository;
import com.roboter5123.robogames.repository.GameRepository;
import com.roboter5123.robogames.repository.ItemRepository;
import com.roboter5123.robogames.repository.LanguageRepository;
import com.roboter5123.robogames.repository.LobbyRepository;
import com.roboter5123.robogames.repository.PlayerRepository;
import com.roboter5123.robogames.repository.SchedulerRepository;
import com.roboter5123.robogames.repository.SpawnRepository;
import com.roboter5123.robogames.tasks.command.EndGameCommand;
import com.roboter5123.robogames.tasks.command.JoinGameCommand;
import com.roboter5123.robogames.tasks.command.LeaveGameCommand;
import com.roboter5123.robogames.tasks.command.StartGameCommand;

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

    private final LanguageRepository languageRepository;
    private final GameRepository gameRepository;
    private final SpawnRepository spawnRepository;
    private final PlayerRepository playerRepository;
    private final SchedulerRepository schedulerRepository;
    private final ArenaRepository arenaRepository;
    private final LobbyRepository lobbyRepository;
    private final ChestRepository chestRepository;
    private final ItemRepository itemRepository;
    private final ConfigRepository configRepository;
    private static final String NO_PERMISSION_MESSAGE_KEY = "no-permission";


    public RoboGamesCommandHandler(LanguageRepository languageRepository, ArenaRepository arenaRepository, SpawnRepository spawnRepository, GameRepository gameRepository, PlayerRepository playerRepository, SchedulerRepository schedulerRepository, LobbyRepository lobbyRepository, ChestRepository chestRepository, ItemRepository itemRepository, ConfigRepository configRepository) {
        this.languageRepository = languageRepository;
        this.gameRepository = gameRepository;
        this.spawnRepository = spawnRepository;
        this.playerRepository = playerRepository;
        this.schedulerRepository = schedulerRepository;
        this.arenaRepository = arenaRepository;
        this.lobbyRepository = lobbyRepository;
        this.chestRepository = chestRepository;
        this.itemRepository = itemRepository;
        this.configRepository = configRepository;
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
            player.sendMessage(this.languageRepository.getMessage(NO_PERMISSION_MESSAGE_KEY));
            return true;
        }
        if (this.gameRepository.isGameStarting(arenaName) || !this.gameRepository.isGameStarted(arenaName)) {
            player.sendMessage(this.languageRepository.getMessage("endgame.not-started"));
            return true;
        }
        new EndGameCommand(this.gameRepository, this.spawnRepository, this.playerRepository, arenaName).run();
        return true;
    }

    private boolean startGame(Player player, String arenaName) {
        if (!player.hasPermission("hungergames.game.start")) {
            player.sendMessage(this.languageRepository.getMessage(NO_PERMISSION_MESSAGE_KEY));
            return true;
        }
        new StartGameCommand(this.gameRepository, this.schedulerRepository, this.languageRepository, this.playerRepository, this.spawnRepository, this.chestRepository, this.arenaRepository, this.itemRepository, arenaName, this.configRepository).run();
        return true;
    }

    private boolean leaveGame(Player player) {
        if (!player.hasPermission("hungergames.game.leave")) {
            player.sendMessage(this.languageRepository.getMessage(NO_PERMISSION_MESSAGE_KEY));
            return true;
        }
        new LeaveGameCommand(player, this.languageRepository, this.gameRepository, this.playerRepository, this.spawnRepository, this.arenaRepository, this.lobbyRepository).run();
        return true;
    }

    private boolean joinGame(Player player, String arenaName) {
        if (!player.hasPermission("hungergames.game.join")) {
            player.sendMessage(this.languageRepository.getMessage(NO_PERMISSION_MESSAGE_KEY));
            return true;
        }
        new JoinGameCommand(player, languageRepository, gameRepository, playerRepository, spawnRepository, arenaName).run();
        return true;
    }

}
