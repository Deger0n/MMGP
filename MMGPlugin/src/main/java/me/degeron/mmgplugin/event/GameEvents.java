package me.degeron.mmgplugin.event;

import me.degeron.mmgplugin.game.Game;
import me.degeron.mmgplugin.game.GameStatus;
import me.degeron.mmgplugin.game.SlotTypeGame;
import me.degeron.mmgplugin.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class GameEvents implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Game.instance.viewers.remove((Player) event.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (Game.instance.viewers.containsKey((Player) event.getWhoClicked())) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();

            int level = Game.instance.viewers.get(player);
            Inventory inventory = event.getInventory();
            SlotTypeGame slotType = Game.instance.getSlotType(event.getSlot(), level, inventory);
            GameStatus gameStatus = Game.instance.getGameStatus(level, inventory);


            if (gameStatus == GameStatus.START && slotType == SlotTypeGame.EMPTY) {
                Game.instance.start(event.getSlot(), level, inventory);
            }

            else if (gameStatus == GameStatus.GAME_GOING &&
                    (slotType == SlotTypeGame.UP || slotType == SlotTypeGame.RIGHT ||
                    slotType == SlotTypeGame.DOWN || slotType == SlotTypeGame.LEFT)) {

                Game.instance.move(event.getSlot(), level, slotType, inventory);
            }

            else if (slotType == SlotTypeGame.CANCEL) {
                player.closeInventory();
                Menu.instance.show(player);
            }
        }
    }
}
