package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.PortalGenerator;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class PortalManager {

    private PortalDungeons plugin;
    private HashMap<PortalGenerator, UUID> portalMap = new HashMap<>();

    public PortalManager(PortalDungeons plugin) {
        this.plugin = plugin;
    }

    public void createPortal(UUID uuid, Location location, String playerCardinality) {
        PortalGenerator newPortal = new PortalGenerator(location);

        portalMap.put(newPortal, uuid);
        newPortal.buildPortal(playerCardinality);
    }

    public void removePortal(PortalGenerator portal) {
        portal.destroyPortal();
    }

    public void removePortals() {
        Iterator<Map.Entry<PortalGenerator, UUID>> entries = portalMap.entrySet().iterator();

        while (entries.hasNext()) {
            Entry<PortalGenerator, UUID> entry = entries.next();

            removePortal(entry.getKey());
            entries.remove();
        }
    }

    public HashMap<PortalGenerator, UUID> getPortalMap() {
        return this.portalMap;
    }
}
