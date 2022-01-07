package doryanbessiere.villager.net.exceptions;

import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.village.Village;
import org.bukkit.entity.Player;

public class WarIsAlreadyDeclaredException extends Exception{

    private Village village;
    private Village target;

    public WarIsAlreadyDeclaredException(VillagerPlayer player, Village village, Village target) {
        super(player.getLang().getMessage("you_are_already_at_war_with_this_village"));

        this.village = village;
        this.target = target;
    }

    public Village getVillage() {
        return village;
    }

    public Village getTarget() {
        return target;
    }
}
