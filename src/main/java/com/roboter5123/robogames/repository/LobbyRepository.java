package com.roboter5123.robogames.repository;

import org.bukkit.Location;

import java.io.IOException;

public interface LobbyRepository {

    void loadLobbiesConfig();

    Location getLobby(String arenaName);

    void createLobby(String lobbyName, Location lobby) throws IOException;
}
