package org.infernworld.infernexp;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.infernworld.infernexp.command.Commands;
import org.infernworld.infernexp.listener.Event;
import org.infernworld.infernexp.manager.ExpManager;
import org.infernworld.infernexp.util.Config;

import java.io.File;

@Getter
public final class InfernExp extends JavaPlugin {
    private YamlConfiguration config;
    private YamlConfiguration menu;
    private NamespacedKey key;
    private ExpManager em;
    private Config cfg;

    @Override
    public void onEnable() {
        this.setupCfg();
        this.cfg = new Config(this);
        PluginCommand command = getCommand("exp");
        Bukkit.getPluginManager().registerEvents(new Event(this),this);
        this.key = new NamespacedKey(this,"IWexp");
        this.em = new ExpManager(this,cfg);
        command.setExecutor(new Commands(this,cfg, em));
    }

    private void setupCfg() {
        final File cfg = new File(getDataFolder(), "config.yml");
        if (!cfg.exists()) {
            saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(cfg);
        final File menu = new File(getDataFolder(),"menu.yml");
        if (!menu.exists()) {
            saveResource("menu.yml", false);
        }
        this.menu = YamlConfiguration.loadConfiguration(menu);
    }
    
    public void reloadConfigs() {
        reloadConfig();
        final File cfg = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(cfg);

        final File menu = new File(getDataFolder(), "menu.yml");
        this.menu = YamlConfiguration.loadConfiguration(menu);

        getLogger().info("кфг перезагружен");
    }
}

