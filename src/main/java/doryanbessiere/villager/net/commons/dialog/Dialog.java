package doryanbessiere.villager.net.commons.dialog;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.dialog.sentence.Sentence;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public abstract class Dialog {

    private Player player;
    private ArrayList<Sentence> sentences = new ArrayList<>();

    public Dialog(Player player) {
        this.player = player;
    }

    public void load(String... sentences){
        this.sentences.clear();
        for (String sentence : sentences) {
            this.sentences.add(new Sentence(VillagerPlugin.getVillager(player).getLang().getMessage(sentence, "%player%", player.getName())));
        }
    }

    int index = 0;
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            if(index == sentences.size()){
                finish();
                return;
            }

            talkSentence(index);
            index++;
        }
    };

    public void play(){
        index = 0;
        runnable.run();
    }

    public abstract void finish();

    public void talkSentence(int index){
        Sentence sentence = sentences.get(index);
        player.sendMessage("§e» §7§o"+sentence.getSentence());
        new BukkitRunnable(){
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(VillagerPlugin.getInstance(), sentence.getDuration());
    }
}
