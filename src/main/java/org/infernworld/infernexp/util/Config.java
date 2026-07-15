package org.infernworld.infernexp.util;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.infernworld.infernexp.InfernExp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Config {
    private final InfernExp plugin;
    private String material,
            name,
            noExp,
            reload,
            noPerms,
            noBottle,
            sound,
            soundNo;
    private Sound sounds, soundsNo;
    private List<String> lore = new ArrayList<>();

    public Config(InfernExp plugin) {
        this.plugin = plugin;
        this.setup();
    }

    private void setup() {
        FileConfiguration cfg = plugin.getConfig();
        ConfigurationSection item = cfg.getConfigurationSection("item");
        ConfigurationSection msg = cfg.getConfigurationSection("message");
        ConfigurationSection settings = cfg.getConfigurationSection("settings");

        if (msg == null || item == null || settings == null) return;

        this.material = item.getString("material");
        this.name = Color.colorize(item.getString("name", "Опытный пузырёк"));
        this.lore = item.getStringList("lore").stream().map(Color::colorize).collect(Collectors.toList());
        this.noBottle = Color.colorize(msg.getString("no-botlle"));
        this.noExp = Color.colorize(msg.getString("no-exp" ));
        this.noPerms = Color.colorize(msg.getString("no-perms"));
        this.reload = Color.colorize(msg.getString("reload"));
        this.sound = settings.getString("sound-exp");
        this.soundNo = settings.getString("sound-no");
        if (this.sound != null) {
            try {
                this.sounds = Sound.valueOf(this.sound);
            } catch (IllegalArgumentException e) {
                this.sounds = null;
                Bukkit.getLogger().warning("Звук " + this.sounds + " не найден. Проверьте конфиг!");
            }
        }
        if (this.soundNo != null) {
            try {
                this.soundsNo = Sound.valueOf(this.soundNo);
            } catch (IllegalArgumentException e) {
                this.soundsNo = null;
                Bukkit.getLogger().warning("Звук " + this.soundsNo + " не найден. Проверьте конфиг!");
            }
        }
    }
}
