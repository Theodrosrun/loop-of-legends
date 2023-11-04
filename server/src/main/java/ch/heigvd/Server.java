package ch.heigvd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private final static Logger LOG = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        (new Server()).start();
    }

    private void start() {
        int port = 20000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                LOG.log(Level.INFO, "Waiting for a new client on port {0}", port);
                Socket clientSocket = serverSocket.accept();
                LOG.info("A new client has arrived. Starting a new thread and delegating work to a new servant...");
                new Thread(new ServerWorker(clientSocket)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}