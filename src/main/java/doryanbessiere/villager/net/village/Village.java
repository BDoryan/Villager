package doryanbessiere.villager.net.village;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.exceptions.AllianceIsAlreadyDeclaredException;
import doryanbessiere.villager.net.exceptions.WarIsAlreadyDeclaredException;
import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.player.storage.StoragePlayerAbstract;
import doryanbessiere.villager.net.village.storage.StorageVillageAbstract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Village {

    private String name;
    private String ownerUUID;

    private int score;

    private ArrayList<String> villager = new ArrayList<>();

    public Village(String name, VillagerPlayer owner, ArrayList<String> villager) {
        this.name = name;
        this.ownerUUID = owner.getUUID();
        this.villager = villager;
        this.villager.add(this.ownerUUID);

        owner.setVillage(name);
    }

    public Village(String name, String ownerUUID) {
        this(name, VillagerPlugin.getVillagerPlayerStore().getVillager(ownerUUID));
    }

    public Village(String name, VillagerPlayer villagerPlayer) {
        this(name, villagerPlayer, new ArrayList<>());
    }

    public boolean addVillager(VillagerPlayer player){
        if(!villager.contains(player.getUUID())){
            villager.add(player.getUUID());
            update();
            return true;
        }
        return false;
    }

    public boolean containsVillager(VillagerPlayer player){
        return villager.contains(player.getUUID());
    }

    public boolean removeVillager(VillagerPlayer player){
        if(villager.contains(player.getUUID())){
            villager.remove(player.getUUID());
            player.setVillage(null);
            update();
            return true;
        }
        return false;
    }

    public void update(){
        StorageVillageAbstract storage = VillagerPlugin.getVillageStore();
        storage.updateVillage(this);
    }

    public String getName() {
        return name;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public ArrayList<String> getVillager() {
        return villager;
    }
}
