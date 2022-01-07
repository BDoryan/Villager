package doryanbessiere.villager.net.commons.npc.event;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCRightClientEvent extends Event implements Cancellable {

    private final Player player;
    private final ServerPlayer npc;
    private boolean isCancelled;

    public static final HandlerList HANDLERS = new HandlerList();

    public NPCRightClientEvent(Player player, ServerPlayer npc) {
        this.player = player;
        this.npc = npc;
    }

    public ServerPlayer getNPC() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers(){
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){return HANDLERS;}

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}
