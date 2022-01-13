package doryanbessiere.villager.net.commands;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.Commons;
import doryanbessiere.villager.net.commons.Lang;
import doryanbessiere.villager.net.commons.command.SimpleCommand;
import doryanbessiere.villager.net.commons.dialog.DialogListener;
import doryanbessiere.villager.net.commons.dialog.DialogResponse;
import doryanbessiere.villager.net.commons.economy.CoinType;
import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.village.Village;
import doryanbessiere.villager.net.village.storage.StorageVillageAbstract;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VillageCommand extends SimpleCommand {

    public static final String CREATE_VILLAGE_COMMAND = "/village create";

    public VillageCommand(){
        super("village");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] arguments) {
        if(arguments.length == 0){
            onHelp(sender);
            return true;
        } else if (arguments.length >= 1){
            StorageVillageAbstract store = VillagerPlugin.getVillageStore();

            String arg1 = arguments[0];

            if(arg1.equalsIgnoreCase("create")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    VillagerPlayer villagerPlayer = VillagerPlugin.getVillager(player);
                    Lang lang = villagerPlayer.getLang();

                    if(!villagerPlayer.hasVillage()){
                        player.sendMessage("   §6» §7§o"+lang.getMessage("command_village_name_question"));
                        if(!DialogResponse.wait(player, new DialogResponse() {
                            @Override
                            public void response(String name) {
                                if(store.existsVillage(name)){
                                    player.sendMessage("§c"+lang.getMessage("command_village_name_already_used"));
                                } else {
                                    Village village = new Village(name, villagerPlayer);
                                    if(store.addVillage(village) != null){
                                        player.sendMessage("§a"+lang.getMessage("command_village_create_success"));
                                    } else {
                                        player.sendMessage("§c"+lang.getMessage("command_village_create_error"));
                                    }
                                }
                            }
                        })){
                            player.sendMessage("§c"+lang.getMessage("command_village_you_are_already_in_dialog"));
                        }
                    } else {
                        player.sendMessage("§c"+lang.getMessage("command_village_you_are_already_in_a_village"));
                        return false;
                    }
                }
                return true;
            }
            if (arguments.length >= 2){
                String arg2 = arguments[1];
                if (arguments.length >= 3){
                    String arg3 = arguments[2];
                    if (arguments.length >= 4){
                        String arg4 = arguments[3];
                    }
                }
            }
        }
        useHelp(sender);
        return false;
    }

    private void useHelp(CommandSender sender){
        Commons.sendMessage(sender, "§cCommande inconnue, utiliser: §f/money §ehelp");
    }

    @Override
    public void onHelp(CommandSender sender) {
        sender.sendMessage("§8"+ Commons.lineSeparator());
        sender.sendMessage("");
        sender.sendMessage("§6Cette commande permet de créer, supprimer ou gérer son village");
        sender.sendMessage("");
        sender.sendMessage("§7§nListe des commandes:");
        sender.sendMessage("  §f- /village create");
        sender.sendMessage("");
        sender.sendMessage("§8"+ Commons.lineSeparator());
    }
}
