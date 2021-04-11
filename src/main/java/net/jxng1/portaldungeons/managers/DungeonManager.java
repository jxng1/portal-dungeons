package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.generators.RoomGenerator;
import net.jxng1.portaldungeons.generators.RoomType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.*;

public class DungeonManager { // might be integrated into a DungeonManager which has a multitude of RoomManagers...

    private PortalDungeons plugin;
    private List<RoomGenerator> rooms = new ArrayList<>();
    private List<Chunk> roomChunks = new ArrayList<>();

    private String seed; // might add this in later

    private final Random random = new Random();

    public DungeonManager(PortalDungeons plugin) {
        this.plugin = plugin;
        this.seed = generateSeed(random.nextInt(10) + 5);
    }

    public void generateRoom(Chunk chunk) {
        char[] seedArray = seed.toCharArray();
        Chunk newChunk = chunk;
        World world = PortalDungeons.getInstance().dungeonWorld;
        RoomGenerator currentRoom;
        RoomType roomType = RoomType.LOBBY;
        int i = 0;

        while (i < seedArray.length) {
            if (seedArray[i] != '0') {
                switch (seedArray[i]) { // Direction
                    case '1': //north
                        newChunk = world.getChunkAt(newChunk.getX(), newChunk.getZ() - 1);

                        break;
                    case '2': //east
                        newChunk = world.getChunkAt(newChunk.getX() + 1, newChunk.getZ());

                        break;
                    case '3': //south
                        newChunk = world.getChunkAt(newChunk.getX(), newChunk.getZ() + 1);

                        break;
                    case '4': //west
                        newChunk = world.getChunkAt(newChunk.getX() - 1, newChunk.getZ());

                        break;
                    default:
                        Bukkit.getLogger().info("Error with rooms.");
                }
            }
            switch (seedArray[++i]) { // RoomType
                case 'L':
                    roomType = RoomType.LOBBY;
                    break;
                case 'N':
                    roomType = RoomType.NORMAL;
                    break;
                case 'E':
                    roomType = RoomType.END;
                    Bukkit.getLogger().info(ChatColor.DARK_PURPLE + "END ROOM ADDED!");
                    break;
                default:
                    Bukkit.getLogger().info("Error with rooms.");
            }

            currentRoom = new RoomGenerator(newChunk, roomType, world);
            i++;
            rooms.add(currentRoom);
            roomChunks.add(newChunk);
        }

        Bukkit.getLogger().info("No of rooms: " + rooms.size());

        rooms.forEach(RoomGenerator::buildRoom);
        rooms.forEach(room -> room.createDoorway(this.roomChunks));
        rooms.forEach(RoomGenerator::populateRoom);
    }

    private String generateSeed(int roomSize) { // kind of want to make this better.
        int[] seed = new int[roomSize];
        char[] out = new char[roomSize * 2];
        int sel;
        int i = 1;
        int j = 0;

        seed[0] = 0;
        while (i < roomSize) {
            sel = random.nextInt(4) + 1;
            if (seed[i - 1] == 0) { //start
                seed[i] = sel;
                i++;
            } else if ((seed[i - 1] - sel) % 2 != 0) {
                seed[i] = sel;
                i++;
            }
        }

        i = 0;
        while (j < roomSize * 2) {
            if (i == 0) {
                out[j++] = (char) 48;
                out[j++] = 'L';
                i++;
            } else if (j == roomSize * 2 - 2) {
                out[j++] = (char) (seed[i++] + 48);
                out[j++] = 'E';
            } else {
                out[j++] = (char) (seed[i++] + 48);
                out[j++] = 'N';
            }
        }

        Bukkit.getLogger().info("SEED: " + ChatColor.RED + String.valueOf(out) + " SIZE: " + roomSize);
        return String.valueOf(out);
    }

    public void cleanup() {
        rooms.forEach(RoomGenerator::cleanup);
    }
}
