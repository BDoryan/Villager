package doryanbessiere.villager.net.commons.chat.text;

import doryanbessiere.villager.net.commons.chat.ChatMessageBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatMessageTextBuilder {

    private TextComponent textComponent;

    public ChatMessageTextBuilder(String text) {
        this.textComponent = new TextComponent(text);
    }

    public ChatMessageTextBuilder(String text, ClickEvent clickEvent) {
        this.textComponent = new TextComponent(text);
        click(clickEvent);
    }

    public ChatMessageTextBuilder(String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
        this.textComponent = new TextComponent(text);
        click(clickEvent);
        hover(hoverEvent);
    }

    public ChatMessageTextBuilder hover(HoverEvent hoverEvent){
        this.textComponent.setHoverEvent(hoverEvent);
        return this;
    }

    public ChatMessageTextBuilder click(ClickEvent clickEvent){
        this.textComponent.setClickEvent(clickEvent);
        return this;
    }

    public String getText() {
        return textComponent.getText();
    }

    public TextComponent getTextComponent() {
        return textComponent;
    }
}
