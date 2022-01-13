package doryanbessiere.villager.net.quests;

import doryanbessiere.villager.net.commons.Commons;
import doryanbessiere.villager.net.commons.npc.NPCPathfinding;
import doryanbessiere.villager.net.commons.dialog.Dialog;
import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.commons.quest.AbstractQuest;
import doryanbessiere.villager.net.commons.quest.QuestData;
import org.bukkit.entity.Player;

public class WelcomeQuest extends AbstractQuest {

    public WelcomeQuest() {
        super(0, "welcome_quest", 3);
    }

    public WelcomeQuest(QuestData data) {
        super(data);
    }

    @Override
    public void play(VillagerPlayer villagerPlayer){
        play = true;

        play(villagerPlayer, 0);
    }

    @Override
    public void resume(VillagerPlayer villagerPlayer) {
        play = true;

        play(villagerPlayer, localStepIndex);
    }

    private int localStepIndex = 0;

    private void play(VillagerPlayer villagerPlayer, int index){
        if(!villagerPlayer.isOnline())return;
        if(index == 0){
            first(villagerPlayer);
        }
        if(index == 1){
            second(villagerPlayer);
        }
        if(index == 2){
            three(villagerPlayer);
        }
        if(index == this.step){
            stop(villagerPlayer);
        }
    }

    @Override
    public void next(VillagerPlayer villagerPlayer) {
        localStepIndex++;
        //if(localStepIndex == step)stop(villagerPlayer);

        villagerPlayer.update();
        play(villagerPlayer, localStepIndex);
    }

    @Override
    public void stop(VillagerPlayer villagerPlayer) {
        play = false;
        villagerPlayer.getQuestDone().add(getId());
    }

    public void first(VillagerPlayer villagerPlayer){
        Player player = villagerPlayer.getPlayer();

        Dialog dialog = new Dialog(player){
            @Override
            public void finish() {
            }

            @Override
            public void end() {
                next(villagerPlayer);
            }
        };
        dialog.load("dialog_guide_sentence_1", "dialog_guide_sentence_2", "dialog_guide_sentence_3");
        dialog.play();
    }

    public void second(VillagerPlayer villagerPlayer){
        Player player = villagerPlayer.getPlayer();

        NPCPathfinding pathfinding = new NPCPathfinding("guide");
        if (!pathfinding.load()) {
            Commons.sendMessage(player, "§7[Erreur] §cLa quête rencontre un problème, le chemin que dois parcourir le PNJ est introuvable");
            return;
        }

        pathfinding.play(villagerPlayer.getGuide(), new Runnable() {
            @Override
            public void run() {
                next(villagerPlayer);
            }
        });
    }
    public void three(VillagerPlayer villagerPlayer){
        Player player = villagerPlayer.getPlayer();
        Dialog dialog = new Dialog(player){
            @Override
            public void finish() {

            }

            @Override
            public void end() {
                next(villagerPlayer);
            }
        };
        dialog.load("dialog_guide_sentence_4", "dialog_guide_sentence_5", "dialog_guide_sentence_6");
        dialog.play();
    }
}
