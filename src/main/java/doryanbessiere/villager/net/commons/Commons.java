package doryanbessiere.villager.net.commons;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Commons {

    public static final String PREFIX = "§8[§6Villager§8] §f";
    public static final String lineSeparator() {
        return "§7§n                                                                           ";
    }

    public static void sendMessage(CommandSender sender, String message){
        sender.sendMessage(PREFIX+message);
    }

    public static void sendMessage(Player player, String message){
        player.sendMessage(PREFIX+message);
    }

    public static String lineSeparator(String title) {
        int max = 53;
        int length = title.length() + 4;

        int diff = (max - length) / 2;

        String line = "§7";

        for(int i = 0; i < diff; i++){
            line += "-";
        }
        line += "[ §f"+title +" §7]";
        for(int i = 0; i < diff; i++){
            line += "-";
        }

        return line;
    }

    public static boolean compareLocation(Location location1, Location location2){
        return location1.getWorld().getName().equals(location2.getWorld().getName()) && location1.getBlockX() == location2.getBlockX()
                && location1.getBlockY() == location2.getBlockY()
                && location1.getBlockZ() == location2.getBlockZ();
    }
}
