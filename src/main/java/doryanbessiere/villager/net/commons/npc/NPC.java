package doryanbessiere.villager.net.commons.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commons.npc.event.NPCRightClientEvent;
import doryanbessiere.villager.net.commons.npc.listeners.PlayerMoveListener;
import doryanbessiere.villager.net.commons.npc.listeners.PlayerNPCRightclickListener;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.scores.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class NPC {

    public static final HashMap<Player, ArrayList<NPC>> NPCs = new HashMap<>();
    public static final ArrayList<NPC> STORAGES = new ArrayList<>();

    public static HashMap<Player, ArrayList<NPC>> getNPCs(){
        return NPCs;
    }

    public static ArrayList<NPC> getPlayerNPCs(Player player){
        if(!NPCs.containsKey(player)){
            NPCs.put(player, new ArrayList<>());
        }
        return NPCs.get(player);
    }

    public static void removeAllNPCs(){
        NPCs.values().forEach(list -> {
            if(list != null){
                ArrayList<NPC> listCopy = new ArrayList<>();
                listCopy.addAll(list);
                for (NPC npc : listCopy) {
                    if(npc != null)
                        npc.remove();
                }
            }
        });
    }

    public static void init(){
        Plugin plugin = VillagerPlugin.getInstance();
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new PlayerMoveListener(), plugin);
        pm.registerEvents(new PlayerNPCRightclickListener(), plugin);
    }

    private String prefix;
    private String suffix;

    private String name;
    private String textures;
    private String signature;

    private boolean lookAtMe = false;
    private boolean walking = false;

    public NPC(String name) {
        this.name = name;
    }

    public NPC(String name, String textures, String signature) {
        this.name = name;
        this.textures = textures;
        this.signature = signature;
    }

    public NPC(String name, String textures, String signature, boolean lookAtMe) {
        this.name = name;
        this.textures = textures;
        this.signature = signature;
        this.lookAtMe = lookAtMe;
    }

    private Player[] players;

    private GameProfile gameProfile;
    private ServerPlayer serverPlayer;

    public void spawn(Location location, Player... players){
        this.players = players;

        this.gameProfile= new GameProfile(UUID.randomUUID(), name);

        Arrays.stream(players).forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            ServerPlayer sp = craftPlayer.getHandle();
            MinecraftServer server = sp.getServer();
            ServerLevel level = sp.getLevel();

            serverPlayer = new ServerPlayer(server, level, gameProfile);

            if(textures != null && signature != null)
                gameProfile.getProperties().put("textures", new Property("textures", textures, signature));

            serverPlayer.setPos(location.getX(), location.getY(), location.getZ());

            ServerGamePacketListenerImpl ps = sp.connection;


            ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, serverPlayer));
            ps.send(new ClientboundAddPlayerPacket(serverPlayer));

            getPlayerNPCs(player).add(this);
            STORAGES.add(this);
        });
    }

    public void remove(){
        Arrays.stream(players).forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            ServerPlayer sp = craftPlayer.getHandle();
            ServerGamePacketListenerImpl ps = sp.connection;
            ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, this.getServerPlayer()));
            ps.send(new PacketPlayOutEntityDestroy(serverPlayer.getId()));
            getPlayerNPCs(player).remove(this);
            STORAGES.remove(this);
        });
    }

    public void walk(Location location) {
        walk(location, 0.35f);
    }

    public void walk(Location location, double speed) {
        walk(location, speed, null);
    }

    public void walk(Location location, Runnable finish) {
        walk(location, 0.35f, finish);
    }

    public void walk(Location location, double speed, Runnable finish) {
        ServerPlayer npc = getServerPlayer();
        Entity entity = npc.getBukkitEntity();

        float[] headDirection = getHeadDirection(location.clone().subtract(entity.getLocation()).toVector());
        new BukkitRunnable() {
            public void run() {
                float[] rotations = getRotations(entity.getLocation(), location);
                if(entity.getLocation().distance(location) > 1) {

                    Vector direction = new Vector(-Math.sin(rotations[0] * 3.1415927F / 180.0F) * (float) 1 * 0.5F, 0, Math.cos(rotations[0] * 3.1415927F / 180.0F) * (float) 1 * 0.5F).multiply(speed);

                    if(entity.getLocation().getY() - location.getY() > 0 && npc.getBukkitEntity().isOnGround()) {
                        direction.setY(Math.min(0.42, entity.getLocation().getY() - location.getY()));
                    }

                    Location from = entity.getLocation();
                    Location to = entity.getLocation().clone().add(direction);
                    Location ground = entity.getLocation().clone().add(0, -1, 0);

                    if(!to.getBlock().isPassable()){
                        to.add(0, 1, 0);
                    } else {
                        while (ground.getBlock().getType() == Material.AIR){
                            ground.add(0, -1, 0);
                            to.add(0, -1, 0);
                        }
                        if(ground.getBlock().isPassable()){
                            to.add(0, -0.5, 0);
                        }
                    }

                    npc.setPos(to.getX(), to.getY(), to.getZ());
                    location.getWorld().spawnParticle(Particle.HEART, to, 100);
                    move(from, to, (byte) ((headDirection[0]%360)* 256/360), (byte) ((headDirection[1]%360)* 256/360));

                    walking = true;

                } else {
                    this.cancel();
                    if(finish != null) finish.run();

                    walking = false;
                }
            }
        }.runTaskTimer(VillagerPlugin.getInstance(), 0L, 1L);
    }

    @NotNull
    public float[] getHeadDirection(@NotNull Vector vector) {
        double _2PI = 6.283185307179586D;
        double x = vector.getX();
        double z = vector.getZ();
        float yaw = 0f;
        float pitch = 0f;
        if (x == 0.0D && z == 0.0D) {
            pitch = (float)(vector.getY() > 0.0D ? -90 : 90);
        } else {
            double theta = Math.atan2(-x, z);
            yaw = (float)Math.toDegrees((theta + 6.283185307179586D) % 6.283185307179586D);
            double x2 = NumberConversions.square(x);
            double z2 = NumberConversions.square(z);
            double xz = Math.sqrt(x2 + z2);
            pitch = (float)Math.toDegrees(Math.atan(-vector.getY() / xz));
        }

        return new float[]{yaw, pitch};
    }

    private void move(Location from, Location to, byte yaw, byte pitch) {
        short x = 0;
        short y = 0;
        short z = 0;

        if (to != null) {
            x = (short) ((to.getX() * 32 - from.getX() * 32) * 128);
            y = (short) ((to.getY() * 32 - from.getY() * 32) * 128);
            z = (short) ((to.getZ() * 32 - from.getZ() * 32) * 128);
        }

        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packetMoveLook = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(getServerPlayer().getId(), x, y, z, (byte) yaw,  (byte) pitch, false);
        PacketPlayOutEntityHeadRotation packetRotation = new PacketPlayOutEntityHeadRotation(getServerPlayer(), (byte) yaw);

        Arrays.stream(players).forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            ServerPlayer sp = craftPlayer.getHandle();
            ServerGamePacketListenerImpl ps = sp.connection;
            ps.send(packetMoveLook);
            ps.send(packetRotation);
        });
    }

    public static float[] getRotations(Location one, Location two) {
        double diffX = two.getX() - one.getX();
        double diffZ = two.getZ() - one.getZ();
        double diffY = two.getY() + 2.0 - 0.4 - (one.getY() + 2.0);
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isWalking() {
        return walking;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Entity getBukkitEntity(){
        return serverPlayer.getBukkitEntity();
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    public void setLookAtMe(boolean lookAtMe) {
        this.lookAtMe = lookAtMe;
    }

    public boolean hasLookAtMe() {
        return lookAtMe;
    }
}
