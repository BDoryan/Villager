package doryanbessiere.villager.net.commons.packet;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.npc.NPC;
import doryanbessiere.villager.net.commons.npc.event.NPCRightClientEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PacketReader {

    Channel channel;

    public HashMap<UUID, Channel> channels = new HashMap<UUID, Channel>();

    public void inject(Player player) {
        CraftPlayer craftPlayer = ((CraftPlayer) player);

        channel = craftPlayer.getHandle().connection.getConnection().channel;
        channels.put(player.getUniqueId(), channel);

        if(channel.pipeline().get("PacketInjector") != null)return;

        if(!player.isOnline())return;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, List<Object> arg) throws Exception {
                arg.add(packet);

                readPacket(player, packet);
            }
        });
    }

    public void uninject(Player player){
        channel = channels.get(player.getUniqueId());
        if(channel.pipeline().get("PacketInjector") != null)
            channel.pipeline().remove("PacketInjector");
    }

    public void readPacket(Player player, Packet<?> packet){
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")){
            read(player, (PacketPlayInUseEntity) packet);
        }
    }

    private int count = 0;

    public void read(Player player, PacketPlayInUseEntity packet){
            count = 0;
            int id = (int) getValue(packet, "a");
            NPC.STORAGES.stream().filter(npc -> npc.getServerPlayer().getId() == id).forEach(npc -> {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(id == npc.getId()){
                            Bukkit.getPluginManager().callEvent(new NPCRightClientEvent(player, npc));
                        }
                    }
                }.runTask(VillagerPlugin.getInstance());
            });
    }

    public Object getValue(Object instance, String name){
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
