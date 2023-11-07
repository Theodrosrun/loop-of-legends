package ch.heigvd;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerWorker implements Runnable {
    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
    private Player player;
    Socket clientSocket;
    BufferedReader clientInput = null;
    PrintWriter serverOutput = null;

    public ServerWorker(Socket clientSocket) {
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
            String line;
            while ((line = clientInput.readLine()) != null) {

                // Message parsing
                String[] command = line.split(" ");
                Message message = Message.fromString(command[0]);
                String data = command[1];

                // Message unknown
                if(message == Message.UNKN){
                    // return;
                }

                commandHandler(message, data);

                serverOutput.write("Hello client!" + "\n");
                serverOutput.flush();
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
                // Handle INIT message
                return 1;
            case DONE:
                // Handle DONE message
                return 1;
            case LOBB:
                // Handle LOBB message
                return 1;
            case JOIN:
                // Handle JOIN message
                return 1;
            case RADY:
                // Handle RADY message
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
}
