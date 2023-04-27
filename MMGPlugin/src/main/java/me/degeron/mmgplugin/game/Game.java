package me.degeron.mmgplugin.game;

import me.degeron.mmgplugin.Main;
import me.degeron.mmgplugin.util.DecorCreator;
import me.degeron.mmgplugin.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    public static Game instance = new Game();

    public Map<Player, Integer> viewers = new HashMap<>();

    private final ConfigurationSection levelsSection = Main.getInstance().getConfig().getConfigurationSection("levels");
    private final ConfigurationSection itemsSection = Main.getInstance().getConfig().getConfigurationSection("lvl_items");
    
    private final List<Integer> gameSlots = Main.getInstance().getConfig().getIntegerList("game_zone_slots");



    public void show(Player player, int level) {
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(
                null,
                45,
                levelsSection.getString(level + ".name")
        );


        for (int slot : levelsSection.getIntegerList(level + ".bricks_slots")) {
            inventory.setItem(
                    slot,
                    new ItemBuilder(
                            new ItemStack(Material.valueOf(itemsSection.getString("bricks.material"))))
                            .setName(itemsSection.getString("bricks.name"))
                            .build()
            );
        }

        inventory.setItem(
                itemsSection.getInt("cancel.slot"),
                new ItemBuilder(
                        new ItemStack(
                                Material.valueOf(itemsSection.getString("cancel.material"))
                        )
                )
                        .setName(itemsSection.getString("cancel.name"))
                        .build()
        );

        DecorCreator.create(itemsSection, inventory);

        player.openInventory(inventory);
        viewers.put(player, level);
    }



    public void start(int slot, int level, Inventory inventory) {
        inventory.setItem(
                slot,
                new ItemBuilder(new ItemStack(Material.valueOf(itemsSection.getString("well.material"))))
                        .setName(itemsSection.getString("well.name"))
                        .build()
            );

        spawnPoints(slot, level, inventory);
    }



    public void move(int slot, int level, SlotTypeGame slotType, Inventory inventory) {
        for (int isPointSlot : gameSlots) {
            if (itemsSection.getConfigurationSection("pointers").getKeys(false)
                    .contains(getSlotType(isPointSlot, level, inventory).name().toLowerCase())) {
                inventory.setItem(isPointSlot, new ItemStack(Material.AIR));
            }
        }

        int lastCursorSlot = 0;
        for (int cursorSlot = slot;
             gameSlots.contains(cursorSlot);
             cursorSlot = cursorSlot + itemsSection.getInt("pointers." + slotType.name().toLowerCase() + ".slot")) {

            if (getSlotType(cursorSlot, level, inventory) == SlotTypeGame.CANCEL ||
                    getSlotType(cursorSlot, level, inventory) == SlotTypeGame.OTHER ||
                    getSlotType(cursorSlot, level, inventory) == SlotTypeGame.BRICK) break;

            inventory.setItem(
                    cursorSlot,
                    new ItemBuilder(
                            new ItemStack(Material.valueOf(itemsSection.getString("cursor.material")))
                    )
                            .setName(itemsSection.getString("cursor.name"))
                            .build()
            );
            lastCursorSlot = cursorSlot;
        }
        spawnPoints(lastCursorSlot, level, inventory);


        if (getGameStatus(level, inventory) == GameStatus.FINISH) {
            finish(level, inventory);
        }

        else if (getGameStatus(level, inventory) == GameStatus.FAIL) {
            clear(level, inventory);
        }
    }



    public void finish(int level, Inventory inventory) {
        for (int slot : gameSlots) {
            if (getSlotType(slot, level, inventory) != SlotTypeGame.BRICK) {
                inventory.setItem(
                        slot,
                        new ItemBuilder(new ItemStack(Material.valueOf(itemsSection.getString("well.material"))))
                                .setName(itemsSection.getString("well.name"))
                                .build()
                );
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                show((Player) inventory.getViewers().get(0), level + 1);
            }
        }.runTaskLater(Main.getInstance(), 10L);
    }



    private void clear(int level, Inventory inventory) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int slot : gameSlots) {
                    if (getSlotType(slot, level, inventory) != SlotTypeGame.BRICK) {
                        inventory.setItem(slot, new ItemStack(Material.AIR));
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), 10L);
    }



    private void spawnPoints(int slot, int level, Inventory inventory) {
        for (String pointKey : itemsSection.getConfigurationSection("pointers").getKeys(false)) {
            ConfigurationSection pointItemSection = itemsSection.getConfigurationSection("pointers." + pointKey);
            int pointSlot = slot + pointItemSection.getInt("slot");
            if (!gameSlots.contains(pointSlot)) continue;

            if (getSlotType(pointSlot, level, inventory) == SlotTypeGame.EMPTY) {
                inventory.setItem(
                        pointSlot,
                        new ItemBuilder(
                                new ItemStack(Material.valueOf(pointItemSection.getString("material")))
                        )
                                .setName(pointItemSection.getString("name"))
                                .build()
                );
            }
        }
    }



    public GameStatus getGameStatus(int level, Inventory inventory) {
        for (int isOtherSlot : gameSlots) {

            if (getSlotType(isOtherSlot, level, inventory) == SlotTypeGame.OTHER) {

                for (int isEmptyOrPointSlot : gameSlots) {
                    if (getSlotType(isEmptyOrPointSlot, level, inventory) == SlotTypeGame.EMPTY ||
                            itemsSection.getConfigurationSection("pointers").getKeys(false)
                                    .contains(getSlotType(isEmptyOrPointSlot, level, inventory).name().toLowerCase())) {

                        for (int isPointSlot : gameSlots) {
                            if (itemsSection.getConfigurationSection("pointers").getKeys(false)
                                    .contains(getSlotType(isPointSlot, level, inventory).name().toLowerCase())) {
                                return GameStatus.GAME_GOING;
                            }
                        }
                        return GameStatus.FAIL;
                    }
                }

                return GameStatus.FINISH;
            }
        }
        return GameStatus.START;
    }



    public SlotTypeGame getSlotType(int slot, int level, Inventory inventory) {

        if (levelsSection.getIntegerList(level + ".bricks_slots").contains(slot)) {
            return SlotTypeGame.BRICK;
        }

        else if (inventory.getItem(slot) == null) {
            return SlotTypeGame.EMPTY;
        }

        else if (itemsSection.getInt("cancel.slot") == slot) {
            return SlotTypeGame.CANCEL;
        }

        for (String pointKey : itemsSection.getConfigurationSection("pointers").getKeys(false)) {
            ConfigurationSection pointItemSection = itemsSection.getConfigurationSection("pointers." + pointKey);
            if (inventory.getItem(slot).getType() == Material.valueOf(pointItemSection.getString("material")) &&
                pointItemSection.getString("name").replace("&", "§").equals(inventory.getItem(slot).getItemMeta().getDisplayName())) {
                switch (pointKey) {
                    case "up":
                        return SlotTypeGame.UP;
                    case "right":
                        return SlotTypeGame.RIGHT;
                    case "down":
                        return SlotTypeGame.DOWN;
                    case "left":
                        return SlotTypeGame.LEFT;
                }
            }
        }
        return SlotTypeGame.OTHER;
    }
}