package org.infernworld.infernexp.util;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.infernworld.infernexp.InfernExp;

@Getter
public class Menu {
    private final InfernExp plugin;
    private String title;
    private int size;

    public Menu(InfernExp plugin) {
        this.plugin = plugin;
        this.setup();
    }
    private void setup() {
        FileConfiguration menu = plugin.getMenu();

        this.size = menu.getInt("size");
        this.title = menu.getString("title");
    }
}
