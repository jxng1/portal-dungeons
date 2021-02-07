package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerManager {

    private PortalDungeons plugin;

    public PlayerManager(PortalDungeons plugin) {
        this.plugin = plugin;
    }

    public String getCardinalDirection(Player player) {
        double rotation = player.getLocation().getYaw() - 180;

        while (rotation < 0.0D) {
            rotation += 360.0D;
        }

        Bukkit.getLogger().info(String.valueOf(rotation));

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
        return null;
    }
}