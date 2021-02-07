package net.jxng1.portaldungeons.commands;

import net.jxng1.portaldungeons.PortalDungeons;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RemovePortal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("all")) { // remove all portals
                PortalDungeons.getInstance().getPortalManager().removePortals();
                Bukkit.getLogger().info("All portals have been removed!");
            }
            //TODO: individual portals being removed.
            return true;
        }

        return false;
    }
}
