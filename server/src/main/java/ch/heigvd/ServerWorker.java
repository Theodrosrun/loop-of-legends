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

    @Override
    public void run() {
        try {
            while ((clientInput.readLine()) != null) {
                serverOutput.println("Hello client!");
                serverOutput.flush();
            }

             clientInput.close();
             serverOutput.close();
        } catch (IOException ex) {

        }
    }
}
