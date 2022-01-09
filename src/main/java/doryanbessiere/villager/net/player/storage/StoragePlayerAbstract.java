package doryanbessiere.villager.net.player.storage;

import doryanbessiere.villager.net.player.VillagerPlayer;
import org.bukkit.entity.Player;

public abstract class StoragePlayerAbstract {

    public boolean existsVillager(String uuidString) {
        return getVillager(uuidString) != null;
    }

    public abstract VillagerPlayer getVillager(String uuidString);
    public abstract VillagerPlayer addVillager(Player player);
    public abstract boolean updateVillager(VillagerPlayer player);
    public abstract boolean removeVillager(String uuidString);
}
