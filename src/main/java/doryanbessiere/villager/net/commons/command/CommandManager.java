package doryanbessiere.villager.net.commons.command;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.Commons;
import doryanbessiere.villager.net.commons.config.ConfigurationUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public abstract class CommandManager {

    private ArrayList<SimpleCommand> commands = new ArrayList<>();
    private JavaPlugin plugin;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void defaultCommand(){
        setCommand(new SimpleCommand(plugin.getName()) {
            @Override
            public boolean onExecute(CommandSender sender, String[] arguments) {
                if(arguments.length == 1 && arguments[0].equalsIgnoreCase("help")){
                    onHelp(sender);
                } else {
                    if(arguments.length >= 1){
                        String arg1 = arguments[0];
                        if(arg1.equalsIgnoreCase("setspawn") && sender instanceof Player) {
                            Player player = (Player) sender;

                            ConfigurationUtils.setLocation(VillagerPlugin.getInstance().getConfig(), player.getLocation(), "spawn", true);
                            Commons.sendMessage(player, "§7Vous avez désormais défini le point d'apparition du plugin.");
                            VillagerPlugin.saveConfiguration();
                        } else if(arg1.equalsIgnoreCase("spawn") && sender instanceof Player){
                            Player player = (Player) sender;

                            player.teleport(ConfigurationUtils.getLocation(VillagerPlugin.getInstance().getConfig(), "spawn"));
                            Commons.sendMessage(player, "§7Vous avez été téléporté au point d'apparition du plugin.");
                        } else if(arg1.equalsIgnoreCase("setguide") && sender instanceof Player) {
                            Player player = (Player) sender;

                            ConfigurationUtils.setLocation(VillagerPlugin.getInstance().getConfig(), player.getLocation(), "guide", true);
                            Commons.sendMessage(player, "§7Vous avez désormais défini le point d'apparition du guide du début.");
                            VillagerPlugin.saveConfiguration();
                        }
                    }
                }
                return true;
            }

            @Override
            public void onHelp(CommandSender sender) {
                sender.sendMessage("§7");
            }
        });
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ArrayList<SimpleCommand> getCommands() {
        return commands;
    }

    public void setCommand(SimpleCommand command){
        plugin.getCommand(command.getCommand()).setExecutor(command);
        commands.add(command);
    }

    public abstract void loadCommands();

}
