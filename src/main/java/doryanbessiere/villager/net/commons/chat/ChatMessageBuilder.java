package doryanbessiere.villager.net.commons.chat;

import doryanbessiere.villager.net.commons.chat.text.ChatMessageTextBuilder;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ChatMessageBuilder {

    private String text;

    public ChatMessageBuilder(String text) {
        this.text = text;
        baseComponents.add(new TextComponent(text));
    }

    private BaseComponent[] components;
    private ArrayList<BaseComponent> baseComponents = new ArrayList<>();

    public ChatMessageBuilder addText(ChatMessageTextBuilder text){
        baseComponents.add(text.getTextComponent());
        return this;
    }

    public ChatMessageBuilder build(){
        components = new BaseComponent[baseComponents.size()];
        for(int i = 0; i < baseComponents.size(); i++){
            components[i] = baseComponents.get(i);
        }
        return this;
    }

    public void sendMessage(Player player){
        if(components == null)build();
        player.spigot().sendMessage(components);
    }
}
