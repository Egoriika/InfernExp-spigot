package org.infernworld.infernexp.manager;

import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.infernworld.infernexp.InfernExp;
import org.infernworld.infernexp.util.Config;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ExpManager {
    private final InfernExp plugin;
    private final Config cfg;

    public boolean getItem(Player player) {
        ItemStack[] item = player.getInventory().getContents();
        for (int i = 0; i < item.length; i++) {
            ItemStack stack = item[i];
            if (stack != null && stack.isSimilar(new ItemStack(Material.GLASS_BOTTLE))) {
                player.getInventory().getItem(i).setAmount(stack.getAmount() - 1);
                return true;
            }
        }
        return false;
    }

    private void giveExp(Player player, ItemStack item) {
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getStorageContents().length; i++) {
            ItemStack stack = inv.getItem(i);
            if (stack == null || stack.getType().isEmpty()) {
                player.getInventory().addItem(item);
                return;
            }
        }
        player.getWorld().dropItem(player.getLocation(), item);
    }

    public void createBottleExp(Player player, int exp) {
        val material = cfg.getMaterial();
        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(cfg.getName().replace("%lvl", String.valueOf(exp)));
        List<String> lore = cfg.getLore().stream().map(str -> str.replace("%lvl", String.valueOf(exp))).collect(Collectors.toList());
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(plugin.getKey(), PersistentDataType.INTEGER, exp);
        item.setItemMeta(meta);
        giveExp(player, item);

        if (player.hasPermission("infernexp.give")) return;
        player.setLevel(player.getLevel() - exp);
    }

    public int calculateExp(int level) {
        if (level <= 15) return level * level + 6 * level;
        if (level <= 30) return (int) (2.5D * level * level - 40.5D * level + 360);
        return (int) (4.5D * level * level - 162.5D * level + 2220);
    }
}
