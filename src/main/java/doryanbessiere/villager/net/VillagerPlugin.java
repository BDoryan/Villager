package doryanbessiere.villager.net;

import doryanbessiere.villager.net.commands.MoneyCommand;
import doryanbessiere.villager.net.commands.NPCCommand;
import doryanbessiere.villager.net.commons.Lang;
import doryanbessiere.villager.net.commons.command.CommandManager;
import doryanbessiere.villager.net.commons.npc.NPC;
import doryanbessiere.villager.net.listener.ListenerManager;
import doryanbessiere.villager.net.listener.listeners.PlayerConnectionListener;
import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.player.storage.StoragePlayerAbstract;
import doryanbessiere.villager.net.player.storage.StoragePlayerByFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class VillagerPlugin extends JavaPlugin {

    private static VillagerPlugin instance;
    private static StoragePlayerAbstract villagerPlayerStore;
    private static ArrayList<VillagerPlayer> villagerPlayers = new ArrayList<>();

    private static HashMap<String, Lang> langs = new HashMap<>();

    @Override
    public void onLoad() {
        super.onLoad();

        instance = this;
        villagerPlayerStore = new StoragePlayerByFile();
    }

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        super.onEnable();

        commandManager = new CommandManager(this) {
            @Override
            public void loadCommands() {
                setCommand(new NPCCommand());
                setCommand(new MoneyCommand());
            }
        };
        commandManager.defaultCommand();
        commandManager.loadCommands();

        ListenerManager.registerListeners();
        NPC.init();

        langs.put("fr_fr", new Lang("fr_fr"));

        Bukkit.getOnlinePlayers().forEach((player -> {
            PlayerConnectionListener.join(player);
        }));
    }

    public static Lang getLang(String lang) {
        return langs.get(lang);
    }

    @Override
    public void onDisable() {
        NPC.removeAllNPCs();
        this.saveDefaultConfig();
        super.onDisable();
    }

    public static VillagerPlugin getInstance() {
        return instance;
    }

    public static StoragePlayerAbstract getVillagerPlayerStore() {
        return villagerPlayerStore;
    }

    public static ArrayList<VillagerPlayer> getVillagerPlayers() {
        return villagerPlayers;
    }

    public static VillagerPlayer getVillager(Player player){
        return villagerPlayers.stream().filter(villager -> villager.getUUID().equalsIgnoreCase(player.getUniqueId().toString())).findFirst().get();
    }

    public static void saveConfiguration() {
        instance.saveConfig();
    }
}
