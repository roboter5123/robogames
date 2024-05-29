package com.roboter5123.robogames.service;

import org.bukkit.Location;

import java.io.IOException;

public interface LobbyService {

    void loadLobbiesConfig();

    Location getLobby(String arenaName);

    void createLobby(String lobbyName, Location lobby) throws IOException;
}
