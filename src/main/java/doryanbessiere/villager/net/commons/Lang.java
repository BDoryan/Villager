package doryanbessiere.villager.net.commons;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

public class Lang {

    private InputStream inputStream;

    public Lang(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private HashMap<String, String> messages = new HashMap<>();

    public Lang(String name){
        try {
            this.inputStream = getClass().getResourceAsStream("/fr_fr.lang");
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() throws IOException {
        messages.clear();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

        String line = null;
        while((line = reader.readLine()) != null){
            if(line.contains("=")){
                String[] arguments = line.split("=");
                String path = arguments[0];
                String value = arguments[1];

                messages.put(path, value);
            }
        }
    }

    public String getMessage(String path, String... replaces){
        String message = messages.get(path);
        if(replaces.length % 2 == 0){
            for(int i = 0;i < replaces.length; i+=2){
                message = message.replace(replaces[i], replaces[i + 1]);
            }
        }
        return message;
    }

    public HashMap<String, String> getMessages() {
        return messages;
    }
}
