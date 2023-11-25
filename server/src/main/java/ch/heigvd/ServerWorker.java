package ch.heigvd;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerWorker implements Runnable {
    private final int UPDATE_FREQUENCY = 100; // millisecondes
    private Player player;
    private Server server;
    private Socket clientSocket;
    private BufferedReader clientInput = null;
    private BufferedWriter serverOutput = null;
    private Thread thGuiUpdate = new Thread(this::guiUpdate);

    public ServerWorker(Socket clientSocket, Server server) {
        this.server = server;
        this.clientSocket = clientSocket;

        try {
            clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            serverOutput = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException ex) {
        }

        Thread exitTh = new Thread(new Exit(clientSocket, serverOutput, clientInput));
        Runtime.getRuntime().addShutdownHook(exitTh);
    }

    @Override
    public void run() {
        try {
            String command, response, message, data;
            boolean finished = false;

            while (!finished) {
                response = Message.getResponse(clientInput);
                if (response == null) break;
                message = Message.getMessage(response);
                data = Message.getData(response);

                // Message unknown
                if (Message.fromString(message) == Message.UNKN) {
                }

                // Message handling
                switch (Message.fromString(message)) {
                    case INIT:
                        command = Message.setCommand(Message.DONE);
                        serverOutput.write(command);
                        serverOutput.flush();
                        break;

                    case LOBB:
                        command = server.isFull() ?
                                Message.setCommand(Message.EROR, "The lobby is full") :
                                Message.setCommand(Message.DONE);
                        serverOutput.write(command);
                        serverOutput.flush();
                        break;

                    case JOIN:
                        if (server.isFull()) {
                            serverOutput.write(Message.setCommand(Message.EROR, "The lobby is full"));
                            serverOutput.flush();
                            break;
                        } else if (server.playerExists(data)) {
                            serverOutput.write(Message.setCommand(Message.REPT, "Username already used"));
                            serverOutput.flush();
                            break;
                        } else if (data.isEmpty()) {
                            serverOutput.write(Message.setCommand(Message.REPT, "Username must have minimum 1 character"));
                            serverOutput.flush();
                            break;
                        } else {
                            serverOutput.write(Message.setCommand(Message.DONE));
                            serverOutput.flush();
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
                        serverOutput.write(Message.setCommand(Message.QUIT, "You left the game"));
                        serverOutput.flush();
                        if (player != null) server.removePlayer(player);
                        finished = true;
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
                }
            }

            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException ex1) {
                }
            }
        }
    }

    public void guiUpdate() {
        while (true) {
            try {
                Thread.sleep(UPDATE_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String command = Message.setCommand(Message.UPTE, server.getBoard().toString());
            if (!clientSocket.isClosed()) {
                try {
                    serverOutput.write(command);
                    serverOutput.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }
}