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
        this.server = server;
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
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
            String command = "", response = "", message = "", data = "";

            while (!(response = Message.getResponse(clientInput)).equals(null)) {
                message = Message.getMessage(response);
                data = Message.getData(response);

                // Message unknown
                if(Message.fromString(message) == Message.UNKN){
                    // return;
                }

                commandHandler(Message.fromString(message), data);
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

    private void commandHandler(Message message, String data) {
        switch (message) {
            case INIT:
                String command = Message.setCommand(Message.DONE);
                messageHandler.send(command);
                break;
            case DONE:
                // Handle DONE message
                break;
            case LOBB:
                if (server.isFull()) {
                    messageHandler.send(Message.setCommand(Message.EROR, "The lobby is full"));
                } else {
                    messageHandler.send(Message.setCommand(Message.DONE));
                }
                break;
            case JOIN:
                player = new Player(data);
                if (server.isFull()) {
                    messageHandler.send(Message.setCommand(Message.EROR, "The lobby is full"));
                    break;
                }
                server.joinLobby(player);
                thGuiUpdate.start();
                break;
            case RADY:
                server.setReady(player);
                break;
            case STRT:
                // Handle STRT message
                break;
            case DIRE:
                KEY key = KEY.valueOf(data);
                server.setDirection(key, player);
                // Handle DIRE message
                break;
            case UPTE:
                // Handle UPTE message
                break;
            case ENDD:
                // Handle ENDD message
                break;
            default:
                // Handle unexpected message
                break;
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