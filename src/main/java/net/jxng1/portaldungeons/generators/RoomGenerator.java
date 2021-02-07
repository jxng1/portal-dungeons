package net.jxng1.portaldungeons.generators;

import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RoomGenerator {

    private int currentHeight = 65;
    private Set<Block> blocks = new HashSet<>();
    private Chunk chunk;

    private RoomGenerator northRoom;
    private RoomGenerator eastRoom;
    private RoomGenerator southRoom;
    private RoomGenerator westRoom;

    private String roomType;
    private int id;

    public RoomGenerator(Chunk chunk, int id, String roomType) {
        this.chunk = chunk;
        this.id = id;
        this.roomType = roomType;
    }

    public void buildRoom() {
        Block currentBlock;

        for (int x = 0; x < 16; x++) { // creates box
            for (int z = 0; z < 16; z++) {
                currentBlock = chunk.getBlock(x, currentHeight, z);
                currentBlock.setType(Material.OBSIDIAN);
                blocks.add(currentBlock);
                for (int i = currentHeight + 1; i < 100; i++) {
                    currentBlock = chunk.getBlock(0, i, z);
                    currentBlock.setType(Material.OBSIDIAN);
                    blocks.add(currentBlock);

                    currentBlock = chunk.getBlock(15, i, z);
                    currentBlock.setType(Material.OBSIDIAN);
                    blocks.add(currentBlock);

                    currentBlock = chunk.getBlock(x, i, 0);
                    currentBlock.setType(Material.OBSIDIAN);
                    blocks.add(currentBlock);

                    currentBlock = chunk.getBlock(x, i, 15);
                    currentBlock.setType(Material.OBSIDIAN);
                    blocks.add(currentBlock);
                }
                currentBlock = chunk.getBlock(x, 100, z);
                currentBlock.setType(Material.OBSIDIAN);
                blocks.add(currentBlock);
            }
        }

        createDoorway(); // creates a doorway
    }

    public void createDoorway() {
        String direction = generateDoorPosition();
        int doorX = 0;
        int doorZ = 0;

        switch (direction) {
            case "N":
                doorX = 7;
                doorZ = 15;
                break;
            case "E":
                doorX = 15;
                doorZ = 7;
                break;
            case "S":
                doorX = 7;
                doorZ = 0;
                break;
            case "W":
                doorX = 0;
                doorZ = 7;
                break;
            default:
                Bukkit.getLogger().info("Error with creating room door.");
        }
        chunk.getBlock(doorX, currentHeight + 1, doorZ).setType(Material.AIR);
        chunk.getBlock(doorX, currentHeight + 2, doorZ).setType(Material.AIR);
    }

    public String generateDoorPosition() {
        Random random = new Random();
        int sel = random.nextInt(4) + 1;
        switch (sel) {
            case 1:
                return "N";
            case 2:
                return "E";
            case 3:
                return "S";
            case 4:
                return "W";
            default:
                Bukkit.getLogger().info("Direction not generated!");
                return "";
        }
    }

    public void destroyRoom() {
        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
        blocks.clear();
    }
}
