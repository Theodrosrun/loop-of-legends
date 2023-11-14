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
                BufferedWriter serverOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        ) {

            Terminal terminal = new Terminal();
              InputHandler inputHandler = new InputHandler(terminal, 50);




            //INIT
            serverOutput.write(Message.INIT.toString() + "\n");
            serverOutput.flush();
            String message = "";
            while (!message.equals("DONE")) {
                message = Message.getMessage(serverInput);
                terminal.print(message);
            }


            terminal.print("Press enter to join");

            while (inputHandler.getKey() != KEY.ENTER) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            serverOutput.write(Message.JOIN.toString() + " " + "Premier" + "\n");
            serverOutput.flush();


            while (!message.equals("STRT")) {
                if (inputHandler.getKey() == KEY.READY) {
                    serverOutput.write(Message.RADY.toString() + "\n");
                    serverOutput.flush();
                }
                message = Message.getMessage(serverInput);
                terminal.print(message.substring(5, message.length() - 1));
            }


            KEY lastKey = null;

            while (true) {

                KEY key = KEY.parseKeyStroke(inputHandler.getKeyStroke());
                if (key != lastKey) {
                    terminal.print("Key :" + key.toString() + " pressed");
                }


            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
