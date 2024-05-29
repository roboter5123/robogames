package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.entity.Player;

import java.util.*;


public class PlayerServiceImpl implements PlayerService {

    private final RoboGames roboGames;

    private final Map<String, List<Player>> alivePlayers;

    private final Map<String, List<Player>> inGamePlayers;

    public PlayerServiceImpl(RoboGames roboGames) {
        this.roboGames = roboGames;
        this.inGamePlayers = new HashMap<>();
        this.alivePlayers = new HashMap<>();
    }


    @Override
    public void clearInGamePlayers(String arenaName) {
        ingamePlayersGuard(arenaName);
        alivePlayersGuard(arenaName);
        this.inGamePlayers.get(arenaName).clear();
        this.alivePlayers.get(arenaName).clear();
    }

    @Override
    public void addInGamePlayer(String arenaName, Player player) {
        ingamePlayersGuard(arenaName);
        alivePlayersGuard(arenaName);
        this.inGamePlayers.get(arenaName).add(player);
        this.alivePlayers.get(arenaName).add(player);
    }

    @Override
    public List<Player> getInGamePlayers(String arenaName) {
        ingamePlayersGuard(arenaName);
        return this.inGamePlayers.get(arenaName);
    }

    @Override
    public List<Player> getAlivePlayers(String arenaName) {
        alivePlayersGuard(arenaName);
        return this.alivePlayers.get(arenaName);
    }

    @Override
    public void removeAlivePlayer(String arenaName, Player player) {
        alivePlayersGuard(arenaName);
        this.alivePlayers.get(arenaName).remove(player);
    }

    @Override
    public void removeIngamePlayer(String arenaName, Player player) {
        ingamePlayersGuard(arenaName);
        alivePlayersGuard(arenaName);
        this.inGamePlayers.get(arenaName).remove(player);
        this.alivePlayers.get(arenaName).remove(player);
    }

    @Override
    public String getArenaNameByPlayer(Player player) {
        // @formatter:off
        return this.inGamePlayers.entrySet().stream()
                .filter(entry -> entry.getValue().contains(player))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        // @formatter:on
    }

    private void ingamePlayersGuard(String arenaName) {
        if (!this.inGamePlayers.containsKey(arenaName)) {
            this.inGamePlayers.put(arenaName, new ArrayList<>());
        }
    }

    private void alivePlayersGuard(String arenaName) {
        if (!this.alivePlayers.containsKey(arenaName)) {
            this.alivePlayers.put(arenaName, new ArrayList<>());
        }
    }
}
