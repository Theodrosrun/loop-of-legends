package ch.heigvd;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.concurrent.locks.ReentrantLock;


public class ServerWorker implements Runnable {
    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
    private final int UPDATE_FREQUENCY = 100; // millisecondes
    private Player player;
    private Server server;
    private Socket clientSocket;
    private BufferedReader clientInput = null;
    private BufferedWriter serverOutput = null;
    private Thread thGuiUpdate = new Thread(this::guiUpdate);
    private MessageHandler messageHandler;
    // private final ReentrantLock mutex = new ReentrantLock();

    public ServerWorker(Socket clientSocket, Server server) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        this.server = server;
        this.clientSocket = clientSocket;
        try {
            clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            serverOutput = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(),  StandardCharsets.UTF_8));
            messageHandler = new MessageHandler(serverOutput);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void run() {
        try {
            String command, response, message, data;

            while ((response = Message.getResponse(clientInput)) != null) {
                message = Message.getMessage(response);
                data = Message.getData(response);

                // Message unknown
                if(Message.fromString(message) == Message.UNKN){
                    LOG.log(Level.SEVERE, "Message unknown");
                }

                // Message handling
                switch (Message.fromString(message)) {
                    case INIT:
                        command = Message.setCommand(Message.DONE);
                        messageHandler.send(command);
                        break;

                    case LOBB:
                        if (server.isFull()) {
                            messageHandler.send(Message.setCommand(Message.EROR, "The lobby is full"));
                        } else {
                            messageHandler.send(Message.setCommand(Message.DONE));
                        }
                        break;

                    case JOIN:
                        if (server.isFull()) {
                            messageHandler.send(Message.setCommand(Message.EROR, "The lobby is full"));
                            break;
                        }
                        else if (server.playerExists(data)) {
                            messageHandler.send(Message.setCommand(Message.REPT, "Username already used"));
                            break;
                        }
                        else if (data.isEmpty()) {
                            messageHandler.send(Message.setCommand(Message.REPT, "Username must have minimum 1 character"));
                            break;
                        }
                        else {
                            messageHandler.send(Message.setCommand(Message.DONE));
                            player = new Player(data);
                            server.joinLobby(player);
                            thGuiUpdate.start();
                        }
                        break;

                    case RADY:
                        server.setReady(player);
                        break;

                    case DIRE:
                        KEY key = KEY.valueOf(data);
                        server.setDirection(key, player);
                        break;

                    case QUIT:
                        messageHandler.send(Message.setCommand(Message.ENDD, "You left the game"));
                        if (player != null) server.removePlayer(player);
                        break;

                    default:
                        break;
                }
            }

            clientInput.close();
            serverOutput.close();

        } catch (IOException ex) {
            if (clientInput != null) {
                try {
                    clientInput.close();
                } catch (IOException ex1) {
                    LOG.log(Level.SEVERE, "In BufferedReader cannot be closed");
                }
            }

            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException ex1) {
                    LOG.log(Level.SEVERE, "ClientSocket cannot be closed");
                }
            }

            LOG.log(Level.SEVERE, "Global error: client made a hard disconnect");
        }
    }

    public void guiUpdate(){
        while (true){
            try {
                Thread.sleep(UPDATE_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String command = Message.setCommand(Message.UPTE, server.getBoard().toString());
            messageHandler.send(command);
        }
    }
}