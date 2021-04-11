package net.jxng1.portaldungeons.tasks;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.managers.PlayerManager;
import net.jxng1.portaldungeons.managers.PortalSpawnState;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerPortalSpawnTask extends BukkitRunnable {

    private final PlayerManager playerManager;

    public PlayerPortalSpawnTask(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    private int timeLeft = 20;

    @Override
    public void run() {
        timeLeft--;
        if (timeLeft <= 0) {
            cancel();
            this.playerManager.setAllowPortalSpawn(PortalSpawnState.ALLOW);

            PortalDungeons.getInstance().getPortalManager().removeOverworldPortalFromUUID(this.playerManager.getUUID());
            Bukkit.getLogger().info("Player can now have a portal spawn.");
        } else {
            //Bukkit.getLogger().info(timeLeft + " until portal spawn.");
        }
    }
}
