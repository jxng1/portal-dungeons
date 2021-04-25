package net.jxng1.portaldungeons.listeners;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.managers.PlayerEditState;
import net.jxng1.portaldungeons.managers.StructureManager;
import net.jxng1.portaldungeons.dungeonstructures.EditBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        StructureManager sm = PortalDungeons.getInstance().getPlayerManager(player.getUniqueId()).getStructureManager();

        if (sm == null || sm.getPlayerEditState() == PlayerEditState.DISABLED) { // player has no StructureManager or isn't in enabled edit state, don't track...
            //player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You're not in enabled edit state yet!");
            return;
        }

        // For now, designated 'jigsaw' block is bedrock, but can be configured by a config file in the future.
        Block blockPlaced = event.getBlockPlaced();

        // If baseBlock not yet placed, then set the base block if it's BEDROCK.
        if (blockPlaced.getType() == Material.BEDROCK) {
            if (sm.numberOfConnectorsPlaced() == 4) { // 4 CONNECTORS
                player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Max connector blocks of 4, you can't add anymore!");
                sm.getConnectorBlocks().remove(event.getBlockPlaced());
                event.setCancelled(true);
                return;
            } else if (sm.numberOfConnectorsPlaced() == 0) { // FIRST CONNECTOR BLOCK
                sm.addConnectorBlock(blockPlaced);
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You've now placed the first connector block, tracking started.");
            } else if (sm.numberOfConnectorsPlaced() < 4) { // NOT 4 CONNECTORS YET
                sm.addConnectorBlock(blockPlaced);
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You've now placed " + sm.numberOfConnectorsPlaced() + " connector blocks.");

            }
            player.sendMessage(ChatColor.GREEN + "Connector block placed at: " +
                    "x : " + blockPlaced.getX() +
                    " y : " + blockPlaced.getY() +
                    " z : " + blockPlaced.getZ());
        } else if (sm.numberOfConnectorsPlaced() == 0) {
            player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You haven't placed a connector block yet!");
            event.setCancelled(true);
        } else { // ANY OTHER BLOCK
            Block baseConnector = sm.getBaseConnector(); // GET BASE CONNECTOR

            sm.addEditBlock(blockPlaced, blockPlaced.getX() - baseConnector.getX(),
                    blockPlaced.getY() - baseConnector.getY(),
                    blockPlaced.getZ() - baseConnector.getZ(),
                    blockPlaced.getType());

            player.sendMessage(ChatColor.GREEN + "Block placed at: " +
                    "x : " + blockPlaced.getX() +
                    " y : " + blockPlaced.getY() +
                    " z : " + blockPlaced.getZ() +
                    " Blocks placed currently: " + sm.getBlocksPlaced().size());
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        StructureManager sm = PortalDungeons.getInstance().getPlayerManager(player.getUniqueId()).getStructureManager();

        if (sm == null || sm.getPlayerEditState() == PlayerEditState.DISABLED) { // player has no StructureManager or isn't in enabled edit state, don't track...
            //player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You're not in enabled edit state yet!");
            return;
        }

        Block blockDestroyed = event.getBlock();

        if (blockDestroyed.getType() == Material.BEDROCK && sm.numberOfConnectorsPlaced() != 0) {
            if (sm.getBaseConnector().equals(blockDestroyed)) { // CONNECTOR DESTROYED IS THE BASE ONE
                if (sm.numberOfConnectorsPlaced() == 1) { // IF THE BASE CONNECTOR IS THE ONLY ONE LEFT
                    player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "No other connector block available, please set another first!");
                    event.setCancelled(true);
                    return;
                }

                sm.getConnectorBlocks().remove(blockDestroyed); // REMOVE BASE CONNECTOR FROM SET

                Block newBaseConnector = sm.getBaseConnector(); // TAKE NEW BASE CONNECTOR
                for (EditBlock block : sm.getBlocksPlaced()) { // UPDATE DISTANCES FROM NEW BASE CONNECTOR FOR EACH EditBlock
                    block.setXDistanceFromBase(block.getBlock().getX() - newBaseConnector.getX());
                    block.setYDistanceFromBase(block.getBlock().getY() - newBaseConnector.getY());
                    block.setZDistanceFromBase(block.getBlock().getZ() - newBaseConnector.getZ());
                }
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You've removed a base connector block(first connector you placed).");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "The new base connector is the next connector you placed from the previous.");
            } else if (!sm.getConnectorBlocks().contains(blockDestroyed)) { // CONNECTOR ISN'T THIS PLAYER'S
                player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Not your connector block!");
                event.setCancelled(true);
            } else { // CONNECTOR DESTROYED IS ANOTHER IN THE SET
                sm.getConnectorBlocks().remove(blockDestroyed);
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You've removed a connector block.");
            }
        } else {
            sm.removeEditBlock(blockDestroyed);
            player.sendMessage(ChatColor.RED + "Block removed at: " +
                    "x : " + blockDestroyed.getX() +
                    " y : " + blockDestroyed.getY() +
                    " z : " + blockDestroyed.getZ());

        }
    }
}
