package com.badbones69.crazyenchantments.controllers;

import com.badbones69.crazyenchantments.api.CrazyManager;
import com.badbones69.crazyenchantments.api.managers.InfoMenuManager;
import com.badbones69.crazyenchantments.api.objects.EnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InfoGUIControl implements Listener {
    
    private final CrazyManager ce = CrazyManager.getInstance();
    private final InfoMenuManager manager = ce.getInfoMenuManager();
    
    @EventHandler
    public void infoClick(InventoryClickEvent e) {
        if (e.getInventory() != null && e.getView().getTitle().equals(manager.getInventoryName())) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                ItemStack item = e.getCurrentItem();
                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    Player player = (Player) e.getWhoClicked();

                    if (item.isSimilar(manager.getBackLeftButton()) || item.isSimilar(manager.getBackRightButton())) {
                        manager.openInfoMenu(player);
                        return;
                    }

                    for (EnchantmentType enchantmentType : manager.getEnchantmentTypes()) {
                        if (item.isSimilar(enchantmentType.getDisplayItem())) {
                            manager.openInfoMenu(player, enchantmentType);
                            return;
                        }
                    }
                }
            }
        }
    }
}