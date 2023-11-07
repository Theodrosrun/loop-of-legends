package ch.heigvd;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerWorker implements Runnable {
    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
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

    Message getMessage(String s) {
        switch(s.toUpperCase()) {
            case :
                return Operation.ADD;
            case "MULTIPLY":
                return Operation.MULTIPLY;
            default:
                return Operation.UNKNOWN;
        }
    }

    boolean isMessageValid(String s) {
        return getMessage(s) != Message.UNKN;
    }

    int handle(Message message, String value) {
        switch (message) {
            default:
                return 0;
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = clientInput.readLine()) != null) {
                String[] command = line.split(" ");

                serverOutput.write("Hello client!" + "\n");
                serverOutput.flush();
            }

             clientInput.close();
             serverOutput.close();
        } catch (IOException ex) {

        }
    }
}
