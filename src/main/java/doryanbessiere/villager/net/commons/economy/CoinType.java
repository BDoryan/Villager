package doryanbessiere.villager.net.commons.economy;

import doryanbessiere.villager.net.items.CustomItems;
import org.bukkit.inventory.ItemStack;

public enum CoinType {

    SILVER(CustomItems.SILVER_COIN),
    GOLD(CustomItems.GOLD_COIN);

    private ItemStack itemStack;

    CoinType(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static CoinType fromString(String string){
        for (CoinType value : CoinType.values()) {
            if(value.toString().equalsIgnoreCase(string))
                return value;
        }
        return null;
    }
}
