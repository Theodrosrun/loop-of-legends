package ch.heigvd;

import com.googlecode.lanterna.input.KeyStroke;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import static java.lang.System.*;

public class Client {
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
     * Constructor of the client
     *
     * @param address the address of the server
     * @param port    the port of the server
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
     *
     * @param address the address of the server
     * @param port    the port of the server
     */
    private void initConnection(InetAddress address, int port) {
        try {
            socket = new Socket(address, port);
            serverOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            Thread exitTh = new Thread(new Exit(socket, serverOutput, serverInput));
            Runtime.getRuntime().addShutdownHook(exitTh);

            command = Message.setCommand(Message.INIT);
            serverOutput.write(command);
            serverOutput.flush();

            while (!message.equals("DONE")) {
                response = Message.getResponse(serverInput);
                message = Message.getMessage(response);
                data = Message.getData(response);
                messageHandling(message, data);
            }
        } catch (IOException e) {
            exit(1);
        }
    }

    /**
     * try if lobby is open or not full
     */
    private void tryLobby() {
        try {
            serverOutput.write(Message.setCommand(Message.LOBB));
            serverOutput.flush();
            response = Message.getResponse(serverInput);
            message = Message.getMessage(response);
            data = Message.getData(response);
            messageHandling(message, data);
        } catch (IOException e) {
            terminal.clear();
            terminal.print("Client exception: " + e);
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

        try {
            while (true) {
                String UserName = terminal.userInput();

                if (UserName == null) {
                    serverOutput.write(Message.setCommand(Message.QUIT));
                    serverOutput.flush();
                    exit(1);
                }

                if (socket.getInputStream().available() > 0) {
                    response = Message.getResponse(serverInput);
                    message = Message.getMessage(response);
                    data = Message.getData(response);
                    messageHandling(message, data);
                }

                command = Message.setCommand(Message.JOIN, UserName);
                serverOutput.write(command);
                serverOutput.flush();
                response = Message.getResponse(serverInput);
                message = Message.getMessage(response);
                data = Message.getData(response);
                messageHandling(message, data);

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
        } catch (IOException e) {
            terminal.clear();
            terminal.print("Client exception: " + e);
        }

        inputHandler.restoreHandler();

        try {
            serverOutput.write(command);
            serverOutput.flush();
        } catch (IOException e) {
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
                messageHandling(message, data);
                terminal.print(data);
            }

        } catch (IOException e) {
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
                messageHandling(message, data);
                terminal.print(data);
            }
            quit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Quit the game
     */
    private void quit() {
        command = Message.setCommand(Message.QUIT);
        try {
            serverOutput.write(command);
            serverOutput.flush();
            response = Message.getResponse(serverInput);
        } catch (IOException e) {
            terminal.clear();
            terminal.print("Client exception: " + e);
        }

        message = Message.getMessage(response);
        data = Message.getData(response);
        if (!message.equals("QUIT")) {
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
        exit(0);
    }

    private void messageHandling(String message, String data) {
        switch (message) {
            case "EROR":
                terminal.clear();
                terminal.print("Error :" + data + "\n" + "Press enter to exit\n");
                requestEnter();
                exit(0);
                break;
            case "QUIT":
                terminal.clear();
                terminal.print("Server left \n" + data + "\n" + "Press enter to exit\n");
                requestEnter();
                exit(0);
            default:
                break;
        }

    }

    private void requestEnter() {
        while (inputHandler.getKey() != KEY.ENTER) {
            inputHandler.restoreHandler();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Close the connection with the server
     */


    public static void main(String[] args) {
        int port;
        InetAddress address = null;

        if (args.length != 2) {
            System.err.println("usage: client <address> <port>");
            return;
        }

        try {
            address = InetAddress.getByName((args[0]));
        } catch (UnknownHostException ex) {
            return;
        }

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            return;
        }

        Client client = new Client(address, port);
    }
}
