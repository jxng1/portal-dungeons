package net.jxng1.portaldungeons;

import net.jxng1.portaldungeons.commands.*;
import net.jxng1.portaldungeons.generators.DungeonGenerator;
import net.jxng1.portaldungeons.generators.PortalGenerator;
import net.jxng1.portaldungeons.listeners.EventListener;
import net.jxng1.portaldungeons.managers.PlayerManager;
import net.jxng1.portaldungeons.managers.PortalManager;
import net.jxng1.portaldungeons.managers.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class PortalDungeons extends JavaPlugin {

    private static PortalDungeons instance;

    private PortalManager portalManager;
    private HashMap<UUID, PlayerManager> playerManagers = new HashMap<>();
    private HashMap<UUID, DungeonManager> dungeonManagers = new HashMap<>();

    public World dungeonWorld;
    public World baseWorld;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        portalManager = new PortalManager(instance);

        this.baseWorld = Bukkit.getServer().getWorlds().get(0);
        this.dungeonWorld = Bukkit.getServer().createWorld(new WorldCreator("dungeon"));

        /* Listener */
        getServer().getPluginManager().registerEvents(new EventListener(), instance);

        /* Commands */
        this.getCommand("buildportal").setExecutor(new BuildPortal());
        this.getCommand("removeportal").setExecutor(new RemovePortal());
        this.getCommand("generateroom").setExecutor(new GenerateRoom());
        this.getCommand("dungeonteleport").setExecutor(new TeleportPlayerToDungeonWorld());
    }

    @Override
    public void onDisable() {
        cleanup();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String uid) {
        return new DungeonGenerator();
    }

    public PortalManager getPortalManager() {
        return this.portalManager;
    }

    public PlayerManager getPlayerManager(UUID uuid) {
        return playerManagers.get(uuid);
    }

    public static PortalDungeons getInstance() {
        return instance;
    }

    public DungeonManager createNewDungeonManager(UUID uuid) {
        if (!dungeonManagers.containsKey(uuid)) {
            dungeonManagers.put(uuid, new DungeonManager(instance));
        }

        return dungeonManagers.get(uuid);
    }

    public PlayerManager createNewPlayerManager(UUID uuid) {
        if (!playerManagers.containsKey(uuid)) {
            playerManagers.put(uuid, new PlayerManager(uuid));
        }

        return playerManagers.get(uuid);
    }

    public void removePlayerManager(UUID uuid) {
        playerManagers.entrySet().removeIf(entry -> entry.getKey().equals(uuid));
    }

    public void removeDungeonManager(UUID uuid) {
        dungeonManagers.entrySet().removeIf(entry -> entry.getKey().equals(uuid));
    }


    public void cleanup() {
        portalManager.removeDungeonWorldPortals();
        portalManager.removeOverworldPortals();

        Iterator<Map.Entry<UUID, DungeonManager>> entries = dungeonManagers.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<UUID, DungeonManager> entry = entries.next();

            entry.getValue().cleanup();
            entries.remove();
        }
    }
}
