package doryanbessiere.villager.net.npc;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.config.ConfigurationUtils;
import doryanbessiere.villager.net.commons.npc.NPC;
import doryanbessiere.villager.net.commons.quest.AbstractQuest;
import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.quests.WelcomeQuest;
import doryanbessiere.villager.net.village.Village;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GuideNPC extends NPC {

    public GuideNPC(String name, String textures, String signature, boolean lookAtMe) {
        super(name, textures, signature, lookAtMe);
    }

    public void spawn(VillagerPlayer player){
        spawn(ConfigurationUtils.getLocation(VillagerPlugin.getInstance().getConfig(), "npc.guide.spawn"), player.getPlayer());
    }

    @Override
    public void click(Player player) {
        super.click(player);

        VillagerPlayer villagerPlayer = VillagerPlugin.getVillager(player);

        AbstractQuest quest = villagerPlayer.getCurrentQuest();
        if(quest != null && quest instanceof WelcomeQuest && !quest.isPlayed()){
            quest.resume(villagerPlayer);
        } else if (quest == null){
            quest = new WelcomeQuest();
            villagerPlayer.setQuest(quest);
            quest.play(villagerPlayer);
        }
    }

    public static GuideNPC create()
    {
        FileConfiguration config = VillagerPlugin.getInstance().getConfig();
        return new GuideNPC("§8[Guide] §7Bob", config.getString("npc.guide.textures"), config.getString("npc.guide.signature"), true);
    }
}
