package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.tasks.PlayerPortalSpawnTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class PlayerManager {

    private final UUID uuid;

    private PortalSpawnState portalSpawnState;

    private PlayerPortalSpawnTask playerPortalSpawnTask;
    private StructureManager structureManager;

    public PlayerManager(UUID uuid) {
        this.portalSpawnState = PortalSpawnState.ALLOW;
        this.uuid = uuid;
        this.structureManager = new StructureManager();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setPortalSpawnState(PortalSpawnState state) {
        this.portalSpawnState = state;
        if (this.portalSpawnState == PortalSpawnState.DISALLOWED) {
            playerPortalSpawnTask = new PlayerPortalSpawnTask(this);
            playerPortalSpawnTask.runTaskTimer(PortalDungeons.getInstance(), 0, 20);
        }
    }

    public PortalSpawnState getPortalSpawnState() {
        return portalSpawnState;
    }

    public PlayerPortalSpawnTask getPlayerPortalSpawnTask() {
        return playerPortalSpawnTask;
    }

    public void cancelPortalSpawnTask() {
        this.playerPortalSpawnTask.cancel();
    }

    public void sendDungeonEnterMessage() {
        String[] first = {"Howling", "Freezing", "Deep", "Haunted", "Forgotten", "Deserted", "Vicious", "Flowing", "Dilapidated"};
        String[] second = {"Swamp", "Pits", "Caves", "Tombs", "Chambers"};

        Random random = new Random();

        try {
            sendTitle(Bukkit.getPlayer(this.uuid), first[random.nextInt(first.length)], second[random.nextInt(second.length)]);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to get non-existent player.");
        }
    }

    private void sendTitle(Player player, String first, String second) {
        player.sendTitle(ChatColor.DARK_RED + "The " + first + " " + second, "", 1, 70, 1);
    }

    public StructureManager getStructureManager() {
        return structureManager;
    }

    public void setStructureManager(StructureManager structureManager) {
        this.structureManager = structureManager;
    }
}