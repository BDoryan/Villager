package doryanbessiere.villager.net.commons.dialog;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.Commons;
import doryanbessiere.villager.net.commons.npc.NPC;
import doryanbessiere.villager.net.player.VillagerPlayer;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogListener implements Listener {

    @EventHandler
    public void AsyncPlayerChatEvent_(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        VillagerPlayer villagerPlayer = VillagerPlugin.getVillager(player);

        DialogResponse response = DialogResponse.waitingResponse.get(player);
        if(response != null){
            DialogResponse.waitingResponse.remove(player);
            response.response(event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerMoveEvent_(PlayerMoveEvent event){
        Player player = event.getPlayer();
        VillagerPlayer villagerPlayer = VillagerPlugin.getVillager(player);

        Location from = event.getFrom();
        Location to = event.getTo();

        // Cette fonction permet d'éviter de calculer lorsque le joueur ne se déplace pas à plus d'un blocs (pour évite le lag serveur)
        if(from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()){
            return;
        }

        Dialog dialog = Dialog.playersInDialog.get(villagerPlayer);
        if(dialog != null && dialog.getLocation() != null && dialog.getLocation().distanceSquared(player.getLocation()) > Dialog.DIALOG_DISTANCE){
            villagerPlayer.sendMessage("§c"+villagerPlayer.getLang().getMessage("distance_stop_dialog"));
            dialog.setStop(true);
        }
    }
}
