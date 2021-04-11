package net.jxng1.portaldungeons.listeners;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.PortalGenerator;
import net.jxng1.portaldungeons.generators.RoomGenerator;
import net.jxng1.portaldungeons.managers.DungeonManager;
import net.jxng1.portaldungeons.managers.PlayerManager;
import net.jxng1.portaldungeons.managers.PortalSpawnState;
import net.jxng1.portaldungeons.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerInteractWithPortal(PlayerMoveEvent event) { // player interaction with portal

        List<PortalGenerator> portalList;
        Player player = event.getPlayer();
        PlayerManager playerManager = PortalDungeons.getInstance().getPlayerManager(player.getUniqueId());

        if (player.getWorld() == PortalDungeons.getInstance().baseWorld) {
            portalList = PortalDungeons.getInstance().getPortalManager().getOverworldPortalList();
        } else if (player.getWorld() == PortalDungeons.getInstance().dungeonWorld) {
            portalList = PortalDungeons.getInstance().getPortalManager().getDungeonPortalList();
        } else {
            return;
        }

        for (PortalGenerator portal : portalList) { // for checking if player "enters" a portal
            if (portal.getBaseLocation().getBlockX() == player.getLocation().getBlockX()
                    && portal.getBaseLocation().getBlockZ() == player.getLocation().getBlockZ()
                    && portal.getBaseLocation().getBlockY() + 1 == player.getLocation().getBlockY()) {

                if (portal.getLink() == null) {
                    PortalDungeons.getInstance().getPortalManager().setPortalLink(portal, player.getUniqueId());
                    DungeonManager dm = PortalDungeons.getInstance().createNewDungeonManager(player.getUniqueId()); //rooms
                    dm.generateRoom(portal.getLink().getBaseLocation().getChunk());

                    portal.getLink().buildPortal(PlayerUtil.getCardinalDirection(player));
                }

                if (player.getWorld() == PortalDungeons.getInstance().baseWorld) {
                    playerManager.setAllowPortalSpawn(PortalSpawnState.DISABLED);
                    Bukkit.getLogger().info("Portal spawn disabled for player.");
                } else if (player.getWorld() == PortalDungeons.getInstance().dungeonWorld) {
                    playerManager.setAllowPortalSpawn(PortalSpawnState.DISALLOWED);
                    Bukkit.getLogger().info("Portal spawn disallowed for player.");
                }

                player.teleport(new Location(portal.getLink().getBaseLocation().getWorld(),
                        (int) portal.getLink().getBaseLocation().getX() + 2,
                        RoomGenerator.ROOM_HEIGHT + 1,
                        (int) portal.getLink().getBaseLocation().getZ() + 2));

            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) { // chance to spawn portal surrounding them

        Player player = event.getPlayer();
        PlayerManager playerManager = PortalDungeons.getInstance().getPlayerManager(player.getUniqueId());

        if (player.getWorld() == PortalDungeons.getInstance().baseWorld && // in overworld
                playerManager.portalSpawnState == PortalSpawnState.ALLOW) { // and portal can spawn near them
            if (Math.random() < 0.1) { // 10% probability for now, will change later depending on player configs
                // spawn portal near a radius of the player, should be flat at their height
                Location locationAttempt;

                if ((locationAttempt = PlayerUtil.findClosestEmptyBoxArea((int) player.getLocation().getX(),
                        (int) player.getLocation().getY(),
                        (int) player.getLocation().getZ(),
                        1,
                        10,
                        player.getWorld()))
                        != null) {
                    playerManager.setAllowPortalSpawn(PortalSpawnState.DISALLOWED);
                    PortalDungeons.getInstance().getPortalManager().createPortal(
                            player.getUniqueId(),
                            locationAttempt,
                            PlayerUtil.getCardinalDirection(player));
                    Bukkit.getLogger().info("A random portal has now spawned at: " +
                            "x: " + locationAttempt.getX() +
                            "y: " + locationAttempt.getY() +
                            "z: " + locationAttempt.getZ());
                } else {
                    //Bukkit.getLogger().info(ChatColor.DARK_RED + "Couldn't spawn a nearby portal.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (PortalDungeons.getInstance().getPlayerManager(event.getPlayer().getUniqueId()) == null) {
            PortalDungeons.getInstance().createNewPlayerManager(event.getPlayer().getUniqueId());
            Bukkit.getLogger().info(event.getPlayer().getDisplayName() + " now has a player manager!");
        }
        event.getPlayer().teleport(PortalDungeons.getInstance().baseWorld.getSpawnLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (PortalDungeons.getInstance().getPlayerManager(event.getPlayer().getUniqueId()) != null) {
            PortalDungeons.getInstance().removePlayerManager(event.getPlayer().getUniqueId());
            Bukkit.getLogger().info(event.getPlayer().getDisplayName() + "'s player manager is now removed!");
        }
    }

    @EventHandler
    public void onPlayerRespawnFromDungeonWorld(PlayerRespawnEvent event) {
        // if player respawns from dungeon world
        if (event.getPlayer().getWorld() == PortalDungeons.getInstance().dungeonWorld) {
            if (event.getPlayer().getBedSpawnLocation() != null) { // player has bed spawn
                event.setRespawnLocation(event.getPlayer().getBedSpawnLocation());
            } else { // player has no bed spawn
                event.setRespawnLocation(PortalDungeons.getInstance().baseWorld.getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerDeathInDungeonWorld(PlayerDeathEvent event) {
        // if player dies in dungeon world
        if (event.getEntity().getWorld() == PortalDungeons.getInstance().dungeonWorld) {
            PortalDungeons.getInstance().removeDungeonManager(event.getEntity().getUniqueId());
            PortalDungeons.getInstance().getPortalManager().removeOverworldPortalFromUUID(event.getEntity().getUniqueId());
            PortalDungeons.getInstance().getPortalManager().removeDungeonWorldPortalFromUUID(event.getEntity().getUniqueId());
        }
    }

}