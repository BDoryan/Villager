package doryanbessiere.villager.net.npc;

import doryanbessiere.villager.net.VillagerPlugin;
import doryanbessiere.villager.net.commands.VillageCommand;
import doryanbessiere.villager.net.commons.Lang;
import doryanbessiere.villager.net.commons.chat.ChatMessageBuilder;
import doryanbessiere.villager.net.commons.chat.text.ChatMessageTextBuilder;
import doryanbessiere.villager.net.commons.config.ConfigurationUtils;
import doryanbessiere.villager.net.commons.dialog.Dialog;
import doryanbessiere.villager.net.commons.npc.NPC;
import doryanbessiere.villager.net.player.VillagerPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TownManagerNPC extends NPC {

    public static TownManagerNPC NPC = TownManagerNPC.create();

    public TownManagerNPC(String name, String textures, String signature, boolean lookAtMe) {
        super(name, textures, signature, lookAtMe);
    }

    public void spawn(VillagerPlayer player){
        spawn(ConfigurationUtils.getLocation(VillagerPlugin.getInstance().getConfig(), "npc.town-manager.spawn"), player.getPlayer());
    }

    private boolean talking = false;

    @Override
    public void click(Player player) {
        super.click(player);

        VillagerPlayer villagerPlayer = VillagerPlugin.getVillager(player);
        Lang lang = villagerPlayer.getLang();

        if(talking)return;

        talking = true;

        TextComponent[] interactions = new TextComponent[]{
                new ChatMessageTextBuilder(lang.getMessage("dialog_townmanager_create_village"))
                        .click(new ClickEvent(ClickEvent.Action.RUN_COMMAND, VillageCommand.CREATE_VILLAGE_COMMAND))
                        .hover(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(lang.getMessage("dialog_townmanager_create_hover_village")).create())).getTextComponent(),
                new ChatMessageTextBuilder(lang.getMessage("dialog_townmanager_resign_village"))
                        .click(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""))
                        .hover(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(lang.getMessage("dialog_townmanager_resign_hover_village")).create())).getTextComponent()
        };

        Dialog dialog = new Dialog(player, getLocation()) {
            @Override
            public void finish() {
                talking = false;
            }

            @Override
            public void end() {

            }
        };
        dialog.load("dialog_townmanager_sentence_1", "dialog_townmanager_sentence_2", "dialog_townmanager_sentence_3");
        dialog.loadInteractions(interactions);
        dialog.play();
    }

    public static TownManagerNPC create()
    {
        FileConfiguration config = VillagerPlugin.getInstance().getConfig();
        return new TownManagerNPC("§7Gérant", config.getString("npc.town-manager.textures"), config.getString("npc.town-manager.signature"), true);
    }
}
