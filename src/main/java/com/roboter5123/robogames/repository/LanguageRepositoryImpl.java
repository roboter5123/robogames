package com.roboter5123.robogames.repository;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class LanguageRepositoryImpl implements LanguageRepository {


    private final RoboGames roboGames;
    private YamlConfiguration langConfig;

    public LanguageRepositoryImpl(RoboGames roboGames) {
        this.roboGames = roboGames;
    }

    public String getMessage(String messageKey) {

        if (this.langConfig != null) {
            String message = this.langConfig.getString(messageKey);
            if (message != null) {
                return ChatColor.translateAlternateColorCodes('&', message);
            }
        }
        this.roboGames.getLogger().log(Level.SEVERE, "Message not found for key: " + messageKey);
        return ChatColor.translateAlternateColorCodes('&', "&c Missing translation for key: " + messageKey + ". For more information on how to update language keys, visit: https://github.com/Ayman-Isam/Hunger-Games/wiki/Language#language-errors ");
    }

    public void loadLanguageConfig(Player player) {
        String locale = player.getLocale();
        File langFile = new File(this.roboGames.getDataFolder(), "lang/" + locale + ".yml");
        if (langFile.exists()) {
            langConfig = YamlConfiguration.loadConfiguration(langFile);
        } else {
            loadDefaultLanguageConfig();
        }
    }

    public void loadDefaultLanguageConfig() {
        String defaultLocale = "en_US";
        File langFile = new File(this.roboGames.getDataFolder(), "lang/" + defaultLocale + ".yml");
        if (langFile.exists()) {
            langConfig = YamlConfiguration.loadConfiguration(langFile);
        }
    }

    public void updateLanguageKeys() {
        File langFolder = new File(this.roboGames.getDataFolder(), "lang");
        File[] langFiles = langFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (langFiles == null) {
            return;
        }

        for (File langFile : langFiles) {
            YamlConfiguration pluginLangConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("lang/en_US.yml")));
            this.langConfig = YamlConfiguration.loadConfiguration(langFile);
            boolean updated = false;

            for (String key : pluginLangConfig.getKeys(true)) {
                if (this.langConfig.contains(key)) {
                    continue;
                }
                this.langConfig.set(key, pluginLangConfig.get(key));
                updated = true;

            }
            if (!updated) {
                return;
            }

            try {
                this.langConfig.save(langFile);
            } catch (IOException e) {
                this.roboGames.getServer().getLogger().severe(Arrays.toString(e.getStackTrace()));
            }

        }
    }

    public void saveLanguageFiles(File jarFile) {
        String resourceFolder = "lang";
        File langFolder = new File(this.roboGames.getDataFolder(), resourceFolder);
        if (!langFolder.exists()) {
            boolean dirCreated = langFolder.mkdir();
            if (!dirCreated) {
                this.roboGames.getLogger().log(Level.SEVERE, "Could not create language directory.");
                this.roboGames.getLogger().log(Level.SEVERE, "Please Restart the Server");
                return;
            }
        }

        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(resourceFolder + "/") && entry.getName().endsWith(".yml")) {
                    String fileName = new File(entry.getName()).getName();
                    File langFile = new File(langFolder, fileName);
                    if (!langFile.exists()) {
                        this.roboGames.saveResource(resourceFolder + "/" + fileName, false);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
