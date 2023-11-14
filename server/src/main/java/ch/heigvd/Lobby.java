package ch.heigvd;

import java.util.ArrayList;
import java.util.List;

import static ch.heigvd.DIRECTION.UP;

public class Lobby {
    private ArrayList<Player> players;

    private final int MAX_PLAYERS;
    private final int LOBBY_WIDTH = 20;
    private final int LOBBY_HEIGHT = 20;

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
        for (Player player : players){
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
        return players.size() < MAX_PLAYERS;
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

    private void ComputeLobby(){
        StringBuilder gui = new StringBuilder();

    }


    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNbPlayer() {
        return players.size();
    }

    public int getNbReadyPlayers(){
        return getReadyPlayers().size();
    }

    public boolean everyPlayerReady() {
        for(Player player : players){
            if(!player.isReady()){
                return false;
            }
        }
        return !getReadyPlayers().isEmpty();
    }
}
