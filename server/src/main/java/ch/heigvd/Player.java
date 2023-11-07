package ch.heigvd;

public class Player {

    private static int idCnt = 0;
    private int id;

    private String name;

    private boolean ready = false;

    public Player(String name) {
        this.id = ++idCnt;
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

    public int getId() {
        return id;
    }
}
