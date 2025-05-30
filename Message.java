package model;

public class Message {
    private String text;
    private boolean isFake;

    public Message(String text, boolean isFake) {
        this.text = text;
        this.isFake = isFake;
    }

    public String getText() {
        return text;
    }

    public boolean isFake() {
        return isFake;
    }
}
