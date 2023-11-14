package ch.heigvd;

import com.googlecode.lanterna.input.KeyStroke;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    private static MessageHandler messageHandler;

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
            String command = "", response = "", message = "", data = "";
            messageHandler = new MessageHandler(serverOutput);

            // INIT
            command = Message.setCommand(Message.INIT);
            messageHandler.send(command);

            while (!message.equals("DONE")) {
                response = Message.getResponse(serverInput);
                message = Message.getMessage(response);
                data = Message.getData(response);
                if (message.equals("EROR")) {
                    terminal.print("Error :" + data);
                    exit(1);
                }
            }

            // LOBB

                messageHandler.send(Message.setCommand(Message.LOBB));
                response = Message.getResponse(serverInput);
                message = Message.getMessage(response);
                data = Message.getData(response);
                if (message.equals("EROR")) {
                    terminal.print("Error :" + data);
                    exit(1);
                }



            // JOIN
            terminal.print("Press enter to join");

            while (inputHandler.getKey() != KEY.ENTER) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            inputHandler.pauseHandler();

            String UserName = terminal.userInput();
            inputHandler.restoreHandler();

            command = Message.setCommand(Message.JOIN, UserName);
            serverOutput.write(command);
            serverOutput.flush();

            boolean isReady = false;
            while (!isReady) {
                if (inputHandler.getKey() == KEY.READY) {
                    command = Message.setCommand(Message.RADY);
                    serverOutput.write(command);
                    serverOutput.flush();
                    inputHandler.resetKey();
                    isReady = true;
                }
                response = Message.getResponse(serverInput);
                message = Message.getMessage(response);
                data = Message.getData(response);
                terminal.print(data);
            }

            KEY lastKey = null;

            while (inputHandler.getKey() != KEY.QUIT) {
                KeyStroke key = inputHandler.getKeyStroke();
                if (InputHandler.isDirection(key)) {
                    command = Message.setCommand(Message.DIRE, KEY.parseKeyStroke(key).toString());
                    serverOutput.write(command);
                    serverOutput.flush();
                    inputHandler.resetKey();
                }
                response = Message.getResponse(serverInput);
                message = Message.getMessage(response);
                data = Message.getData(response);
                terminal.print(data);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}