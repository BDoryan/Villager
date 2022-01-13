package doryanbessiere.villager.net.commons.npc.listeners;

import doryanbessiere.villager.net.commons.npc.NPC;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void PlayerMoveEvent_(PlayerMoveEvent event){
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        ArrayList<NPC> NPCs = NPC.getPlayerNPCs(player);
        if(NPCs.size() > 0){
            NPCs.stream().filter(npc -> npc.hasLookAtMe() && !npc.isWalking()).forEach(npc -> {
                Location location = npc.getServerPlayer().getBukkitEntity().getLocation();
                location.setDirection(event.getPlayer().getLocation().subtract(location).toVector());
                float yaw = location.getYaw();
                float pitch = location.getPitch();

                ServerGamePacketListenerImpl ps = ((CraftPlayer)event.getPlayer()).getHandle().connection;

                ps.send(new ClientboundRotateHeadPacket(npc.getServerPlayer(), (byte) ((yaw%360)* 256/360)));
                ps.send(new ClientboundMoveEntityPacket.Rot(npc.getServerPlayer().getBukkitEntity().getEntityId(), (byte) ((yaw%360)* 256/360), (byte) ((pitch%360)* 256/360), false));
            });
        }
    }
}
