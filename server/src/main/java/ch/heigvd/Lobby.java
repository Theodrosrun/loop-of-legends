package ch.heigvd;

import static ch.heigvd.DIRECTION.UP;

public class Lobby {
    private final Player [] players;
    private final int LOBBY_WIDTH = 20;
    private final int LOBBY_HEIGHT = 20;
    private int nbPlayers = 0;
    public Lobby(int maxPlayers) {
        players = new Player[maxPlayers];
    }
    public boolean join(Player player) {
        for (Player p : players) {
            if (p.getName().equals(player.getName())) {
                return false;
            }
        }
        if (nbPlayers < players.length) {
            players[nbPlayers] = player;
            nbPlayers++;
            return true;
        }
        return false;
    }
    private void setReady(Player player) {
        player.setReady();
    }
    public Player[] getReadyPlayers() {
        int nbReady = countReady();
        Player[] readyPlayers = new Player[nbReady];
        int j = 0;
        for (Player player : players){
            if (player.isReady()) {
                readyPlayers[j] = player;
                j++;
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
        return nbPlayers < players.length;
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


    public Player[] getPlayers() {
        return players;
    }

    public int getNbPlayer() {
        return nbPlayers;
    }
}
