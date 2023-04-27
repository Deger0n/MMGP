package me.degeron.mmgplugin.event;

import me.degeron.mmgplugin.game.Game;
import me.degeron.mmgplugin.menu.Menu;
import me.degeron.mmgplugin.menu.SlotTypeMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuEvents implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Menu.instance.viewers.remove((Player) event.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (Menu.instance.viewers.contains((Player) event.getWhoClicked())) {
            event.setCancelled(true);

            SlotTypeMenu slotType = Menu.instance.getSlotType(event.getSlot());

            if (slotType == SlotTypeMenu.BUTTON) {
                Game.instance.show(
                        (Player) event.getWhoClicked(),
                        Menu.instance.levelSlots.indexOf(event.getSlot())
                    );
            }
        }
    }
}
