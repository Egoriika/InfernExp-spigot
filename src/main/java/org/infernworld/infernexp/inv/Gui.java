package org.infernworld.infernexp.inv;

import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.infernworld.infernexp.InfernExp;
import org.infernworld.infernexp.manager.ExpManager;
import org.infernworld.infernexp.util.Color;
import org.infernworld.infernexp.util.Config;
import org.infernworld.infernexp.util.Menu;
import org.infernworld.infernexp.util.SoundUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Gui {
    private final InfernExp plugin;
    private final Menu menu;
    private final Config cfg;
    private final ExpManager em;

    public Gui(InfernExp plugin) {
        this.plugin = plugin;
        this.menu = new Menu(plugin);
        this.cfg = new Config(plugin);
        this.em = new ExpManager(plugin,cfg);
    }

    public void createInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null,menu.getSize(), menu.getTitle());

        ConfigurationSection items = plugin.getMenu().getConfigurationSection("items");
        for (String key : items.getKeys(false)) {
            ConfigurationSection sec = items.getConfigurationSection(key);
            val material = sec.getString("material");
            Object slot = sec.get("slot");

            List<Integer> slots = new ArrayList<>();
            if (slot instanceof Integer) {
                slots.add(sec.getInt("slot"));
            } else if (slot instanceof List) {
                slots = sec.getIntegerList("slot");
            }

            val name = Color.colorize(sec.getString("name"));
            val count = sec.getInt("count");
            val exp = sec.getInt("exp");
            List<String> lore = sec.getStringList("lore").stream().map(line -> line.replace("%exp", String.valueOf(player.getExpToLevel())))
                    .map(Color::colorize).collect(Collectors.toList());
            ItemStack item = new ItemStack(Material.valueOf(material), count);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
            for (Integer slott : slots) {
                inv.setItem(slott, item);
            }
            player.openInventory(inv);
        }
    }


    public void onInvClick(Player player, int slot, Inventory inv) {
        ConfigurationSection items = plugin.getMenu().getConfigurationSection("items");
        if (items == null) {
            Bukkit.getLogger().info("Секция items в файле menu.yml не найдена!");
            return;
        }
        ItemStack item = inv.getItem(slot);
        if (item == null) return;
        int exp = 0;

        for (String key : items.getKeys(false)) {
            ConfigurationSection sec = items.getConfigurationSection(key);
            if (sec == null) {
                return;
            }
            if (sec.getInt("slot") == slot) {
                exp = sec.getInt("exp");
                if (exp == 11) {
                    if (player.getTotalExperience() >= exp) {
                        if (em.getItem(player)) {
                            player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE));
                            player.giveExp(-exp);
                            SoundUtil.playSound(player, cfg.getSounds());
                        } else {
                            player.sendMessage(cfg.getNoBottle());
                            SoundUtil.playSound(player, cfg.getSoundsNo());
                        }
                    } else {
                        player.sendMessage(cfg.getNoExp());
                        SoundUtil.playSound(player, cfg.getSoundsNo());
                    }
                    return;
                }

                if (player.getLevel() >= exp) {
                    if (em.getItem(player)) {
                        em.createBottleExp(player, exp);
                        SoundUtil.playSound(player, cfg.getSounds());
                    } else {
                        player.sendMessage(cfg.getNoBottle());
                        SoundUtil.playSound(player, cfg.getSoundsNo());
                    }
                } else {
                    player.sendMessage(cfg.getNoExp());
                    SoundUtil.playSound(player, cfg.getSoundsNo());
                }
                return;
            }
        }
    }
}
