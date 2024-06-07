package com.roboter5123.robogames.repository;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;


public class PlayerRepositoryImpl implements PlayerRepository {

    private final RoboGames roboGames;

    private final Map<String, List<Player>> alivePlayers;

    private final Map<String, List<Player>> inGamePlayers;

    public PlayerRepositoryImpl(RoboGames roboGames) {
        this.roboGames = roboGames;
        this.inGamePlayers = new HashMap<>();
        this.alivePlayers = new HashMap<>();
    }


    @Override
    public void removeAllIngamePlayersByArenaName(String arenaName) {
        ingamePlayersGuard(arenaName);
        alivePlayersGuard(arenaName);
        this.inGamePlayers.get(arenaName).clear();
        this.alivePlayers.get(arenaName).clear();
    }

    @Override
    public void createIngamePlayer(String arenaName, Player player) {
        ingamePlayersGuard(arenaName);
        alivePlayersGuard(arenaName);
        this.inGamePlayers.get(arenaName).add(player);
        this.alivePlayers.get(arenaName).add(player);
    }

    @Override
    public List<Player> getInGamePlayersByArenaName(String arenaName) {
        ingamePlayersGuard(arenaName);
        return this.inGamePlayers.get(arenaName);
    }

    @Override
    public List<Player> getAlivePlayersByArenaName(String arenaName) {
        alivePlayersGuard(arenaName);
        return this.alivePlayers.get(arenaName);
    }

    @Override
    public void removeAlivePlayerByArenaName(String arenaName, Player player) {
        alivePlayersGuard(arenaName);
        this.alivePlayers.get(arenaName).remove(player);
    }

    @Override
    public void removeIngamePlayerByArenaName(String arenaName, Player player) {
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

    public void setMetaDataOnPlayer(Player player, String key, Object value) {
        player.setMetadata(key, new FixedMetadataValue(roboGames, value));
    }

    private void ingamePlayersGuard(String arenaName) {
        this.inGamePlayers.computeIfAbsent(arenaName, k -> new ArrayList<>());
    }

    private void alivePlayersGuard(String arenaName) {
        this.alivePlayers.computeIfAbsent(arenaName, k -> new ArrayList<>());
    }
}