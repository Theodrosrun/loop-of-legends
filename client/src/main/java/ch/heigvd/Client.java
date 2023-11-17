package ch.heigvd;

import com.googlecode.lanterna.input.KeyStroke;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.*;

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    private String command = "", response = "", message = "", data = "";

    private final Terminal terminal = new Terminal();
    private final InputHandler inputHandler = new InputHandler(terminal, 50);

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
        response = Message.getResponse(serverInput);
        message = Message.getMessage(response);
        data = Message.getData(response);
        if (message.equals("EROR")) {
            terminal.print("Error :" + data);
            exit(1);
        }
    }

    public Client(InetAddress address, int port) {

        initConnection(address, port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            onExit();
        }));
        tryLobby();
        join();
        waitReady();
        controlSnake();
    }

    private void join() {
        terminal.print(Intro.logo);
        while (inputHandler.getKey() != KEY.ENTER) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        inputHandler.pauseHandler();
        message = "";
        while (true) {
            if (message.equals("DONE")) {
                break;
            }
            String UserName = terminal.userInput();
            command = Message.setCommand(Message.JOIN, UserName);
            messageHandler.send(command);
            response = Message.getResponse(serverInput);
            message = Message.getMessage(response);
            data = Message.getData(response);
            if (UserName == null) {
                messageHandler.send(Message.setCommand(Message.QUIT));
                closeServer();
                exit(1);
            }

            if(message.equals("REPT")) {
                inputHandler.restoreHandler();
                inputHandler.resetKey();
                while (inputHandler.getKey() != KEY.ENTER) {
                    terminal.print(data + "\n" + "Press enter to continue\n");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                inputHandler.pauseHandler();
            }
        }

        inputHandler.restoreHandler();

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
                if (inputHandler.getKey() == KEY.MENU){
                    inputHandler.pauseHandler();
                    new Menu(terminal);
                    inputHandler.restoreHandler();
                    inputHandler.resetKey();
                }
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

    private void onExit() {
        messageHandler.send(Message.setCommand(Message.QUIT));
        closeServer();
        LOG.log(Level.INFO, "Client Exit");
    }
}