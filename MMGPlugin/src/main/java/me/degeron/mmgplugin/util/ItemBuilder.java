package me.degeron.mmgplugin.util;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;

    private final ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
    }


    public ItemBuilder setName(String value) {

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', value));
        return this;
    }


    public ItemBuilder setLore(List<String> value) {

        for (int i = 0; i < value.size(); i++) {
            value.set(i, ChatColor.translateAlternateColorCodes('&', value.get(i)));
        }

        itemMeta.setLore(value);
        return this;
    }


    public ItemBuilder addPersistentValue(String key, PersistentDataType persistentDataType, Object value) {

        itemMeta.getPersistentDataContainer().set(NamespacedKey.fromString(key), persistentDataType, value);
        return this;
    }


    public ItemBuilder removePersistentValue(String key) {

        itemMeta.getPersistentDataContainer().remove(NamespacedKey.fromString(key));
        return this;
    }


    public ItemBuilder addEnchant(Enchantment enchantment, int level) {

        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }


    public ItemBuilder removeEnchant(Enchantment enchantment) {

        itemMeta.removeEnchant(enchantment);
        return this;
    }


    public ItemBuilder setCustomModelData(int value) {

        itemMeta.setCustomModelData(value);
        return this;
    }


    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
