package net.jxng1.portaldungeons.listeners;

import net.jxng1.portaldungeons.PortalDungeons;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PortalDungeons.getInstance().baseWorld.setTime(0); // SET TIME TO 0, FOR EASY DEBUG...
        PortalDungeons.getInstance().baseWorld.setClearWeatherDuration(Integer.MAX_VALUE);

        if (PortalDungeons.getInstance().getPlayerManager(event.getPlayer().getUniqueId()) == null) {
            PortalDungeons.getInstance().createNewPlayerManager(event.getPlayer().getUniqueId());
            Bukkit.getLogger().info(event.getPlayer().getDisplayName() + " now has a player manager!");
        }
        event.getPlayer().teleport(PortalDungeons.getInstance().baseWorld.getSpawnLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (PortalDungeons.getInstance().getPlayerManager(event.getPlayer().getUniqueId()) != null) {
            if (PortalDungeons.getInstance().getPlayerManager(event.getPlayer().getUniqueId()).getPlayerPortalSpawnTask() != null) {
                PortalDungeons.getInstance().getPlayerManager(event.getPlayer().getUniqueId()).cancelPortalSpawnTask();
            }

            PortalDungeons.getInstance().removePlayerManager(event.getPlayer().getUniqueId());
            Bukkit.getLogger().info(event.getPlayer().getDisplayName() + "'s player manager is now removed!");
        }
    }
}
