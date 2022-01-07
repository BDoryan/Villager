package doryanbessiere.villager.net.commons.quest;

public class QuestData {

    private int id;
    protected String name;
    protected int step = 0;
    protected int stepIndex = 0;

    public QuestData(int id, String name, int step, int stepIndex) {
        this.id = id;
        this.name = name;
        this.step = step;
        this.stepIndex = stepIndex;
    }

    public QuestData(int id, String name, int step) {
        this(id, name, step, 0);
    }

    public int getId() {
        return id;
    }

    public int getStep() {
        return step;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public String getName() {
        return name;
    }
}
