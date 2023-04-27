package me.degeron.mmgplugin.command;

import me.degeron.mmgplugin.game.Game;
import me.degeron.mmgplugin.menu.Menu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length == 0) {
            Menu.instance.show(player);
        }

        else {
            Game.instance.show(player, Integer.parseInt(strings[0]));
        }
        return true;
    }
}
