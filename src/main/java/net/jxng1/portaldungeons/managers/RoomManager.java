package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.RoomGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RoomManager { // might be integrated into a DungeonManager which has a multitude of RoomManagers...

    private PortalDungeons plugin;
    private HashMap<Integer, RoomGenerator> roomMap = new HashMap<>();

    public RoomManager(PortalDungeons plugin) {
        this.plugin = plugin;
    }

    public void generateRoom(Chunk chunk) {
        int id = 1;

        RoomGenerator room = new RoomGenerator(chunk, id, "lobby");
        roomMap.put(id, room);
        room.buildRoom();
    }

    public void removeRoom(RoomGenerator room) {
        room.destroyRoom();
    }

    public void removeRooms() {
        Iterator<Map.Entry<Integer, RoomGenerator>> entries = roomMap.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<Integer, RoomGenerator> entry = entries.next();

            removeRoom(entry.getValue());
            entries.remove();
        }
    }
}
