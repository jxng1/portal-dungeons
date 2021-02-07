package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.RoomGenerator;
import org.bukkit.Chunk;

import java.util.*;

public class RoomManager { // might be integrated into a DungeonManager which has a multitude of RoomManagers...

    private PortalDungeons plugin;
    private Set<RoomGenerator> rooms = new HashSet<>();

    public RoomManager(PortalDungeons plugin) {
        this.plugin = plugin;
    }

    public void generateRoom(Chunk chunk) {
        RoomGenerator room = new RoomGenerator(chunk, "lobby");
        rooms.add(room);
        room.buildRoom();
    }

    public void removeRoom(RoomGenerator room) {
        room.destroyRoom();
    }

    public void removeRooms() {
        rooms.forEach(this::removeRoom);
        rooms.clear();
    }
}
