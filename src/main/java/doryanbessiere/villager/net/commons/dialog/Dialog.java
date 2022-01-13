package doryanbessiere.villager.net.commons.dialog;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.dialog.sentence.Sentence;
import doryanbessiere.villager.net.player.VillagerPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Dialog {

    public static final HashMap<VillagerPlayer, Dialog> playersInDialog = new HashMap<>();
    public static final double DIALOG_DISTANCE = 8d;

    private Location location;
    private boolean stop = false;

    public static void init(){
        PluginManager pm = Bukkit.getPluginManager();
        Plugin plugin = VillagerPlugin.getInstance();
        pm.registerEvents(new DialogListener(), plugin);
    }

    private Player player;
    private VillagerPlayer villagerPlayer;

    private ArrayList<Sentence> sentences = new ArrayList<>();
    private ArrayList<BaseComponent> interactions = new ArrayList<>();

    public Dialog(Player player) {
        this.player = player;
        this.villagerPlayer = VillagerPlugin.getVillager(player);
    }

    public Dialog(Player player, Location location) {
        this(player);
        this.location = location;
    }

    public void loadInteractions(BaseComponent... baseComponents){
        interactions.addAll(List.of(baseComponents));
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
                dialogEnd();
                return;
            }

            talkSentence(index);
            index++;
        }
    };

    public void play(){
        if(playersInDialog.containsKey(villagerPlayer)){
            villagerPlayer.sendMessage("§c"+villagerPlayer.getLang().getMessage("you_are_already_in_dialog"));
            return;
        }
        index = 0;
        runnable.run();
        playersInDialog.put(villagerPlayer, this);
    }

    private void dialogEnd(){
        interactions.forEach(interaction -> {
            TextComponent message = new TextComponent("   §6» §7§o");
            message.addExtra(interaction);
            player.spigot().sendMessage(message);
        });
        playersInDialog.remove(villagerPlayer);
        end();
        dialogFinish();
    }

    public abstract void end();

    public void talkSentence(int index){
        Sentence sentence = sentences.get(index);
        player.sendMessage("§e» §7§o"+sentence.getSentence());
        new BukkitRunnable(){
            @Override
            public void run() {
                if(isStopped())return;
                runnable.run();
            }
        }.runTaskLater(VillagerPlugin.getInstance(), sentence.getDuration());
    }

    public void setStop(boolean stop) {
        this.stop = stop;
        dialogFinish();
    }

    public void dialogFinish(){
        playersInDialog.remove(villagerPlayer);
        finish();
    }

    public abstract void finish();

    public boolean isStopped() {
        return stop;
    }

    public Location getLocation() {
        return location;
    }
}
