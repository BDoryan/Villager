package doryanbessiere.villager.net.item;

import doryanbessiere.villager.net.commons.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItems {

    public static final ItemStack GOLD_COIN = new ItemBuilder(Material.EMERALD).displayname("§ePièce d'or").customModelData(1).build();
    public static final ItemStack SILVER_COIN = new ItemBuilder(Material.EMERALD).displayname("§7Pièce d'argent").customModelData(2).build();

}
