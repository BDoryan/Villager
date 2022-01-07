package doryanbessiere.villager.net.village;

import doryanbessiere.villager.net.exceptions.AllianceIsAlreadyDeclaredException;
import doryanbessiere.villager.net.exceptions.WarIsAlreadyDeclaredException;
import doryanbessiere.villager.net.player.VillagerPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class Village {

    private String uuid = UUID.randomUUID().toString();
    private String name;
    private String owner;

    private ArrayList<String> villager = new ArrayList<>();

    // Déclaration de guerre en cours...
    private ArrayList<String> declarations_of_war = new ArrayList<>();

    // Déclaration de d'alliance en cours...
    private ArrayList<String> declarations_of_alliance = new ArrayList<>();

    private ArrayList<String> allies = new ArrayList<>();
    private ArrayList<String> enemies = new ArrayList<>();

    public Village(String name, String owner, ArrayList<String> villager) {
        this.name = name;
        this.owner = owner;
        this.villager = villager;
        this.villager.add(this.owner);
    }

    public void declarationOfWar(VillagerPlayer villagerPlayer, Village village) throws WarIsAlreadyDeclaredException {
        if(village.getEnemies().contains(village.getUUID())){
            throw new WarIsAlreadyDeclaredException(villagerPlayer, this, village);
        }
    }

    public void declarationOfAlliance(VillagerPlayer villagerPlayer, Village village) throws AllianceIsAlreadyDeclaredException {
        if(village.getAllies().contains(village.getUUID())){
            throw new AllianceIsAlreadyDeclaredException(villagerPlayer, this, village);
        }
    }

    public boolean addVillager(VillagerPlayer player){
        if(!villager.contains(player.getUUID())){
            villager.add(player.getUUID());
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
            return true;
        }
        return false;
    }

    public String getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<String> getAllies() {
        return allies;
    }

    public ArrayList<String> getEnemies() {
        return enemies;
    }

    public ArrayList<String> getDeclarationsOfWar() {
        return declarations_of_war;
    }

    public ArrayList<String> getDeclarationsOfAlliance() {
        return declarations_of_alliance;
    }

    public ArrayList<String> getVillager() {
        return villager;
    }
}
