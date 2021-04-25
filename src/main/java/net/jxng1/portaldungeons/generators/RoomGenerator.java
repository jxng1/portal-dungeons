package net.jxng1.portaldungeons.generators;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RoomGenerator {

    public static int ROOM_HEIGHT = 65; // READ FROM CONFIG;
    private Set<Block> blocks = new HashSet<>();
    private Chunk chunk;
    private World world;

    private Set<Block> chests = new HashSet<>();

    private RoomType roomType;

    public RoomGenerator(Chunk chunk, RoomType roomType, World world) {
        this.chunk = chunk;
        this.roomType = roomType;
        this.world = world;
    }

    public void buildRoom() { // IMPLEMENT A MATERIAL FROM CONFIG...
        Block currentBlock;

        for (int x = 0; x < 16; x++) { // creates box
            for (int z = 0; z < 16; z++) {
                currentBlock = chunk.getBlock(x, ROOM_HEIGHT, z);
                currentBlock.setType(Material.CYAN_STAINED_GLASS);
                blocks.add(currentBlock);
                for (int i = ROOM_HEIGHT + 1; i < 85; i++) {
                    currentBlock = chunk.getBlock(0, i, z);
                    currentBlock.setType(Material.CYAN_STAINED_GLASS);
                    blocks.add(currentBlock);

                    currentBlock = chunk.getBlock(15, i, z);
                    currentBlock.setType(Material.CYAN_STAINED_GLASS);
                    blocks.add(currentBlock);

                    currentBlock = chunk.getBlock(x, i, 0);
                    currentBlock.setType(Material.CYAN_STAINED_GLASS);
                    blocks.add(currentBlock);

                    currentBlock = chunk.getBlock(x, i, 15);
                    currentBlock.setType(Material.CYAN_STAINED_GLASS);
                    blocks.add(currentBlock);
                }
                currentBlock = chunk.getBlock(x, 85, z);
                currentBlock.setType(Material.CYAN_STAINED_GLASS);
                blocks.add(currentBlock);
            }
        }
    }

    public void createDoorway(List<Chunk> roomChunks) {
        if (roomChunks.contains(world.getChunkAt(this.chunk.getX(), this.chunk.getZ() + 1))) { // south
            chunk.getBlock(7, ROOM_HEIGHT + 1, 15).setType(Material.AIR);
            chunk.getBlock(7, ROOM_HEIGHT + 2, 15).setType(Material.AIR);
        }
        if (roomChunks.contains(world.getChunkAt(this.chunk.getX() + 1, this.chunk.getZ()))) { // east
            chunk.getBlock(15, ROOM_HEIGHT + 1, 7).setType(Material.AIR);
            chunk.getBlock(15, ROOM_HEIGHT + 2, 7).setType(Material.AIR);
        }
        if (roomChunks.contains(world.getChunkAt(this.chunk.getX(), this.chunk.getZ() - 1))) { // north
            chunk.getBlock(7, ROOM_HEIGHT + 1, 0).setType(Material.AIR);
            chunk.getBlock(7, ROOM_HEIGHT + 2, 0).setType(Material.AIR);
        }
        if (roomChunks.contains(world.getChunkAt(this.chunk.getX() - 1, this.chunk.getZ()))) { // west
            chunk.getBlock(0, ROOM_HEIGHT + 1, 7).setType(Material.AIR);
            chunk.getBlock(0, ROOM_HEIGHT + 2, 7).setType(Material.AIR);
        }
    }

    public void populateRoom() {
        switch (this.roomType) {
            case LOBBY:
                break;
            case NORMAL:
                if (Math.random() < 0.05) { // READ FROM CONFIG
                    addChest(7, 7);
                }

                break;
            case END:
                break;
            default:
                Bukkit.getLogger().info("Error with populating room.");
        }
    }

    private void addChest(int baseX, int baseZ) {
        Random random = new Random();
        Block target;
        int x, z;
        int y = ROOM_HEIGHT + 1;

        do {
            x = baseX + ((random.nextInt(5) + 1) * (random.nextBoolean() ? 1 : -1));
            z = baseZ + ((random.nextInt(5) + 1) * (random.nextBoolean() ? 1 : -1));
            target = chunk.getBlock(x, y, z);
        } while ((x >= 15 || x <= 0) && (z >= 15 || z <= 0) && !target.getType().isEmpty());

        target.setType(Material.CHEST);
        chests.add(target);
        Bukkit.getLogger().info("Chest added at: " + target.getX() + " " + target.getZ());
        placeItems(((Chest) target.getState()).getInventory());
    }

    private void placeItems(Inventory inventory) {
        List<ItemStack> items = ItemGenerator.generateItems();

        items.forEach(inventory::addItem);
    }

    public void cleanup() {
        removeChests();
        destroyRoom();
    }

    private void removeChests() {
        chests.forEach(block -> block.setType(Material.AIR));
    }

    private void destroyRoom() {
        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
        blocks.clear();
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
