package doryanbessiere.villager.net.commons.dialog;

import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class DialogResponse {

    public static HashMap<Player, DialogResponse> waitingResponse = new HashMap<>();

    private Player player;

    public abstract void response(String response);

    public Player getPlayer() {
        return player;
    }

    public static boolean wait(Player player, DialogResponse response){
        if(!waitingResponse.containsKey(player)){
            waitingResponse.put(player, response);
            return true;
        }
        return false;
    }
}
