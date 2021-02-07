package net.jxng1.portaldungeons.generators;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;

public class PortalGenerator {

    private Location baseLocation;
    private Set<Block> blocks = new HashSet<>();
    private Location linkLocation;

    public PortalGenerator(Location location) {
        this.baseLocation = location;
    }

    public void buildPortal(String playerCardinality) {
        Block portal = baseLocation.getBlock();

        portal.setType(Material.OBSIDIAN); // central column
        portal.getRelative(BlockFace.UP).setType(Material.DARK_OAK_WOOD);
        portal.getRelative(BlockFace.UP, 2).setType(Material.DARK_OAK_WOOD);
        portal.getRelative(BlockFace.UP, 3).setType(Material.OBSIDIAN);

        blocks.add(portal); // add central column to blocks set
        blocks.add(portal.getRelative(BlockFace.UP));
        blocks.add(portal.getRelative(BlockFace.UP, 2));
        blocks.add(portal.getRelative(BlockFace.UP, 3));

        // deals with player facing direction:
        Location left;
        Location right;

        switch (playerCardinality) { // player facing?
            case "N":
            case "NW":
            case "NE":
                left = baseLocation.clone().add(-1, 0, 0);
                right = baseLocation.clone().add(1, 0, 0);
                break;
            case "S":
            case "SW":
            case "SE":
                left = baseLocation.clone().add(1, 0, 0);
                right = baseLocation.clone().add(-1, 0, 0);
                break;
            case "E":
                left = baseLocation.clone().add(0, 0, -1);
                right = baseLocation.clone().add(0, 0, 1);
                break;
            case "W":
                left = baseLocation.clone().add(0, 0, 1);
                right = baseLocation.clone().add(0, 0, -1);
                break;
            default:
                Bukkit.getLogger().info("Unexpected direction.");
                return;
        }

        Block leftColumn = left.getBlock();
        Block rightColumn = right.getBlock();

        leftColumn.setType(Material.OBSIDIAN);
        leftColumn.getRelative(BlockFace.UP).setType(Material.OBSIDIAN);
        leftColumn.getRelative(BlockFace.UP, 2).setType(Material.OBSIDIAN);
        leftColumn.getRelative(BlockFace.UP, 3).setType(Material.OBSIDIAN);

        blocks.add(leftColumn);
        blocks.add(leftColumn.getRelative(BlockFace.UP));
        blocks.add(leftColumn.getRelative(BlockFace.UP, 2));
        blocks.add(leftColumn.getRelative(BlockFace.UP, 3));

        rightColumn.setType(Material.OBSIDIAN);
        rightColumn.getRelative(BlockFace.UP).setType(Material.OBSIDIAN);
        rightColumn.getRelative(BlockFace.UP, 2).setType(Material.OBSIDIAN);
        rightColumn.getRelative(BlockFace.UP, 3).setType(Material.OBSIDIAN);

        blocks.add(rightColumn);
        blocks.add(rightColumn.getRelative(BlockFace.UP));
        blocks.add(rightColumn.getRelative(BlockFace.UP, 2));
        blocks.add(rightColumn.getRelative(BlockFace.UP, 3));
    }

    public void destroyPortal() {
        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
        blocks.clear();
    }

    public Set<Block> getBlockLocations() {
        return this.blocks;
    }

    public Location getBaseLocation() {
        return this.baseLocation;
    }
}
