package doryanbessiere.villager.net.commons.item;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public class BookItemBuilder {

    private ItemStack itemStack;

    private String title;
    private String author;

    public BookItemBuilder create(){
        itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();

        BaseComponent[] page = new ComponentBuilder("[Click here to restore dropped items]")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/restoredrops")).create();

        bookMeta.spigot().addPage(page);

        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        itemStack.setItemMeta(bookMeta);

        return this;
    }
}
