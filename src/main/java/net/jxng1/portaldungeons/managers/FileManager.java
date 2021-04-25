package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.PortalDungeons;
import net.jxng1.portaldungeons.dungeonstructures.EditBlock;
import org.bukkit.Bukkit;
import org.bukkit.StructureType;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FileManager {

    private final PortalDungeons plugin;

    // Folders
    public File structureFolder;
    public File playerFolder;

    HashMap<File, FileConfiguration> structureFiles = new HashMap<>();

    public FileManager(PortalDungeons plugin) {
        this.plugin = plugin;

        // Create plugin folder if it doesn't exist...
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        // Create structures folder inside plugin folder if it doesn't exist...
        // TODO
        // - Have subfolders inside structures to have the different StructureTypes...
        structureFolder = new File(plugin.getDataFolder(), "structures");
        if (!structureFolder.exists()) {
            structureFolder.mkdir();
        }

        // Create players folder inside plugin folder if it doesn't exist...
        playerFolder = new File(plugin.getDataFolder(), "players");
        if (!structureFolder.exists()) {
            playerFolder.mkdir();
        }
    }

    private File createNewStructureFile(String structureName) {
        File structureFile = new File(this.structureFolder, structureName);

        if (!structureFile.exists()) {
            try {
                structureFile.createNewFile();
                Bukkit.getLogger().info("New file created with the name: " + structureName);
                return structureFile;
            } catch (IOException e) {
                Bukkit.getLogger().severe("Issue with creating new structure file!" + e);
            }
        }

        return null;
    }

    public void saveNewStructureFile(Player player, StructureManager structureManager, String structureName, StructureType structureType) {
        File structureFile;

        if ((structureFile = createNewStructureFile(structureName)) != null) {
            FileConfiguration structureConfiguration = new YamlConfiguration();

            try {
                structureConfiguration.load(structureFile);

                structureConfiguration.set("structure_name", structureName);
                structureConfiguration.set("structure_type", null);

                int i = 0;
                // CONNECTOR BLOCKS
                for (Block connector : structureManager.getConnectorBlocks()) {
                    structureConfiguration.set("blocks.connector." + i + ".X", connector.getX() - structureManager.getBaseConnector().getX());
                    structureConfiguration.set("blocks.connector." + i + ".Y", connector.getY() - structureManager.getBaseConnector().getY());
                    structureConfiguration.set("blocks.connector." + i + ".Z", connector.getZ() - structureManager.getBaseConnector().getZ());
                    i++;
                }

                // ORDINARY BLOCKS
                i = 0;
                for (EditBlock block : structureManager.getBlocksPlaced()) {
                    structureConfiguration.set("blocks.edit." + i + ".X", block.getXDistanceFromBase());
                    structureConfiguration.set("blocks.edit." + i + ".Y", block.getYDistanceFromBase());
                    structureConfiguration.set("blocks.edit." + i + ".Z", block.getZDistanceFromBase());
                    structureConfiguration.set("blocks.edit." + i + ".MATERIAL", block.getMaterial().toString());
                    i++;
                }

                // TODO
                // - Still in dev...

                structureConfiguration.save(structureFile); // SAVE FILE
            } catch (IOException | InvalidConfigurationException e) {
                Bukkit.getLogger().severe("Issue with saving to structure file!" + e);
            }
        }
    }

    public boolean isStructureFileExist(String fileName) {
        return new File(structureFolder.getPath() + "/" + fileName).exists();
    }
}
