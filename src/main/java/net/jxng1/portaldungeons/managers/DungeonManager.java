package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.PortalGenerator;
import net.jxng1.portaldungeons.generators.RoomGenerator;
import net.jxng1.portaldungeons.generators.RoomType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

public class DungeonManager { // might be integrated into a DungeonManager which has a multitude of RoomManagers...

    private final List<RoomGenerator> rooms = new ArrayList<>();
    private final List<Chunk> roomChunks = new ArrayList<>();

    private PortalGenerator startPortal; // DUNGEON START
    private PortalGenerator endPortal; // DUNGEON END
    private PortalGenerator overworldPortal; // OVERWORLD PORTAL

    private final int dungeonSize;

    private final Random random = new Random();

    public DungeonManager() {
        this.dungeonSize = random.nextInt(10) + 5; // 5-10 rooms... READ FROM CONFIG
    }

    public void generateDungeon(Chunk startingChunk) {
        World dungeonWorld = PortalDungeons.getInstance().dungeonWorld;
        Chunk targetChunk = null;

        int currentChunkX, currentChunkZ;
        currentChunkX = startingChunk.getX();
        currentChunkZ = startingChunk.getZ();

        roomChunks.add(startingChunk);
        rooms.add(new RoomGenerator(startingChunk, RoomType.LOBBY, dungeonWorld));

        for (int i = 1; i < dungeonSize; i++) {
            do {
                int dir = random.nextInt(4) + 1;

                switch (dir) {
                    case 1: // NORTH
                        targetChunk = dungeonWorld.getChunkAt(currentChunkX, currentChunkZ--);
                        break;
                    case 2: // EAST
                        targetChunk = dungeonWorld.getChunkAt(currentChunkX++, currentChunkZ);
                        break;
                    case 3: // SOUTH
                        targetChunk = dungeonWorld.getChunkAt(currentChunkX, currentChunkZ++);
                        break;
                    case 4: // WEST
                        targetChunk = dungeonWorld.getChunkAt(currentChunkX--, currentChunkZ);
                        break;
                }
            } while (roomChunks.contains(targetChunk));

            roomChunks.add(targetChunk);

            if (roomChunks.size() == dungeonSize) {
                rooms.add(new RoomGenerator(targetChunk, RoomType.END, dungeonWorld));
            } else {
                rooms.add(new RoomGenerator(targetChunk, RoomType.NORMAL, dungeonWorld));
            }
        }

        Bukkit.getLogger().info(ChatColor.DARK_RED + "No of rooms: " + rooms.size());

        // TODO
        // Can turn below code into another method.
        rooms.forEach(RoomGenerator::buildRoom);
        rooms.forEach(room -> room.createDoorway(this.roomChunks));
        rooms.forEach(RoomGenerator::populateRoom);
    }

    public void cleanup() {
        rooms.forEach(RoomGenerator::cleanup);
    }

    public void setEndPortal(PortalGenerator endPortal) {
        this.endPortal = endPortal;
    }

    public PortalGenerator getEndPortal() {
        return endPortal;
    }

    public void setOverworldPortal(PortalGenerator overworldPortal) {
        this.overworldPortal = overworldPortal;
    }

    public PortalGenerator getOverworldPortal() {
        return overworldPortal;
    }

    public void setStartPortal(PortalGenerator startPortal) {
        this.startPortal = startPortal;
    }

    public PortalGenerator getStartPortal() {
        return startPortal;
    }

    public PortalGenerator createDungeonStartPortal(UUID uuid) {
        Random random = new Random();
        int x;
        int z;

        do {
            x = (random.nextInt(Integer.MAX_VALUE) * (random.nextBoolean() ? 1 : -1)) >> 12;
            z = (random.nextInt(Integer.MAX_VALUE) * (random.nextBoolean() ? 1 : -1)) >> 12;
        } while (PortalDungeons.getInstance().getPortalManager().isCloseToOtherPortals(x, z));

        Chunk destChunk = PortalDungeons.getInstance().dungeonWorld.getChunkAt(x, z);

        Block base = destChunk.getBlock(7, RoomGenerator.ROOM_HEIGHT + 1, 7);

        PortalGenerator dest = new PortalGenerator(base.getLocation());
        setStartPortal(dest);

        PortalDungeons.getInstance().getPortalManager().getPortalMapDungeonWorld().put(dest, uuid);

        return dest;
    }

    public PortalGenerator createDungeonEndPortal(UUID uuid) {
        for (RoomGenerator room : this.rooms) {
            if (room.getRoomType() == RoomType.END) {
                Block base = room.getChunk().getBlock(7, RoomGenerator.ROOM_HEIGHT + 1, 7);

                PortalGenerator dest = new PortalGenerator(base.getLocation());
                setEndPortal(dest);

                PortalDungeons.getInstance().getPortalManager().getPortalMapDungeonWorld().put(dest, uuid);

                return dest;
            }
        }

        return null;
    }
}
