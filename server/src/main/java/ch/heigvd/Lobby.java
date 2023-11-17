package ch.heigvd;

import java.util.ArrayList;
import java.util.List;

import static ch.heigvd.DIRECTION.UP;

public class Lobby {
    private ArrayList<Player> players;

    private boolean isOpen = true;

    private final int MAX_PLAYERS;

    public Lobby(int maxPlayers) {
        MAX_PLAYERS = maxPlayers;
        players = new ArrayList<>();
    }

    public boolean join(Player player) {
        if (players.size() >= MAX_PLAYERS) {
            return false;
        }
        if (players.isEmpty()) {
            player.setMaster();
        } else {
            for (Player p : players) {
                if (p.getName().equals(player.getName())) {
                    return false;
                }
            }
            player.removeMaster();
        }
        players.add(player);
        return true;
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
    private void ComputeLobby() {
        StringBuilder gui = new StringBuilder();

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
        if (player.isMaster() && !players.isEmpty()){
            getPolePlayers().setMaster();
        }
    }

    private Player getPolePlayers() {
        if (players.isEmpty()) return null;
        return players.get(0);
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
}
