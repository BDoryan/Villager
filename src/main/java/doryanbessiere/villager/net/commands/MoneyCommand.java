package doryanbessiere.villager.net.commands;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.Commons;
import doryanbessiere.villager.net.commons.command.SimpleCommand;
import doryanbessiere.villager.net.commons.economy.CoinType;
import doryanbessiere.villager.net.commons.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoneyCommand extends SimpleCommand {

    public MoneyCommand(){
        super("money");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] arguments) {
        if(arguments.length == 0){
            onHelp(sender);
            return true;
        } else if (arguments.length >= 1){
            String arg1 = arguments[0];
            if (arguments.length >= 2){
                String arg2 = arguments[1];
                if (arguments.length >= 3){
                    String arg3 = arguments[2];
                    if (arguments.length >= 4){
                        String arg4 = arguments[3];

                        if(arg1.equalsIgnoreCase("give")) {
                            Player player = Bukkit.getPlayer(arg2);
                            if (player == null) {
                                Commons.sendMessage(sender, "§cCe joueur n'est pas connecté !");
                                return false;
                            }

                            CoinType coinType = CoinType.fromString(arg3);

                            if (coinType == null) {
                                Commons.sendMessage(sender, "§cCe type de monnaie est inconnue !");
                                return false;
                            }

                            try {
                                Integer total = Integer.valueOf(arg4);
                                Integer amount = total;

                                while(amount > 0){
                                    int count = 64;
                                    if(amount < count)
                                        count = amount;

                                    ItemStack itemStack = coinType.getItemStack();
                                    itemStack.setAmount(count);

                                    player.getInventory().addItem(itemStack);

                                    amount -= count;
                                }

                                Commons.sendMessage(sender, "§6Vous avez donnée §e"+total+" §6de §e"+(coinType.getItemStack().getItemMeta().getDisplayName().toLowerCase())+" §6à §e"+player.getName());

                                return true;
                            } catch (Exception e){
                                Commons.sendMessage(sender, "§cCette quatité d'argent n'est pas un nombre !");
                                return false;
                            }
                        }
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
        sender.sendMessage("§6Cette commande permet uniquement de donné de l'argent physique");
        sender.sendMessage("");
        sender.sendMessage("§7§nListe des commandes:");
        sender.sendMessage("  §f- /money give §e<joueur> <silver/gold> <quantité>");
        sender.sendMessage("");
        sender.sendMessage("§8"+ Commons.lineSeparator());
    }
}
