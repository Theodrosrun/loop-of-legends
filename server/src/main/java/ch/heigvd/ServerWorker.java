package ch.heigvd;

import java.io.*;
import java.net.Socket;
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
    private PrintWriter serverOutput = null;
    private Thread thGuiUpdate = new Thread(this::guiUpdate);

    // private final ReentrantLock mutex = new ReentrantLock();

    public ServerWorker(Socket clientSocket, Server server) {
        this.server = server;
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        this.clientSocket = clientSocket;
        try {
            clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            serverOutput = new PrintWriter(clientSocket.getOutputStream());
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

            if (serverOutput != null) {
                serverOutput.close();
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

    private int commandHandler(Message message, String data) {
        switch (message) {
            case INIT:
                String command = Message.setCommand(Message.DONE);
                send(command);
                return 1;
            case DONE:
                // Handle DONE message
                return 1;
            case LOBB:
                // Handle LOBB message
                return 1;
            case JOIN:
                player = new Player(data);
                server.joinLobby(player);
                thGuiUpdate.start();
                return 1;
            case RADY:
                server.setReady(player);
                return 1;
            case STRT:
                // Handle STRT message
                return 1;
            case DIRE:
                // Server.SetKey(Key, player)
                // Handle DIRE message
                return 1;
            case UPTE:
                // Handle UPTE message
                return 1;
            case ENDD:
                // Handle ENDD message
                return 1;
            default:
                // Handle unexpected message
                return 0;
        }
    }

    private void send(String command){
        serverOutput.write(command);
        serverOutput.flush();
    }

    public void guiUpdate(){
        while (true){
            try {
                Thread.sleep(UPDATE_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String command = Message.setCommand(Message.UPTE, server.getBoard().toString());
            send(command);
        }
    }
}