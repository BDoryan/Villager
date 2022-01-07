package doryanbessiere.villager.net.commons;

import org.bukkit.Bukkit;

import java.util.ArrayList;

public class Logger {

    private static ArrayList<String> logs = new ArrayList<>();

    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage("§7[§eVillager§7] "+message);
    }

    public void save(){

    }
}
