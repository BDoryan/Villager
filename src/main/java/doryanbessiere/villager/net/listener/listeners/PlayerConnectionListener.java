package doryanbessiere.villager.net.listener.listeners;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.store.StoragePlayerAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent_(PlayerJoinEvent event){
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        join(player);
    }

    public static void join(Player player){
        StoragePlayerAbstract storage = VillagerPlugin.getVillagerPlayerStore();
        VillagerPlayer villagerPlayer = null;
        if((villagerPlayer = storage.getVillager(player.getUniqueId().toString())) == null){
            villagerPlayer = storage.addVillager(player);
            villagerPlayer.welcome();
        }
        villagerPlayer.connect();
    }

    @EventHandler
    public void PlayerQuitEvent_(PlayerQuitEvent event){
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        VillagerPlayer villagerPlayer = VillagerPlugin.getVillager(player);
        villagerPlayer.disconnect();
    }
}
