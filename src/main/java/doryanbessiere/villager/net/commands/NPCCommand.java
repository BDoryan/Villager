package doryanbessiere.villager.net.commands;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.Commons;
import doryanbessiere.villager.net.commons.command.SimpleCommand;
import doryanbessiere.villager.net.commons.config.ConfigurationUtils;
import doryanbessiere.villager.net.commons.npc.NPC;
import doryanbessiere.villager.net.commons.npc.NPCPathfinding;
import doryanbessiere.villager.net.npc.NPCManager;
import doryanbessiere.villager.net.npc.TownManagerNPC;
import doryanbessiere.villager.net.player.VillagerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class NPCCommand extends SimpleCommand {

    public NPCCommand() {
        super("npc");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] arguments) {
        if(arguments.length >= 1){
            String arg1 =arguments[0];

            if(arg1.equalsIgnoreCase("setguide") && sender instanceof Player) {
                Player player = (Player) sender;

                ConfigurationUtils.setLocation(VillagerPlugin.getInstance().getConfig(), player.getLocation(), "npc.guide.spawn", true);
                Commons.sendMessage(player, "§7Vous avez désormais défini le point d'apparition du guide du début.");
                VillagerPlugin.saveConfiguration();
            } else if(arg1.equalsIgnoreCase("settownmanager") && sender instanceof Player) {
                Player player = (Player) sender;

                ConfigurationUtils.setLocation(VillagerPlugin.getInstance().getConfig(), player.getLocation(), "npc.town-manager.spawn", true);
                Commons.sendMessage(player, "§7Vous avez désormais défini le point d'apparition du directeur municipal du début.");
                VillagerPlugin.saveConfiguration();
            } else if(arg1.equalsIgnoreCase("debug") && sender instanceof Player) {
                Player player = (Player) sender;
                VillagerPlayer villagerPlayer = VillagerPlugin.getVillager(player);

                NPC npc = TownManagerNPC.NPC;

                npc.walk(player.getLocation());

                villagerPlayer.sendMessage("debug !");
            }

            if(arguments.length >= 2){
                String arg2 =arguments[1];

                if(arg1.equalsIgnoreCase("addpathfinding") && sender instanceof Player) {
                    Player player = (Player) sender;

                    NPCPathfinding pathfinding = new NPCPathfinding(arg2);
                    if(!pathfinding.load()){
                        Commons.sendMessage(player, "§cImpossible de trouver ce NPC !");
                        return false;
                    }

                    pathfinding.add(player.getLocation());

                    Commons.sendMessage(player, "§7Nouvelle location de parcours");
                }
            }
        }
        return false;
    }

    @Override
    public void onHelp(CommandSender sender) {

    }


}
