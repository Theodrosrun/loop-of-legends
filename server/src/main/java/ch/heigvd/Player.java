package ch.heigvd;

import ch.heigvd.snake.Snake;

public class Player {

    private static int idCnt = 0;
    private int id;

    private Snake snake;

    private String name;

    private boolean ready = false;

    public Player(String name) {
        this.id = ++idCnt;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Snake getSnake() {
        return snake;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player player){
            return this.id == player.id;
        }
        return false;
    }
}
