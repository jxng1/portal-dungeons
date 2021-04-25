package net.jxng1.portaldungeons.listeners;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.managers.DungeonManager;
import net.jxng1.portaldungeons.managers.PortalManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DungeonListener implements Listener {
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
            DungeonManager dm = PortalDungeons.getInstance().getDungeonManager(event.getEntity().getUniqueId());
            PortalManager pm = PortalDungeons.getInstance().getPortalManager();

            pm.removePortal(dm.getStartPortal(), pm.getPortalMapDungeonWorld());
            pm.removePortal(dm.getEndPortal(), pm.getPortalMapDungeonWorld());
            pm.removePortal(dm.getOverworldPortal(), pm.getPortalMapOverworld());

            dm.cleanup();

            PortalDungeons.getInstance().removeDungeonManager(event.getEntity().getUniqueId());
        }
    }
}
