package net.jxng1.portaldungeons;

import net.jxng1.portaldungeons.commands.*;
import net.jxng1.portaldungeons.generators.DungeonGenerator;
import net.jxng1.portaldungeons.listeners.EventListener;
import net.jxng1.portaldungeons.managers.PlayerManager;
import net.jxng1.portaldungeons.managers.PortalManager;
import net.jxng1.portaldungeons.managers.RoomManager;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public final class PortalDungeons extends JavaPlugin {

    private static PortalDungeons instance;

    private PortalManager portalManager;
    private PlayerManager playerManager;
    private RoomManager roomManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        portalManager = new PortalManager(instance);
        playerManager = new PlayerManager(instance);
        roomManager = new RoomManager(instance);

        /* Listener */
        getServer().getPluginManager().registerEvents(new EventListener(), instance);

        //this.getCommand("dimension").setExecutor(teleportToDimension);
        this.getCommand("buildportal").setExecutor(new BuildPortal());
        this.getCommand("removeportal").setExecutor(new RemovePortal());
        this.getCommand("generateroom").setExecutor(new GenerateRoom());
        this.getCommand("removerooms").setExecutor(new RemoveRooms());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        cleanup();
    }

    public void cleanup() {
        roomManager.removeRooms();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String uid) {
        return new DungeonGenerator();
    }

    public PortalManager getPortalManager() {
        return this.portalManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public RoomManager getRoomManager() {
        return this.roomManager;
    }

    public static PortalDungeons getInstance() {
        return instance;
    }
}
