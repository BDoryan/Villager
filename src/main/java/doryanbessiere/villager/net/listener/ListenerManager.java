package doryanbessiere.villager.net.listener;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.listener.listeners.PlayerConnectionListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    public static void registerListeners(){
        Plugin plugin = VillagerPlugin.getInstance();
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new PlayerConnectionListener(), plugin);
    }

}
