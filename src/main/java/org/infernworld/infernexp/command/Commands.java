package org.infernworld.infernexp.command;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.infernworld.infernexp.InfernExp;
import org.infernworld.infernexp.inv.Gui;
import org.infernworld.infernexp.manager.ExpManager;
import org.infernworld.infernexp.util.Config;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class Commands implements CommandExecutor {
    private final InfernExp plugin;
    private final Config cfg;
    private final ExpManager em;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("infernexp.reload")) {
                player.sendMessage(cfg.getNoPerms());
                return false;
            }

            player.sendMessage(cfg.getReload());
            plugin.reloadConfigs();
            return true;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
            if (!player.hasPermission("infernexp.give")) {
                player.sendMessage(cfg.getNoPerms());
                return false;
            }
            Player p = Bukkit.getPlayerExact(args[1]);
            int exp = Integer.parseInt(args[2]);
            em.createBottleExp(p,exp);
        }

        if (args.length == 0) {
            player.openInventory(new Gui(plugin).getInventory());
            return true;
        }

        return true;
    }
}
