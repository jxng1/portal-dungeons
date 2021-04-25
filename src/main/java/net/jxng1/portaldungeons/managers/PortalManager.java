package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.generators.PortalGenerator;
import org.bukkit.Location;

import java.util.*;
import java.util.Map.Entry;

public class PortalManager {

    private final HashMap<PortalGenerator, UUID> portalMapOverworld = new HashMap<>();
    private final HashMap<PortalGenerator, UUID> portalMapDungeon = new HashMap<>();

    public void createPortal(UUID uuid, Location location, String playerCardinality) {
        PortalGenerator newPortal = new PortalGenerator(location);

        portalMapOverworld.put(newPortal, uuid);
        newPortal.buildPortal(playerCardinality);
    }

    public void removePortal(PortalGenerator portal, HashMap<PortalGenerator, UUID> map) {
        portal.destroyPortal();
        map.remove(portal);
    }

    public void removeOverworldPortals() {
        portalMapOverworld.clear();
    }

    public void removeDungeonWorldPortals() {
        portalMapDungeon.clear();
    }

    public HashMap<PortalGenerator, UUID> getPortalMapOverworld() {
        return this.portalMapOverworld;
    }

    public HashMap<PortalGenerator, UUID> getPortalMapDungeonWorld() {
        return this.portalMapDungeon;
    }

    public PortalGenerator getPortal(UUID uuid, HashMap<PortalGenerator, UUID> map) {
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
}
