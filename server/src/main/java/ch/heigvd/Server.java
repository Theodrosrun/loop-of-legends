package ch.heigvd;

import ch.heigvd.snake.Snake;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final int NB_PLAYER = 4 ;

    private final int LOBBY_FREQUENCY = 100; // millisecondes
    private final int GAME_FREQUENCY = 300; // millisecondes
    private Lobby lobby = new Lobby(NB_PLAYER);
    private Board board;
    private Snake snakes [] = new Snake[NB_PLAYER];

    private DIRECTION[] directions = {DIRECTION.UP, DIRECTION.RIGHT, DIRECTION.DOWN, DIRECTION.LEFT};
    private final static Logger LOG = Logger.getLogger(Server.class.getName());
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        (new Server()).start();
    }
    private void start() {
        board = new Board(25, 25, (short)NB_PLAYER, (short)20, (short)200);



        //loop for lobby
        while (lobby.getReadyPlayers().length != lobby.getNbPlayer()){
            board.deployLobby(lobby);
            try {
                Thread.sleep(LOBBY_FREQUENCY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        initSnakes();
        //loop for game
        while (true) {
            for (Snake snake : snakes ) {
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

//        int port = 20000;
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            while (true) {
//                LOG.log(Level.INFO, "Waiting for a new client on port {0}", port);
//                Socket clientSocket = serverSocket.accept();
//                LOG.info("A new client has arrived. Starting a new thread and delegating work to a new servant...");
//                new Thread(new ServerWorker(clientSocket)).start();
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void initSnakes() {
        int initLenght = 3;
        Position initPosition;
        int bw = board.getWidth();
        int bh = board.getHeigth();
        for (int i = 0; i < lobby.getReadyPlayers().length; i++) {
            switch (i) {
                case 0: {
                    initPosition = new Position(bw/2, bh, directions[i],' ' );
                    snakes[i] = new Snake(initPosition, (short)initLenght);
                }
                case 1: {
                    initPosition = new Position(0, bh/2, directions[i],' ' );
                    snakes[i] = new Snake(initPosition, (short)initLenght);
                }
                case 2: {
                    initPosition = new Position(bw/2, 0, directions[i],' ' );
                    snakes[i] = new Snake(initPosition, (short)initLenght);
                }
                case 3: {
                    initPosition = new Position(bw, bh/2, directions[i],' ' );
                    snakes[i] = new Snake(initPosition, (short)initLenght);
                }
            }
        }
    }

    public void setKey(KEY key, Player player) {
        if (key.ordinal() < 4) {
            setDirection(key, player);
        }
        else {
            //do somthing
        }
    }

    private void setDirection(KEY key, Player player) {
        DIRECTION direction = DIRECTION.parseKey(key);
        if (direction != null) {
            directions[player.getId()] = direction;
        }
    }

    public Player getPlayer(int id) {
        return lobby.getPlayer(id);
    }
    public void joinLobby(Player player) {
        lobby.join(player);
    }
    public boolean isFull(){
        return lobby.lobbyIsFull();
    }

}