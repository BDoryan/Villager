package doryanbessiere.villager.net.commons.npc;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.config.ConfigurationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

public class NPCPathfinding {

    private ArrayList<Location> paths = new ArrayList<Location>();

    private String npc;
    private String path;

    public NPCPathfinding(String npc){
        this.npc = npc;
        this.path = "npc."+npc+".pathfinding";
    }

    public boolean load(){
        paths.clear();

        FileConfiguration config = VillagerPlugin.getInstance().getConfig();

        if(!config.contains("npc."+npc))
            return false;

        if(!config.contains("npc."+npc+".pathfinding"))
            return true;

        config.getConfigurationSection(path).
                getKeys(false).forEach((key -> {
            paths.add(ConfigurationUtils.getLocation(config, path+"."+key));
        }));
        return true;
    }

    public void add(Location location){
        FileConfiguration config = VillagerPlugin.getInstance().getConfig();

        ConfigurationUtils.setLocation(config, location, path+"."+ UUID.randomUUID());
        VillagerPlugin.saveConfiguration();
    }

    private int index;

    public void play(NPC npc){
        play(npc, null);
    }

    public void play(NPC npc, Runnable finish){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                playIndex(npc, index, this, finish);
                index++;
            }
        };

        runnable.run();
    }

    private void playIndex(NPC npc, int index, Runnable runnable, Runnable finish){
        if(index == paths.size()){
            if(finish != null)finish.run();
            return;
        }
        npc.walk(paths.get(index), runnable);
    }
}
