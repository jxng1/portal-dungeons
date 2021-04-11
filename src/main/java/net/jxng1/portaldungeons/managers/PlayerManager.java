package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.tasks.PlayerPortalSpawnTask;

import java.util.UUID;

public class PlayerManager {

    private final UUID uuid;

    public PortalSpawnState portalSpawnState;

    public PlayerManager(UUID uuid) {
        this.portalSpawnState = PortalSpawnState.ALLOW;
        this.uuid = uuid;
    }

    public void setAllowPortalSpawn(PortalSpawnState state) {
        this.portalSpawnState = state;
        if (this.portalSpawnState == PortalSpawnState.DISALLOWED) {
            PlayerPortalSpawnTask playerPortalSpawnTask = new PlayerPortalSpawnTask(this);
            playerPortalSpawnTask.runTaskTimer(PortalDungeons.getInstance(), 0, 20);
        }
    }

    public UUID getUUID() {
        return this.uuid;
    }
}