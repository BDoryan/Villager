package doryanbessiere.villager.net.player;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.Lang;
import doryanbessiere.villager.net.commons.config.ConfigurationUtils;
import doryanbessiere.villager.net.commons.dialog.Dialog;
import doryanbessiere.villager.net.commons.packet.PacketReader;
import doryanbessiere.villager.net.npc.GuideNPC;
import doryanbessiere.villager.net.npc.NPCManager;
import doryanbessiere.villager.net.npc.TownManagerNPC;
import doryanbessiere.villager.net.player.storage.StoragePlayerAbstract;
import doryanbessiere.villager.net.commons.quest.AbstractQuest;
import doryanbessiere.villager.net.commons.quest.QuestData;
import doryanbessiere.villager.net.village.Village;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VillagerPlayer {

    private String lang = "fr_fr";

    private String UUID;
    private String username;

    private long firstConnection;
    private long lastConnection;

    private String villageName;

    private boolean tutoSkipped = false;
    private QuestData questData;
    private ArrayList<Integer> questDone = new ArrayList<>();

    public VillagerPlayer(Player player){
        this.UUID = player.getUniqueId().toString();
        this.username = player.getName();
        this.firstConnection = System.currentTimeMillis();
    }

    public void disconnect(){
        uninitialize();

        this.lastConnection = System.currentTimeMillis();
        update();
    }

    private transient PacketReader packetReader = new PacketReader();
    private transient AbstractQuest quest;
    private transient GuideNPC guide;

    public void connect(){
        initialize();

        this.lastConnection = System.currentTimeMillis();

        if(this.questData != null){
            Class<AbstractQuest> questClass = (Class<AbstractQuest>) AbstractQuest.QUESTS[questData.getId()];
            try {
                this.quest = questClass.getConstructor(QuestData.class).newInstance(questData);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        update();

        // IN GAME
        if(!tutoSkipped){
            Player player = getPlayer();
            player.teleport(ConfigurationUtils.getLocation(VillagerPlugin.getInstance().getConfig(), "spawn"));

            guide = GuideNPC.create();
            guide.spawn(this);
        }
    }

    public void welcome(){
    }

    public void uninitialize(){
        VillagerPlugin.getVillagerPlayers().remove(this);

        this.packetReader.uninject(getPlayer());

        NPCManager.uninitialize(this);

        if(Dialog.playersInDialog.containsKey(this)){
            Dialog.playersInDialog.get(this).setStop(true);
        }
    }

    public void initialize(){
        VillagerPlugin.getVillagerPlayers().add(this);

        this.packetReader = new PacketReader();
        this.packetReader.inject(getPlayer());

        NPCManager.initialize(this);
    }

    public void update(){
        StoragePlayerAbstract storage = VillagerPlugin.getVillagerPlayerStore();
        storage.updateVillager(this);
    }

    public GuideNPC getGuide() {
        return guide;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(java.util.UUID.fromString(UUID));
    }

    public long getLastConnection() {
        return lastConnection;
    }

    public long getFirstConnection() {
        return firstConnection;
    }

    public void update(Player player){
        this.username = player.getName();
    }

    public String getUUID() {
        return UUID;
    }

    public Lang getLang() {
        return VillagerPlugin.getLang(lang);
    }

    public void sendMessage(String message) {
        getPlayer().sendMessage(message);
    }

    public boolean isOnline(){
        return getPlayer() != null;
    }

    public ArrayList<Integer> getQuestDone() {
        return questDone;
    }

    public void setQuest(AbstractQuest quest) {
        this.quest = quest;
        this.questData = (QuestData) quest;
        update();
    }

    public void setVillage(String villageName) {
        this.villageName = villageName;
        update();
    }

    public QuestData getQuestData() {
        return questData;
    }

    public boolean hasVillage(){
        return villageName != null;
    }

    public Village getVillage(){
        return VillagerPlugin.getVillageStore().getVillage(getVillageName());
    }

    public String getVillageName() {
        return villageName;
    }

    public AbstractQuest getCurrentQuest() {
        return quest;
    }
}
