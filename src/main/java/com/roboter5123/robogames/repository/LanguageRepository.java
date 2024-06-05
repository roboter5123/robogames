package com.roboter5123.robogames.repository;

import org.bukkit.entity.Player;

import java.io.File;

public interface LanguageRepository {

    public String getMessage(String messageKey);

    public void loadLanguageConfig(Player player);

    public void loadDefaultLanguageConfig();

    public void updateLanguageKeys();

    public void saveLanguageFiles(File jarFile);
}
