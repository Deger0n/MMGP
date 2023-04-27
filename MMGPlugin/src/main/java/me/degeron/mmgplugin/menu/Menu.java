package me.degeron.mmgplugin.menu;

import me.degeron.mmgplugin.Main;
import me.degeron.mmgplugin.util.DecorCreator;
import me.degeron.mmgplugin.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static Menu instance = new Menu();

    public List<Player> viewers = new ArrayList<>();

    private final ConfigurationSection menuSection = Main.getInstance().getConfig().getConfigurationSection("menu_items");
    private final ConfigurationSection levelSection = Main.getInstance().getConfig().getConfigurationSection("levels");

    public final List<Integer> levelSlots = menuSection.getIntegerList("level_slots");


    public void show(Player player) {
        Inventory inventory = Bukkit.createInventory(
                null,
                45,
                menuSection.getString("menu_name")
        );


        for (int slot : levelSlots) {
            String numberLvl = Integer.toString(levelSlots.indexOf(slot));
            if (!levelSection.getKeys(false).contains(numberLvl)) break;
            inventory.setItem(
                    slot,
                    new ItemBuilder(
                            new ItemStack(Material.valueOf(levelSection.getString(numberLvl + ".material")))
                    )
                            .setName(levelSection.getString(numberLvl + ".name"))
                            .build()
            );
        }

        DecorCreator.create(menuSection, inventory);

        player.closeInventory();
        player.openInventory(inventory);
        viewers.add(player);
    }

    public SlotTypeMenu getSlotType(int slot) {
        if (levelSlots.contains(slot)) {
            return SlotTypeMenu.BUTTON;
        }
        return SlotTypeMenu.OTHER;
    }
}
