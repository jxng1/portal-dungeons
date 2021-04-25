package net.jxng1.portaldungeons.dungeonstructures;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class EditBlock {

    Block block;
    int xDistanceFromBase;
    int yDistanceFromBase;
    int zDistanceFromBase;

    Material material;

    public EditBlock(Block block, int xDistanceFromBase, int yDistanceFromBase, int zDistanceFromBase, Material material) {
        this.block = block;
        this.xDistanceFromBase = xDistanceFromBase;
        this.yDistanceFromBase = yDistanceFromBase;
        this.zDistanceFromBase = zDistanceFromBase;
        this.material = material;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public int getXDistanceFromBase() {
        return xDistanceFromBase;
    }

    public void setXDistanceFromBase(int xDistanceFromBase) {
        this.xDistanceFromBase = xDistanceFromBase;
    }

    public int getYDistanceFromBase() {
        return yDistanceFromBase;
    }

    public void setYDistanceFromBase(int yDistanceFromBase) {
        this.yDistanceFromBase = yDistanceFromBase;
    }

    public int getZDistanceFromBase() {
        return zDistanceFromBase;
    }

    public void setZDistanceFromBase(int zDistanceFromBase) {
        this.zDistanceFromBase = zDistanceFromBase;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
