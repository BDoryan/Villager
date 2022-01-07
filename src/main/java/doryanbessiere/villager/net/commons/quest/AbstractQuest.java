package doryanbessiere.villager.net.commons.quest;

import doryanbessiere.villager.net.player.VillagerPlayer;
import doryanbessiere.villager.net.quests.WelcomeQuest;
import org.bukkit.event.Listener;

public abstract class AbstractQuest extends QuestData implements Listener {

    protected boolean play = false;

    public boolean isPlayed() {
        return play;
    }

    public static final Class<?>[] QUESTS = new Class<?>[]{
            WelcomeQuest.class
    };

    public AbstractQuest(int id, String name, int step) {
        super(id, name, step);
    }

    public AbstractQuest(int id, String name, int step, int stepIndex) {
        super(id, name, step);

        this.stepIndex = stepIndex;
    }

    public AbstractQuest(QuestData data) {
        super(data.getId(), data.getName(), data.getStep(), data.getStepIndex());
    }

    public abstract void play(VillagerPlayer villagerPlayer);
    public abstract void next(VillagerPlayer villagerPlayer);
    public abstract void stop(VillagerPlayer villagerPlayer);
    public abstract void resume(VillagerPlayer villagerPlayer);

}
