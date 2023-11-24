package ch.heigvd;

import ch.heigvd.snake.Snake;

import java.util.ArrayList;
import java.util.List;

import static ch.heigvd.DIRECTION.*;

public class Lobby {
    private ArrayList<Player> players;

    private boolean isOpen = true;

    private final int MAX_PLAYERS;

    public Lobby(int maxPlayers) {
        MAX_PLAYERS = maxPlayers;
        players = new ArrayList<>();
    }

    public boolean join(Player player) {
        for (Player p : players) {
            if (p.getName().equals(player.getName())) {
                return false;
            }
        }
        if (players.size() < MAX_PLAYERS) {
            players.add(player);
            return true;
        }
        return false;
    }
    public void setReady(Player player) {
        player.setReady();
    }
    public ArrayList<Player> getReadyPlayers() {
        int nbReady = countReady();
        ArrayList<Player> readyPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isReady()) {
                readyPlayers.add(player);
            }
        }
        return readyPlayers;
    }
    public Player getPlayer(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    public boolean lobbyIsFull() {
        return players.size() >= MAX_PLAYERS;
    }
    private int countReady() {
        int nbReady = 0;
        for (Player player : players) {
            if (player.isReady()) {
                nbReady++;
            }
        }
        return nbReady;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void close() {
        isOpen = false;
    }

    public int getNbPlayer() {
        return players.size();
    }

    public int getNbReadyPlayers() {
        return getReadyPlayers().size();
    }

    public boolean everyPlayerReady() {
        for (Player player : players) {
            if (!player.isReady()) {
                return false;
            }
        }
        return !getReadyPlayers().isEmpty();
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void open() {
        isOpen = true;
    }

    public boolean playerExists(String userName) {
        for (Player player : players) {
            if (player.getName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void initSnakes(Board board) {
        int initLenght = 3;
        Position initPosition;
        int bw = board.getWidth();
        int bh = board.getHeigth();

        int i = 0;
        for (Player player : players) {
            switch (i) {
                case 0: {
                    initPosition = new Position(bw / 2, bh, UP, ' ');
                    player.setSnake(new Snake(initPosition, (short) initLenght));
                    break;
                }
                case 1: {
                    initPosition = new Position(0, bh / 2, LEFT, ' ');
                    player.setSnake(new Snake(initPosition, (short) initLenght));
                    break;
                }
                case 2: {
                    initPosition = new Position(bw / 2, 0, DOWN, ' ');
                    player.setSnake(new Snake(initPosition, (short) initLenght));
                    break;
                }
                case 3: {
                    initPosition = new Position(bw, bh / 2, RIGHT, ' ');
                    player.setSnake(new Snake(initPosition, (short) initLenght));
                    break;
                }
            }
            i++;
        }
    }

    public void setDirection(Player player, DIRECTION direction) {
        player.getSnake().setNextDirection(direction);
    }

    public void snakeStep() {
        for (Player player : players) {
            player.getSnake().step();
        }
    }

    public ArrayList<Snake> getSnakes() {
        ArrayList<Snake> snakes = new ArrayList<>();
        for (Player player : players) {
            snakes.add(player.getSnake());
        }
        return snakes;
    }
}
