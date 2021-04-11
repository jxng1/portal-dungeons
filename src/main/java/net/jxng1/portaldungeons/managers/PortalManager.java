package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.PortalGenerator;
import net.jxng1.portaldungeons.generators.RoomGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;
import java.util.Map.Entry;

public class PortalManager {

    private PortalDungeons plugin;
    private HashMap<PortalGenerator, UUID> portalMapOverworld = new HashMap<>();
    private HashMap<PortalGenerator, UUID> portalMapDungeon = new HashMap<>();

    public PortalManager(PortalDungeons plugin) {
        this.plugin = plugin;
    }

    public void createPortal(UUID uuid, Location location, String playerCardinality) {
        PortalGenerator newPortal = new PortalGenerator(location);

        portalMapOverworld.put(newPortal, uuid);
        newPortal.buildPortal(playerCardinality);
    }

    public void removePortal(PortalGenerator portal, HashMap<PortalGenerator, UUID> map) {
        portal.destroyPortal();
        map.remove(portal);
    }

    public void removeOverworldPortalFromUUID(UUID uuid) {
        try {
            removePortal(getPortal(uuid, portalMapOverworld), portalMapOverworld);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to remove non-existent portal!");
        }
    }

    public void removeDungeonWorldPortalFromUUID(UUID uuid) {
        try {
            removePortal(getPortal(uuid, portalMapDungeon), portalMapDungeon);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to remove non-existent portal!");
        }
    }

    public void removeOverworldPortals() {
        Iterator<Map.Entry<PortalGenerator, UUID>> entries = portalMapOverworld.entrySet().iterator();

        while (entries.hasNext()) {
            Entry<PortalGenerator, UUID> entry = entries.next();

            removePortal(entry.getKey(), portalMapOverworld);
            entries.remove();
        }
    }

    public void removeDungeonWorldPortals() {
        Iterator<Map.Entry<PortalGenerator, UUID>> entries = portalMapDungeon.entrySet().iterator();

        while (entries.hasNext()) {
            Entry<PortalGenerator, UUID> entry = entries.next();

            removePortal(entry.getKey(), portalMapDungeon);
            entries.remove();
        }
    }

    public List<PortalGenerator> getOverworldPortalList() {
        return new ArrayList<>(portalMapOverworld.keySet());
    }

    public List<PortalGenerator> getDungeonPortalList() {
        return new ArrayList<>(portalMapDungeon.keySet());
    }

    public HashMap<PortalGenerator, UUID> getPortalMapOverworld() {
        return this.portalMapOverworld;
    }

    public void setPortalLink(PortalGenerator from, UUID uuid) {
        Random random = new Random();
        int x;
        int z;

        do {
            x = (random.nextInt(Integer.MAX_VALUE) * (random.nextBoolean() ? 1 : -1)) >> 12;
            z = (random.nextInt(Integer.MAX_VALUE) * (random.nextBoolean() ? 1 : -1)) >> 12;
        } while (isCloseToOtherPortals(x, z));

        Chunk destChunk = PortalDungeons.getInstance().dungeonWorld.getChunkAt(x, z);

        Block base = destChunk.getBlock(7, RoomGenerator.ROOM_HEIGHT + 1, 7);

        PortalGenerator dest = new PortalGenerator(base.getLocation());
        from.setLink(dest);
        dest.setLink(from);

        portalMapDungeon.put(dest, uuid);
    }

    private PortalGenerator getPortal(UUID uuid, HashMap<PortalGenerator, UUID> map) {
        for (Entry<PortalGenerator, UUID> entry : map.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public Boolean isCloseToOtherPortals(int x, int z) { //in "dungeon" dimension
        for (PortalGenerator portal : portalMapDungeon.keySet()) {
            if (x >= portal.getBaseLocation().getX() + 150 || x <= portal.getBaseLocation().getX() - 150
                    || z >= portal.getBaseLocation().getZ() + 150 || z <= portal.getBaseLocation().getZ() - 150) {
                return true;
            }
        }
        return false;
    }

    public Boolean isNotInMultipleChunks(int x, int z) { // make sure portal wouldn't exist in multiple chunks
        Block block = PortalDungeons.getInstance().dungeonWorld.getBlockAt(x, RoomGenerator.ROOM_HEIGHT + 1, z);

        for (int tempX = -1; tempX != 1; tempX++) {
            for (int tempZ = -1; tempZ != 1; tempZ++) {
                if (block.getChunk() != PortalDungeons.getInstance().dungeonWorld.getBlockAt(x + 1, RoomGenerator.ROOM_HEIGHT + 1, z).getChunk()) {
                    return false;
                }
            }
        }

        return true;
    }

    public Boolean isNotInRoom(int x, int z) {
        Block block = PortalDungeons.getInstance().dungeonWorld.getBlockAt(x, RoomGenerator.ROOM_HEIGHT + 1, z);

        return block.getChunk().getX() != 15 &&
                block.getChunk().getX() != 0 &&
                block.getChunk().getZ() != 15 &&
                block.getChunk().getZ() != 0;
    }
}
