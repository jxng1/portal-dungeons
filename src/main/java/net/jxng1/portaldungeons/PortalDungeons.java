package net.jxng1.portaldungeons;

import net.jxng1.portaldungeons.commands.*;
import net.jxng1.portaldungeons.generators.WorldGenerator;
import net.jxng1.portaldungeons.listeners.BlockListener;
import net.jxng1.portaldungeons.listeners.DungeonListener;
import net.jxng1.portaldungeons.listeners.PortalListener;
import net.jxng1.portaldungeons.listeners.ServerListener;
import net.jxng1.portaldungeons.managers.FileManager;
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
    private FileManager fileManager;

    public World dungeonWorld;
    public World baseWorld;

    @Override
    public void onEnable() {
        instance = this;

        portalManager = new PortalManager();
        fileManager = new FileManager(instance);

        this.baseWorld = Bukkit.getServer().getWorlds().get(0); // READ FROM CONFIG
        this.dungeonWorld = Bukkit.getServer().createWorld(new WorldCreator("dungeon"));

        /* Listeners */
        //getServer().getPluginManager().registerEvents(new PortalListener(), instance);
        //getServer().getPluginManager().registerEvents(new DungeonListener(), instance);
        getServer().getPluginManager().registerEvents(new ServerListener(), instance);
        getServer().getPluginManager().registerEvents(new BlockListener(), instance);

        /* Commands */
        this.getCommand("buildportal").setExecutor(new BuildPortal());
        this.getCommand("removeportal").setExecutor(new RemovePortal());
        this.getCommand("generateroom").setExecutor(new GenerateRoom());
        this.getCommand("dungeonteleport").setExecutor(new TeleportPlayerToDungeonWorld());
        this.getCommand("edit").setExecutor(new ChangePlayerEditState());
        this.getCommand("structure").setExecutor(new AddNewStructure());
    }

    @Override
    public void onDisable() {
        cleanup();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String uid) {
        return new WorldGenerator();
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public PortalManager getPortalManager() {
        return this.portalManager;
    }

    public PlayerManager getPlayerManager(UUID uuid) {
        return playerManagers.get(uuid);
    }

    public DungeonManager getDungeonManager(UUID uuid) {
        return this.dungeonManagers.get(uuid);
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public static PortalDungeons getInstance() {
        return instance;
    }

    public DungeonManager createNewDungeonManager(UUID uuid) {
        if (!dungeonManagers.containsKey(uuid)) {
            dungeonManagers.put(uuid, new DungeonManager());
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
        dungeonManagers.clear();
    }
}
