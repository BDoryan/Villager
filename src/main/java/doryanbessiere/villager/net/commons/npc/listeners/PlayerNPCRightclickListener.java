package doryanbessiere.villager.net.commons.npc.listeners;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.npc.event.NPCRightClientEvent;
import doryanbessiere.villager.net.npc.GuideNPC;
import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.commons.quest.AbstractQuest;
import doryanbessiere.villager.net.quests.WelcomeQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNPCRightclickListener implements Listener  {

    @EventHandler
    public void NPCRightClientEvent_(NPCRightClientEvent event){
        Player player = event.getPlayer();
        VillagerPlayer villagerPlayer = VillagerPlugin.getVillager(player);
        GuideNPC guide = villagerPlayer.getGuide();

        if(guide != null && guide.getServerPlayer().getId() == event.getNPC().getId()){
            AbstractQuest quest = villagerPlayer.getCurrentQuest();
            if(quest != null && quest instanceof WelcomeQuest && !quest.isPlayed()){
                quest.resume(villagerPlayer);
            } else if (quest == null){
                quest = new WelcomeQuest();
                villagerPlayer.setQuest(quest);
                quest.play(villagerPlayer);
            }
        }
    }
}
