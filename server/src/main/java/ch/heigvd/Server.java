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
    private ArrayList<Snake> snakes = new ArrayList<>();
    private MessageHandler messageHandler;

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
        lobby.close();
        listenNewClient = false;


        initSnakes();
        //loop for game
        while (true) {
            for (Snake snake : snakes) {
                snake.setNextDirection(directions[snake.getId()]);
                snake.step();
            }
            board.deploySnakes(snakes);
            board.deployFood();
            try {
                Thread.sleep(GAME_FREQUENCY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initSnakes() {
        int initLenght = 3;
        Position initPosition;
        int bw = board.getWidth();
        int bh = board.getHeigth();

        for (int i = 0; i < lobby.getNbReadyPlayers(); i++) {
            switch (i) {
                case 0: {
                    initPosition = new Position(bw / 2, bh, directions[i], ' ');
                    snakes.add(new Snake(initPosition, (short) initLenght));
                    break;
                }
                case 1: {
                    initPosition = new Position(0, bh / 2, directions[i], ' ');
                    snakes.add(new Snake(initPosition, (short) initLenght));
                    break;
                }
                case 2: {
                    initPosition = new Position(bw / 2, 0, directions[i], ' ');
                    snakes.add(new Snake(initPosition, (short) initLenght));
                    break;
                }
                case 3: {
                    initPosition = new Position(bw, bh / 2, directions[i], ' ');
                    snakes.add(new Snake(initPosition, (short) initLenght));
                    break;
                }
            }
        }
    }

    public void setDirection(KEY key, Player player) {
        if (!lobby.everyPlayerReady()) return;
        DIRECTION direction = DIRECTION.parseKey(key);
        if (direction != null) {
            directions[player.getId()-1] = direction;
        }
    }

    public Player getPlayer(int id) {
        return lobby.getPlayer(id);
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
                MessageHandler messageHandler = new MessageHandler(serverOutput);

                if (lobby.lobbyIsFull()) {
                    LOG.log(Level.INFO, "The lobby is full. Rejecting the new client...");
                    messageHandler.send(Message.setCommand(Message.EROR, "The lobby is full"));
                    clientSocket.close();
                    continue;
                } else {
                    LOG.log(Level.INFO, "The lobby is closed. Rejecting the new client...");
                    messageHandler.send(Message.setCommand(Message.EROR, "The lobby is closed"));
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