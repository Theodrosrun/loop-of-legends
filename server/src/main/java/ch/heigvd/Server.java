package ch.heigvd;

import ch.heigvd.snake.Snake;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private final int PORT = 20000;
    private static final int NB_PLAYER = 4;
    private final int LOBBY_FREQUENCY = 100; // millisecondes
    private final int GAME_FREQUENCY = 300; // millisecondes
    private Lobby lobby = new Lobby(NB_PLAYER);

    private boolean listenNewClient = true;
    private Board board;
    private DIRECTION[] directions = {DIRECTION.UP, DIRECTION.RIGHT, DIRECTION.DOWN, DIRECTION.LEFT};
    private final static Logger LOG = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        (new Server()).start();
    }

    private void start() {

        Thread thListener = new Thread(this::listenNewClient);
        thListener.start();

        board = new Board(30, 15, (short) NB_PLAYER, (short) 20, (short) 200);

        //loop for lobby
        lobby.open();
        while (!lobby.everyPlayerReady()) {
            board.deployLobby(lobby);
            try {
                Thread.sleep(LOBBY_FREQUENCY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        lobby.initSnakes(board);
        lobby.close();
        listenNewClient = false;

        //loop for game
        while (true) {
            lobby.snakeStep();
            board.deploySnakes(lobby.getSnakes());
            board.deployFood();
            try {
                Thread.sleep(GAME_FREQUENCY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setDirection(KEY key, Player player) {
        if (!lobby.everyPlayerReady()) return;
        DIRECTION direction = DIRECTION.parseKey(key);
        if (direction != null) {
            lobby.setDirection(player, direction);
        }
    }

    public void joinLobby(Player player) {
        lobby.join(player);
        board.deployLobby(lobby);
    }

    private void listenNewClient() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                LOG.log(Level.INFO, "Waiting for a new client on port {0}", PORT);
                Socket clientSocket = serverSocket.accept();

                if (lobby.isOpen() && !lobby.lobbyIsFull()) {
                    LOG.info("A new client has arrived. Starting a new thread and delegating work to a new servant...");
                    new Thread(new ServerWorker(clientSocket, this)).start();
                    continue;
                }

                BufferedWriter serverOutput = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));

                if (lobby.lobbyIsFull()) {
                    LOG.log(Level.INFO, "The lobby is full. Rejecting the new client...");
                    serverOutput.write(Message.setCommand(Message.EROR, "The lobby is full"));
                    serverOutput.flush();
                    clientSocket.close();
                    continue;
                } else {
                    LOG.log(Level.INFO, "The lobby is closed. Rejecting the new client...");
                    serverOutput.write(Message.setCommand(Message.EROR, "The lobby is closed"));
                    serverOutput.flush();
                    clientSocket.close();
                    continue;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Board getBoard() {
        return board;
    }

    public boolean isFull() {
        return lobby.lobbyIsFull();
    }

    public void removePlayer(Player player) {
        lobby.removePlayer(player);
    }

    public void setReady(Player player) {
        lobby.setReady(player);
        board.deployLobby(lobby);
    }

    public boolean playerExists(String userName) {
        return lobby.playerExists(userName);
    }
}