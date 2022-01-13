package doryanbessiere.villager.net.village.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.village.Village;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class StorageVillageByFile extends StorageVillageAbstract{

    private static final Gson GSON = new GsonBuilder().create();
    private static final File DIRECTORY = new File("plugins/Villager/villages/");

    @Override
    public Village addVillage(Village village) {
        String name = village.getName();
        if(existsVillage(name)){
            return getVillage(name);
        }
        return createVillage(village);
    }

    @Override
    public Village getVillage(String uuidString) {
        File villageFile = getVillageFile(uuidString);
        if(villageFile.exists()){
            try {
                FileReader reader = new FileReader(villageFile);
                Village village = GSON.fromJson(reader, Village.class);
                reader.close();
                return village;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean updateVillage(Village player) {
        String name = player.getName();
        if(existsVillage(name)){
            try {
                File villageFile = getVillageFile(name);
                FileChannel.open(villageFile.toPath(), StandardOpenOption.WRITE).truncate(0).close();
                writeVillageToFile(villageFile, player);
                return true;
            } catch (IOException exception){
                exception.printStackTrace();
            }

        }
        return false;
    }

    private Village createVillage(Village village){
        if(!DIRECTORY.exists()){
            DIRECTORY.mkdirs();
        }

        File villageFile = getVillageFile(village.getName());
        if(villageFile.exists()){
            return getVillage(village.getName());
        }
        try {
            villageFile.createNewFile();
            writeVillageToFile(villageFile, village);
            return village;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void writeVillageToFile(File villageFile, Village village){
        try {
            FileWriter writer = new FileWriter(villageFile);
            writer.write(GSON.toJson(village));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getVillageFile(String uuidString){
        File villageFile = new File(DIRECTORY, uuidString+".json");
        return villageFile;
    }
}
