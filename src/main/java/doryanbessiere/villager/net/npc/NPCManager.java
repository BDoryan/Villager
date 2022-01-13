package doryanbessiere.villager.net.npc;

import doryanbessiere.villager.net.player.VillagerPlayer;

public class NPCManager {

    public static void initialize(VillagerPlayer player){
        TownManagerNPC.NPC.spawn(player);
    }

    public static void uninitialize(VillagerPlayer player){
        TownManagerNPC.NPC.unspawn(player.getPlayer());
    }
}
