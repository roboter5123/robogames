package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import com.roboter5123.robogames.model.Coordinate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerService {

    private final RoboGames roboGames;

    private final List<Player> inGamePlayers;

    public PlayerService(RoboGames roboGames) {
        this.roboGames = roboGames;
        this.inGamePlayers = new ArrayList<>();
    }

    public List<Player> clearInGamePlayers(){
        List<Player> players = new ArrayList<>(inGamePlayers);
        this.inGamePlayers.clear();
        return players;
    }

    public void addInGamePlayer(Player player){
        this.inGamePlayers.add(player);
    }

    public List<Player> getInGamePlayers(){
        return this.inGamePlayers;
    }

    public List<Player> getOnlinePlayers(){
        Collection<? extends Player> onlinePlayers = roboGames.getServer().getOnlinePlayers();
        return new ArrayList<>(onlinePlayers);
    }

    public void teleportPlayer(Player player, World world, Coordinate coordinate) {
        player.teleport(new Location(world, coordinate.getxCoordinate(), coordinate.getyCoordinate(), coordinate.getzCoordinate()));
    }

    public void removeIngamePlayer(Player player) {
        this.getInGamePlayers().remove(player);
    }
}
