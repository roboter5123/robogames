package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PlayerService {

    private final RoboGames roboGames;

    private final List<Player> alivePlayers;

    private final List<Player> inGamePlayers;

    public PlayerService(RoboGames roboGames) {
        this.roboGames = roboGames;
        this.inGamePlayers = new ArrayList<>();
        this.alivePlayers = new ArrayList<>();
    }

    public List<Player> clearInGamePlayers() {
        List<Player> players = new ArrayList<>(inGamePlayers);
        this.inGamePlayers.clear();
        this.alivePlayers.clear();
        return players;
    }

    public void addInGamePlayer(Player player) {
        this.inGamePlayers.add(player);
        this.alivePlayers.add(player);
    }

    public List<Player> getInGamePlayers() {
        return this.inGamePlayers;
    }

    public List<Player> getAlivePlayers() {
        return this.inGamePlayers;
    }

    public List<Player> getOnlinePlayers() {
        Collection<? extends Player> onlinePlayers = roboGames.getServer().getOnlinePlayers();
        return new ArrayList<>(onlinePlayers);
    }

    public void removeAlivePlayers(Player player) {
        this.alivePlayers.remove(player);
    }

    public void teleportPlayer(Player player, Location location) {
        player.teleport(location);
    }

    public void removeIngamePlayer(Player player) {
        this.inGamePlayers.remove(player);
        this.alivePlayers.remove(player);

    }
}
