package doryanbessiere.villager.net.commons.dialog.sentence;

public class Sentence {

    private String sentence;
    private long duration;

    public Sentence(String sentence) {
        this.sentence = sentence;
        this.duration = (long) 5 * sentence.split(" ").length;
    }

    public Sentence(String sentence, long duration) {
        this.sentence = sentence;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getSentence() {
        return sentence;
    }
}
