package net.jxng1.portaldungeons.commands;

import net.jxng1.portaldungeons.PortalDungeons;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportPlayerToDungeonWorld implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.teleport(new Location(PortalDungeons.getInstance().dungeonWorld,
                    PortalDungeons.getInstance().dungeonWorld.getSpawnLocation().getX(),
                    PortalDungeons.getInstance().dungeonWorld.getSpawnLocation().getY(),
                    PortalDungeons.getInstance().dungeonWorld.getSpawnLocation().getZ()));
            player.setGameMode(GameMode.CREATIVE);

            return true;
        }

        return false;
    }
}
