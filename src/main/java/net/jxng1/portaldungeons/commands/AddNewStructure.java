package net.jxng1.portaldungeons.commands;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.managers.StructureManager;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddNewStructure implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String structureName;
            StructureManager structureManager = PortalDungeons.getInstance().getPlayerManager(player.getUniqueId()).getStructureManager();

            if (args.length != 2) {
                player.sendMessage(ChatColor.DARK_RED + "Usage: /structure <save|__> <structure name>");
                return true;
            } else if (structureManager == null || structureManager.isEmpty() || args[1].equalsIgnoreCase("")) {
                player.sendMessage("Make sure you've used " + ChatColor.GOLD + "" + ChatColor.BOLD + "/edit enable " +
                        ChatColor.RESET + "and started building!");
                return true;
            }

            if (args[0].equalsIgnoreCase("save")) {
                structureName = args[1];
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Usage: /structure <save|__> <structure name>");
                return true;
            }

            if (PortalDungeons.getInstance().getFileManager().isStructureFileExist(structureName)) {
                player.sendMessage(ChatColor.DARK_RED + "The structure already exists, choose a different name!");
                return true;
            } else {
                try {
                    PortalDungeons.getInstance().getFileManager().saveNewStructureFile(
                            player,
                            structureManager,
                            structureName,
                            null);
                    player.sendMessage(ChatColor.GREEN + "The structure " + ChatColor.BOLD + ChatColor.GOLD + structureName + ChatColor.GREEN + " has been added!");
                } catch (NullPointerException e) {
                    player.sendMessage(ChatColor.DARK_RED + "Error with file save.");
                    Bukkit.getLogger().warning("Error with file save." + e);
                }
            }

            return true;
        }

        return false;
    }
}
