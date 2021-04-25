package net.jxng1.portaldungeons.managers;

import net.jxng1.portaldungeons.dungeonstructures.EditBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StructureManager {

    private static final int MAX_CONNECTORS = 4;
    private PlayerEditState playerEditState;

    private List<Block> connectorBlocks = new ArrayList<>();
    private Set<EditBlock> blocks = new HashSet<>();

    public StructureManager() {
        this.playerEditState = PlayerEditState.DISABLED;
    }

    public PlayerEditState getPlayerEditState() {
        return this.playerEditState;
    }

    public void setPlayerEditState(PlayerEditState playerEditState) {
        this.playerEditState = playerEditState;
    }

    public List<Block> getConnectorBlocks() {
        return this.connectorBlocks;
    }

    public void addConnectorBlock(Block block) {
        connectorBlocks.add(block);
    }

    public int numberOfConnectorsPlaced() {
        return connectorBlocks.size();
    }

    public Block getBaseConnector() {
        try {
            return connectorBlocks.get(0);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to get non-existent base connector!");
        }

        return null;
    }

    public Set<EditBlock> getBlocksPlaced() {
        return blocks;
    }

    public void addEditBlock(Block block, int xDistanceFromBase, int yDistanceFromBase, int zDistanceFromBase, Material material) {
        blocks.add(new EditBlock(block, xDistanceFromBase, yDistanceFromBase, zDistanceFromBase, material));
    }

    public void removeEditBlock(Block block) {
        for (EditBlock editBlock : this.blocks) {
            if (editBlock.getBlock().equals(block)) {
                try {
                    this.blocks.remove(editBlock);
                } catch (NullPointerException e) {
                    Bukkit.getLogger().warning("Error with removing EditBlock!");
                }
            }
        }
    }

    public boolean isEmpty() {
        return blocks.isEmpty() || connectorBlocks.isEmpty();
    }
}
