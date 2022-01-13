package doryanbessiere.villager.net.village.storage;

import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.village.Village;
import org.bukkit.entity.Player;

public abstract class StorageVillageAbstract {

    public boolean existsVillage(String name) {
        return getVillage(name) != null;
    }

    public abstract Village getVillage(String name);
    public abstract Village addVillage(Village village);
    public abstract boolean updateVillage(Village player);

}
