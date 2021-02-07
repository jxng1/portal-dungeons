package net.jxng1.portaldungeons.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportToDimension implements CommandExecutor {

    World dimension;

    public TeleportToDimension(World dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.teleport(new Location(dimension, 0, dimension.getHighestBlockYAt(0, 0), 0));
        }

        return true;
    }
}
