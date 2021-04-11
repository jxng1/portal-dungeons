package net.jxng1.portaldungeons.commands;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.PortalGenerator;
import net.jxng1.portaldungeons.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildPortal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location playerLocation = player.getLocation().toBlockLocation();
            Location portalBase;
            String playerCardinality = PlayerUtil.getCardinalDirection(player);

            Bukkit.getLogger().info(playerCardinality);

            switch (playerCardinality) { // player facing?
                case "N":
                case "NW":
                case "NE":
                    portalBase = playerLocation.clone().add(0, 0, -2);
                    break;
                case "S":
                case "SW":
                case "SE":
                    portalBase = playerLocation.clone().add(0, 0, 2);
                    break;
                case "E":
                    portalBase = playerLocation.clone().add(2, 0, 0);
                    break;
                case "W":
                    portalBase = playerLocation.clone().add(-2, 0, 0);
                    break;
                default:
                    Bukkit.getLogger().info("Unexpected direction.");
                    return false;
            }

            for (PortalGenerator portal : PortalDungeons.getInstance().getPortalManager().getPortalMapOverworld().keySet()) {
                Location baseLocation = portal.getBaseLocation();
                if ((baseLocation.getBlockX() - 3 <= portalBase.getBlockX() && baseLocation.getBlockX() + 3 >= portalBase.getBlockX()) &&
                        baseLocation.getBlockZ() - 3 <= portalBase.getBlockZ() && baseLocation.getBlockZ() + 3 >= portalBase.getBlockZ()) {
                    player.sendMessage("Portal too close to existing one!");
                    return false;
                }
            }

            PortalDungeons.getInstance().getPortalManager().createPortal(player.getUniqueId(), portalBase, playerCardinality);
            return true;
        } else {
            Bukkit.getLogger().info("Must be called by a player!");
        }

        return false;
    }
}