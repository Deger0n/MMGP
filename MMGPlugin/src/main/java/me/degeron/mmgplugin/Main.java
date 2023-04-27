package me.degeron.mmgplugin;

import me.degeron.mmgplugin.command.GameCMD;
import me.degeron.mmgplugin.event.GameEvents;
import me.degeron.mmgplugin.event.MenuEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getCommand("mmg").setExecutor(new GameCMD());
        getServer().getPluginManager().registerEvents(new GameEvents(), this);
        getServer().getPluginManager().registerEvents(new MenuEvents(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }
}
