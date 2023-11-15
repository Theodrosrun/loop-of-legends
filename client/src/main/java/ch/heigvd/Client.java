package ch.heigvd;

import com.googlecode.lanterna.input.KeyStroke;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.console;
import static java.lang.System.exit;

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    private String command = "", response = "", message = "", data = "";

    private Terminal terminal = new Terminal();
    private InputHandler inputHandler = new InputHandler(terminal, 50);

    private BufferedWriter serverOutput;
    private BufferedReader serverInput;
    private Socket socket;

    private MessageHandler messageHandler;

    private void initConnection(InetAddress address, int port) {
        try {
            socket = new Socket(address, port);
            serverOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

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
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            closeServer();
            exit(1);
        }
    }

    private void closeServer() {
        try {
            serverOutput.close();
            serverInput.close();
            socket.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            exit(1);
        }
    }

    private void tryLobby() {

        messageHandler.send(Message.setCommand(Message.LOBB));

        try {
            response = Message.getResponse(serverInput);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }

        message = Message.getMessage(response);
        data = Message.getData(response);
        if (message.equals("EROR")) {
            terminal.print("Error :" + data);
            exit(1);
        }
    }

    public Client(InetAddress address, int port) {

        initConnection(address, port);
        tryLobby();
        join();
        waitReady();
        controlSnake();
    }

    private void join() {
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
        try {
            serverOutput.write(command);
            serverOutput.flush();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void waitReady() {
        inputHandler.resetKey();
        boolean isReady = false;
        try {
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

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            closeServer();
            exit(1);
        }
    }

    private void controlSnake() {
        try {
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
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {


        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        int port;
        InetAddress address = null;

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

        Client client = new Client(address, port);
    }
}