package com.badbones69.crazyenchantments.api.managers;

import com.badbones69.crazyenchantments.Methods;
import com.badbones69.crazyenchantments.api.CrazyManager;
import com.badbones69.crazyenchantments.api.FileManager.Files;
import com.badbones69.crazyenchantments.api.economy.Currency;
import com.badbones69.crazyenchantments.api.economy.CurrencyAPI;
import com.badbones69.crazyenchantments.api.enums.ShopOption;
import com.badbones69.crazyenchantments.api.objects.Category;
import com.badbones69.crazyenchantments.api.objects.ItemBuilder;
import com.badbones69.crazyenchantments.api.objects.LostBook;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.HashMap;
import java.util.Map.Entry;

public class ShopManager {
    
    private final CrazyManager ce = CrazyManager.getInstance();
    private static final ShopManager instance = new ShopManager();
    private String inventoryName;
    private int inventorySize;
    private boolean enchantmentTableShop;
    private final HashMap<ItemBuilder, Integer> customizerItems = new HashMap<>();
    private final HashMap<ItemBuilder, Integer> shopItems = new HashMap<>();
    
    public static ShopManager getInstance() {
        return instance;
    }
    
    public void load() {
        customizerItems.clear();
        shopItems.clear();
        FileConfiguration config = Files.CONFIG.getFile();
        inventoryName = Methods.color(config.getString("Settings.InvName"));
        inventorySize = config.getInt("Settings.GUISize");
        enchantmentTableShop = config.getBoolean("Settings.EnchantmentOptions.Right-Click-Enchantment-Table");

        for (String customItemString : config.getStringList("Settings.GUICustomization")) {
            int slot = 0;

            for (String option : customItemString.split(", ")) {
                if (option.contains("Slot:")) {
                    option = option.replace("Slot:", "");
                    slot = Integer.parseInt(option);
                    break;
                }
            }

            if (slot > inventorySize || slot <= 0) {
                continue;
            }

            slot--;
            customizerItems.put(ItemBuilder.convertString(customItemString), slot);
        }

        for (Category category : ce.getCategories()) {

            if (category.isInGUI()) {
                if (category.getSlot() > inventorySize) {
                    continue;
                }
                shopItems.put(category.getDisplayItem(), category.getSlot());
            }

            LostBook lostBook = category.getLostBook();

            if (lostBook.isInGUI()) {
                if (lostBook.getSlot() > inventorySize) {
                    continue;
                }
                shopItems.put(lostBook.getDisplayItem(), lostBook.getSlot());
            }
        }

        for (ShopOption option : ShopOption.values()) {
            if (option.isInGUI()) {
                if (option.getSlot() > inventorySize) {
                    continue;
                }
                shopItems.put(option.getItemBuilder(), option.getSlot());
            }
        }
    }
    
    public Inventory getShopInventory(Player player) {
        HashMap<String, String> placeholders = new HashMap<>();

        for (Currency currency : Currency.values()) {
            placeholders.put("%" + currency.getName() + "%", CurrencyAPI.getCurrency(player, currency) + "");
        }

        Inventory inventory = ce.getPlugin().getServer().createInventory(null, inventorySize, inventoryName);

        for (Entry<ItemBuilder, Integer> itemBuilders : customizerItems.entrySet()) {
            itemBuilders.getKey().setNamePlaceholders(placeholders)
            .setLorePlaceholders(placeholders);
            inventory.setItem(itemBuilders.getValue(), itemBuilders.getKey().build());
        }

        shopItems.keySet().forEach(itemBuilder -> inventory.setItem(shopItems.get(itemBuilder), itemBuilder.build()));
        return inventory;
    }
    
    public String getInventoryName() {
        return inventoryName;
    }
    
    public int getInventorySize() {
        return inventorySize;
    }
    
    public boolean isEnchantmentTableShop() {
        return enchantmentTableShop;
    }
}