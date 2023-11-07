package ch.heigvd;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        InetAddress address;
        int port;

        if (args.length != 2) {
            System.err.println("usage: client <address> <port>");
            return;
        }

        try {
            address = InetAddress.getByName((args[0]));
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return;
        }

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return;
        }

        try (
                Socket socket = new Socket(address, port);
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter serverOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        ) {
            while (true) {
                serverOutput.write(clientInput.readLine() + "\n");
                serverOutput.flush();
                System.out.println(serverInput.readLine());
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
