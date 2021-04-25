package net.jxng1.portaldungeons.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static String getCardinalDirection(Player player) {
        double rotation = player.getLocation().getYaw() - 180;

        while (rotation < 0.0D) {
            rotation += 360.0D;
        }

        if ((0.0D <= rotation) && (rotation < 22.5D)) {
            return "N";
        }
        if ((22.5D <= rotation) && (rotation < 67.5D)) {
            return "NE";
        }
        if ((67.5D <= rotation) && (rotation < 112.5D)) {
            return "E";
        }
        if ((112.5D <= rotation) && (rotation < 157.5D)) {
            return "SE";
        }
        if ((157.5D <= rotation) && (rotation < 202.5D)) {
            return "S";
        }
        if ((202.5D <= rotation) && (rotation < 247.5D)) {
            return "SW";
        }
        if ((247.5D <= rotation) && (rotation < 292.5D)) {
            return "W";
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return "NW";
        }
        if ((337.5D <= rotation) && (rotation < 360.0D)) {
            return "N";
        }
        return "N"; // fail-safe
    }

    // Issue with this function is that it will ALWAYS start at the left most co-ordinate, so is not random.
    // May have further optimisation regarding some parts of the loop being skippable.
    public static Location findClosestEmptyBoxArea(int x, int y, int z, int areaSize, int distanceToCheck, World world) {
        for (int xDistance = x - distanceToCheck; xDistance <= x + distanceToCheck; xDistance++) {
            for (int zDistance = z - distanceToCheck; zDistance <= z + distanceToCheck; zDistance++) {
                for (int yDistance = y - distanceToCheck; yDistance <= y + distanceToCheck; yDistance++) {
                    // distanceToCheck determines how far of a range to check.
                    // 1) Checks to see if the block beneath the base block isn't empty(so portal isn't just in air);
                    // 2) Checks to see if an area from that base block is all empty.
                    // Using deprecated method, might need to make my own isTransparent()...
                    if (!world.getBlockAt(xDistance, yDistance - 1, zDistance).getType().isEmpty() &&
                            !world.getBlockAt(xDistance, yDistance - 1, zDistance).getType().isTransparent() &&
                            isBoxAreaEmpty(xDistance, yDistance, zDistance, areaSize, world)) {
                        Bukkit.getLogger().info(ChatColor.DARK_RED + "Valid base location found.");
                        return new Location(world, xDistance, yDistance, zDistance);
                    }
                }
            }
        }

        return null;
    }

    private static boolean isBoxAreaEmpty(int x, int y, int z, int areaSize, World world) {
        for (int tempX = x - areaSize; tempX <= x + areaSize; tempX++) {
            for (int tempZ = z - areaSize; tempZ <= z + areaSize; tempZ++) {
                for (int tempY = y; tempY <= y + areaSize; tempY++) {
                    if (!(world.getBlockAt(tempX, tempY, tempZ).getType().isEmpty())) {
                        //Bukkit.getLogger().info(ChatColor.DARK_RED + "Not suitable.");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}