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
    /**
     * The logger
     */
    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    /**
     * The command, response, message and data used to communicate with the server
     */
    private String command = "", response = "", message = "", data = "";

    /**
     * The terminal
     */
    private final Terminal terminal = new Terminal();

    /**
     * The input handler used to get the user inputs
     */
    private final InputHandler inputHandler = new InputHandler(terminal, 50);

    /**
     * The output streams used to communicate with the server
     */
    private BufferedWriter serverOutput;

    /**
     * The input stream used to communicate with the server
     */
    private BufferedReader serverInput;
    /**
     * The socket used to communicate with the server
     */
    private Socket socket;

    /**
     * The message handler used to send and receive messages from the server
     */
    private MessageHandler messageHandler;

    /**
     * Constructor of the client
     * @param address the address of the server
     * @param port the port of the server
     */
    public Client(InetAddress address, int port) {
        initConnection(address, port);
        tryLobby();
        join();
        waitReady();
        controlSnake();
    }

    /**
     * Initialize the connection with the server
     * @param address the address of the server
     * @param port the port of the server
     */
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

    /**
     * try if lobby is open or not full
     */
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

    /**
     * Join the lobby
     */
    private void join() {
        terminal.print(Intro.logo);
        while (inputHandler.getKey() != KEY.ENTER) {
            if (inputHandler.getKey() == KEY.QUIT) {
                quit();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        inputHandler.pauseHandler();

        while (true) {
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
            if (message.equals("DONE")) {
                break;
            }
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

        inputHandler.restoreHandler();

        try {
            serverOutput.write(command);
            serverOutput.flush();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Wait for the game to start
     */
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
                if (inputHandler.getKey() == KEY.QUIT) {
                    command = Message.setCommand(Message.QUIT);
                    quit();
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

    /**
     * Control the snake
     */
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
            quit();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Close the connection with the server
     */
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

    /**
     * Quit the game
     */
    private void quit() {
        command = Message.setCommand(Message.QUIT);
        messageHandler.send(command);
        response= Message.getResponse(serverInput);
        message = Message.getMessage(response);
        data = Message.getData(response);
        if (!message.equals("ENDD")) {
            terminal.print("Error :" + data);
            exit(1);
        }
        terminal.clear();
        terminal.print(data + "\n" + "Press enter to exit\n");
        while (inputHandler.getKey() != KEY.ENTER) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        closeServer();
        exit(0);
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
