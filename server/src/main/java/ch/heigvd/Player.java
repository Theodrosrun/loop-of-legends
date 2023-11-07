package ch.heigvd;

public class Player {

    private String name;

    private boolean ready = false;

    public Player(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setReady() {
        this.ready = !this.ready;
    }

    public boolean isReady() {
        return ready;
    }
}
