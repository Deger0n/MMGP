package me.degeron.mmgplugin.util;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DecorCreator {
    public static void create(ConfigurationSection section, Inventory inventory) {
        for (String decorKey : section.getConfigurationSection("decor").getKeys(false)) {
            ConfigurationSection decorItemSection = section.getConfigurationSection("decor." + decorKey);

            ItemStack decorItemStack = new ItemBuilder(
                    new ItemStack(Material.valueOf(decorKey))
            )
                    .setName(decorItemSection.getString("name"))
                    .build();

            for (int slot : decorItemSection.getIntegerList("slots")) {
                inventory.setItem(slot, decorItemStack);
            }
        }
    }
}
