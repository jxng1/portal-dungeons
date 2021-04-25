package net.jxng1.portaldungeons.listeners;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.PortalGenerator;
import net.jxng1.portaldungeons.generators.RoomGenerator;
import net.jxng1.portaldungeons.managers.DungeonManager;
import net.jxng1.portaldungeons.managers.PlayerManager;
import net.jxng1.portaldungeons.managers.PortalManager;
import net.jxng1.portaldungeons.managers.PortalSpawnState;
import net.jxng1.portaldungeons.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalListener implements Listener {

    @EventHandler
    public void onPlayerInteractWithOverworldPortal(PlayerMoveEvent event) {
        PortalDungeons plugin = PortalDungeons.getInstance();
        Player player = event.getPlayer();
        PlayerManager pm = plugin.getPlayerManager(player.getUniqueId());
        String playerCardinality = PlayerUtil.getCardinalDirection(player);

        if (player.getWorld() != plugin.baseWorld) {
            return;
        }

        PortalGenerator overworldPortal;
        // If player has a portal in the oveworld...
        if ((overworldPortal = plugin.getPortalManager().getPortal(player.getUniqueId(),
                plugin.getPortalManager().getPortalMapOverworld())) != null) {

            // If player actually steps on the base block of said portal...
            if (overworldPortal.getBaseLocation().getBlockX() == player.getLocation().getBlockX() // Player steps on portal base...
                    && overworldPortal.getBaseLocation().getBlockZ() == player.getLocation().getBlockZ()
                    && overworldPortal.getBaseLocation().getBlockY() + 1 == player.getLocation().getBlockY()) {

                DungeonManager dm;

                // TODO
                // - Make the code below into a function as part of the DungeonManager

                // If player doesn't have a DungeonManager, create one for them and set-up dungeon...
                if ((dm = plugin.getDungeonManager(player.getUniqueId())) == null) {
                    dm = plugin.createNewDungeonManager(player.getUniqueId());
                    dm.setOverworldPortal(overworldPortal);

                    // Generate the dungeon start portal...
                    PortalGenerator startPortal = dm.createDungeonStartPortal(player.getUniqueId());
                    dm.generateDungeon(startPortal.getBaseLocation().getChunk());
                    startPortal.buildPortal(playerCardinality);

                    // Generate the dungeon end portal...
                    PortalGenerator endPortal = dm.createDungeonEndPortal(player.getUniqueId());
                    endPortal.buildPortal(playerCardinality);
                }

                player.teleport(new Location(plugin.dungeonWorld,
                        dm.getStartPortal().getBaseLocation().getBlockX() + 2,
                        RoomGenerator.ROOM_HEIGHT + 1,
                        dm.getStartPortal().getBaseLocation().getBlockZ() + 2));
                player.sendMessage(ChatColor.DARK_RED + "Portal spawn has been disabled for you!");

                pm.cancelPortalSpawnTask();
                pm.setPortalSpawnState(PortalSpawnState.DISABLED);
                pm.sendDungeonEnterMessage(); // BETTER IMPLEMENTED ELSEWHERE OF PlayerManager?...
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithDungeonPortal(PlayerMoveEvent event) {
        PortalDungeons plugin = PortalDungeons.getInstance();
        Player player = event.getPlayer();
        PortalManager plM = plugin.getPortalManager();
        PlayerManager pm = plugin.getPlayerManager(player.getUniqueId());

        if (player.getWorld() != plugin.dungeonWorld) {
            return;
        }

        DungeonManager dm = plugin.getDungeonManager(player.getUniqueId());

        // Player doesn't have a dm, might be admin etc...
        if (dm == null) {
            return;
        }

        // Is dungeon start portal or dungeon end portal...
        if (dm.getStartPortal().getBaseLocation().getBlockX() == player.getLocation().getBlockX()
                && dm.getStartPortal().getBaseLocation().getBlockZ() == player.getLocation().getBlockZ()
                && dm.getStartPortal().getBaseLocation().getBlockY() + 1 == player.getLocation().getBlockY()) {
            // Player exit dungeon prematurely...
            // TODO
            // - If player leaves prematurely, remove their looted items and xp.
        } else if (dm.getEndPortal().getBaseLocation().getBlockX() == player.getLocation().getBlockX()
                && dm.getEndPortal().getBaseLocation().getBlockZ() == player.getLocation().getBlockZ()
                && dm.getEndPortal().getBaseLocation().getBlockY() + 1 == player.getLocation().getBlockY()) {
            // Player reached the end...
            // TODO
            // - Player stats in config files updated.
        } else {
            // Portal isn't from this DungeonManager...
            return;
        }

        player.teleport(new Location(plugin.baseWorld,
                dm.getOverworldPortal().getBaseLocation().getBlockX() + 2,
                dm.getOverworldPortal().getBaseLocation().getBlockY() + 1,
                dm.getOverworldPortal().getBaseLocation().getBlockZ() + 2));
        player.sendMessage(ChatColor.DARK_GREEN + "Portal spawn has been enabled for you!");
        // pm.sendDungeonExitMessage() ???

        pm.cancelPortalSpawnTask();
        pm.setPortalSpawnState(PortalSpawnState.DISALLOWED);

        // Removing portals...
        plM.removePortal(dm.getOverworldPortal(), plM.getPortalMapOverworld());
        plM.removePortal(dm.getStartPortal(), plM.getPortalMapDungeonWorld());
        plM.removePortal(dm.getEndPortal(), plM.getPortalMapDungeonWorld());

        // Removing dungeon...
        dm.cleanup();

        // Removing DungeonManager...
        plugin.removeDungeonManager(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) { // chance to spawn portal surrounding them

        Player player = event.getPlayer();
        PlayerManager playerManager = PortalDungeons.getInstance().getPlayerManager(player.getUniqueId());

        if (player.getWorld() == PortalDungeons.getInstance().baseWorld && // in overworld
                //player.getLocation().getY() <= 50 && // player y <= 50
                playerManager.getPortalSpawnState() == PortalSpawnState.ALLOW) { // and portal can spawn near them
            if (Math.random() < 0.05) { // 5% probability for now, will change later depending on player configs
                // spawn portal near a radius of the player, should be flat at their height
                Location locationAttempt;

                if ((locationAttempt = PlayerUtil.findClosestEmptyBoxArea((int) player.getLocation().getX(),
                        (int) player.getLocation().getY(),
                        (int) player.getLocation().getZ(),
                        1,
                        10,
                        player.getWorld()))
                        != null) {
                    playerManager.setPortalSpawnState(PortalSpawnState.DISALLOWED);
                    PortalDungeons.getInstance().getPortalManager().createPortal(
                            player.getUniqueId(),
                            locationAttempt,
                            PlayerUtil.getCardinalDirection(player));
                    player.sendMessage(ChatColor.GREEN + "A random portal has now spawned at: " +
                            "x: " + locationAttempt.getX() +
                            " y: " + locationAttempt.getY() +
                            " z: " + locationAttempt.getZ());
                } else {
                    // Bukkit.getLogger().info(ChatColor.DARK_RED + "Portal couldn't be spawned!");
                }
            } else {
                //player.sendMessage(ChatColor.DARK_RED + "Unlucky, you didn't hit the portal spawn!");
            }
        }
    }
}