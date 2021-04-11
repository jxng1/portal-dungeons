package net.jxng1.portaldungeons.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemGenerator {

    private static final Material[] MATERIALS = Material.values();

    public static List<ItemStack> generateItems() {
        List<ItemStack> items = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < random.nextInt(6) + 1; i++) {
            ItemStack item = new ItemStack(MATERIALS[random.nextInt(MATERIALS.length)], random.nextInt(5) + 1);

            items.add(item);
        }

        return items;
    }
}
