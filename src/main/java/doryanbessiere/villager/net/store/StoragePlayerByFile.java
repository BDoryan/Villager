package doryanbessiere.villager.net.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import doryanbessiere.villager.net.player.VillagerPlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class StoragePlayerByFile extends StoragePlayerAbstract {

    private static final Gson GSON = new GsonBuilder().create();
    private static final File DIRECTORY = new File("plugins/Villager/villagers/");

    private VillagerPlayer createVillagerPlayerFile(Player player){
        VillagerPlayer villagerPlayer = new VillagerPlayer(player);

        if(!DIRECTORY.exists()){
            DIRECTORY.mkdirs();
        }

        File villagerFile = getVillagerPlayerFile(player.getUniqueId().toString());
        if(villagerFile.exists()){
            return getVillager(player.getUniqueId().toString());
        }
        try {
            villagerFile.createNewFile();
            writeVillagerPlayerToFile(villagerFile, villagerPlayer);
            return villagerPlayer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void writeVillagerPlayerToFile(File villagerFile, VillagerPlayer villagerPlayer){
        try {
            FileWriter writer = new FileWriter(villagerFile);
            writer.write(GSON.toJson(villagerPlayer));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getVillagerPlayerFile(String uuidString){
        File villagerFile = new File(DIRECTORY, uuidString+".json");
        return villagerFile;
    }

    @Override
    public VillagerPlayer getVillager(String uuidString) {
        File villagerFile = getVillagerPlayerFile(uuidString);
        if(villagerFile.exists()){
            try {
                FileReader reader = new FileReader(villagerFile);
                VillagerPlayer villagerPlayer = GSON.fromJson(reader, VillagerPlayer.class);
                reader.close();
                return villagerPlayer;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }   
        return null;
    }

    @Override
    public VillagerPlayer addVillager(Player player) {
        String uuid = player.getUniqueId().toString();
        if(existsVillager(uuid)){
            return getVillager(uuid);
        }
        return createVillagerPlayerFile(player);
    }

    @Override
    public boolean updateVillager(VillagerPlayer player) {
        String uuid = player.getUUID();
        if(existsVillager(uuid)){
            try {
                File villagerFile = getVillagerPlayerFile(uuid);
                FileChannel.open(villagerFile.toPath(), StandardOpenOption.WRITE).truncate(0).close();
                writeVillagerPlayerToFile(villagerFile, player);
                return true;
            } catch (IOException exception){
                exception.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public boolean removeVillager(String uuidString) {
        if(existsVillager(uuidString)){
            return getVillagerPlayerFile(uuidString).delete();
        }
        return false;
    }
}
