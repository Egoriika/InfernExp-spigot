package org.infernworld.infernexp.listener;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.infernworld.infernexp.InfernExp;
import org.infernworld.infernexp.inv.Gui;
import org.infernworld.infernexp.util.Menu;

public class Event implements Listener {
    private final InfernExp plugin;
    private final Gui gui;
    private final Menu menu;

    public Event(InfernExp plugin) {
        this.plugin = plugin;
        this.gui = new Gui(plugin);
        this.menu = new Menu(plugin);
    }

    @EventHandler
    public void onInvClickE(InventoryClickEvent e) {
        val player = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();

        if (!e.getView().getTitle().equalsIgnoreCase(menu.getTitle())) {
            return;
        }
        e.setCancelled(true);
        if (e.getClick().isKeyboardClick() || e.getClick().isShiftClick()) {
            return;
        }
        if (e.getSlotType() == InventoryType.SlotType.QUICKBAR) {
            return;
        }
        if (clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER) {
            return;
        }

        val slot = e.getSlot();
        gui.onInvClick(player, slot);
    }

    @EventHandler
    public void onExpBottle(ExpBottleEvent event) {
        ThrownExpBottle bottle = event.getEntity();
        ItemStack item = bottle.getItem();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer nbt = meta.getPersistentDataContainer();
        if (nbt.has(plugin.getKey(), PersistentDataType.INTEGER)) {
            int lvl = nbt.get(plugin.getKey(), PersistentDataType.INTEGER);
            event.setExperience(0);
            Location loc = bottle.getLocation();
            ExperienceOrb orb = loc.getWorld().spawn(loc, ExperienceOrb.class);
            orb.setExperience(lvl);
            orb.getPersistentDataContainer().set(plugin.getKey(), PersistentDataType.INTEGER, lvl);
        }
    }

    @EventHandler
    public void onPlayerPickupExperience(PlayerPickupExperienceEvent event) {
        ExperienceOrb exp = event.getExperienceOrb();
        PersistentDataContainer container = exp .getPersistentDataContainer();
        if (container.has(plugin.getKey(), PersistentDataType.INTEGER)) {
            int lvl = container.get(plugin.getKey(), PersistentDataType.INTEGER);
            event.setCancelled(true);
            exp .remove();
            Player player = event.getPlayer();
            player.setLevel(player.getLevel() + lvl);
        }
    }
}
