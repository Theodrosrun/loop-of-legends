package ch.heigvd;

public class Lobby {
    private final Player [] players;

    private final int LOBBY_WIDTH = 20;
    private final int LOBBY_HEIGHT = 20;

    private int nbPlayers = 0;


    private Lobby(int maxPlayers) {
        players = new Player[maxPlayers];
    }
    private boolean Join(Player player) {
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

    private int countReady() {
        int nbReady = 0;
        for (Player player : players) {
            if (player.isReady()) {
                nbReady++;
            }
        }
        return nbReady;
    }


}
