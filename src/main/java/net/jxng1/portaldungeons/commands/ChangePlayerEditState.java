package net.jxng1.portaldungeons.commands;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.managers.PlayerEditState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChangePlayerEditState implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerEditState newPlayerEditState;

            if (args.length != 1) {
                player.sendMessage(ChatColor.DARK_RED + "Usage: /edit <enable|disable>");
                return true;
            }

            if (args[0].equalsIgnoreCase("enable")) {
                newPlayerEditState = PlayerEditState.ENABLED;
            } else if (args[0].equalsIgnoreCase("disable")) {
                newPlayerEditState = PlayerEditState.DISABLED;
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Usage: /edit <enable|disable>");
                return true;
            }

            PlayerEditState currentPlayerEditState = PortalDungeons.getInstance().getPlayerManager(player.getUniqueId()).getStructureManager().getPlayerEditState();
            if (currentPlayerEditState == PlayerEditState.ENABLED && newPlayerEditState == PlayerEditState.ENABLED) {
                player.sendMessage(ChatColor.DARK_RED + "You are already in " + ChatColor.BOLD + "enabled" + ChatColor.RESET + ChatColor.DARK_RED + " edit state!");
            } else if (currentPlayerEditState == PlayerEditState.DISABLED && newPlayerEditState == PlayerEditState.DISABLED) {
                player.sendMessage(ChatColor.DARK_RED + "You are already in " + ChatColor.BOLD + "disabled" + ChatColor.RESET + ChatColor.DARK_RED + " edit state!");
            } else {
                PortalDungeons.getInstance().getPlayerManager(player.getUniqueId()).getStructureManager().setPlayerEditState(newPlayerEditState);
                if (newPlayerEditState == PlayerEditState.ENABLED) {
                    player.sendMessage("You are now in " + ChatColor.BOLD + ChatColor.DARK_GREEN + "enabled" + ChatColor.RESET + " edit state!");
                    player.setGameMode(GameMode.CREATIVE);
                } else {
                    player.sendMessage("You are now in " + ChatColor.BOLD + ChatColor.DARK_GREEN + "disabled" + ChatColor.RESET + " edit state!");
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }

            return true;
        }

        return false;
    }
}
