package net.jxng1.portaldungeons.commands;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.managers.DungeonManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GenerateRoom implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            DungeonManager dm = PortalDungeons.getInstance().createNewDungeonManager(player.getUniqueId());

            dm.generateRoom(player.getChunk());
        }

        return true;
    }
}
