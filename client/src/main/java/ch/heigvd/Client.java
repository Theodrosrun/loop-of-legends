package ch.heigvd;

import com.googlecode.lanterna.input.KeyStroke;

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
                message = readUntilEOT(serverInput);
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

            inputHandler.pauseHandler();


            //TODO recup le username mettre en pause mon InputHandler puis faire une methode readline avec un read input
//            KeyStroke keykey = terminal.();
            String name = terminal.userInput();
//            terminal.clear();
//            terminal.print("Name");
//            StringBuilder sb = new StringBuilder();
//            while (true) {
//                char c = terminal.readInput().getCharacter();
//                if (c == '\n') break;
//                sb.append(c);
//                terminal.clearLine();
//                terminal.print(sb.toString());
//
//            }
            inputHandler.restoreHandler();


            serverOutput.write(Message.JOIN.toString() + " " + name+ "\n");
            serverOutput.flush();


            while (!message.equals("STRT")) {
                if (inputHandler.getKey() == KEY.READY) {
                    serverOutput.write(Message.RADY.toString() + "\n");
                    serverOutput.flush();
                    inputHandler.resetKey();
                }
                message = readUntilEOT(serverInput);
                //TODO créer getData dans la lib protocol
                terminal.print(message.substring(5, message.length() - 1));
            }


            KEY lastKey = null;

            while (inputHandler.getKey() != KEY.QUIT) {
                KeyStroke key = inputHandler.getKeyStroke();
                if (InputHandler.isDirection(key)) {
                    serverOutput.write(Message.DIRE.toString() +KEY.parseKeyStroke(key).toString() + "\n");
                    serverOutput.flush();
                    inputHandler.resetKey();
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }


    }

    //TODO verifier si il existe pas une methode dans la lib pour faire ca
    public static String readUntilEOT(BufferedReader reader) throws IOException {
        StringBuilder data = new StringBuilder();
        int currentChar;

        while ((currentChar = reader.read()) != -1) {
            if (currentChar == 4) {
                // Le caractère 4 correspond à EOT (End Of Transmission)
                break;  // Sortir de la boucle une fois que EOT est trouvé
            }
            data.append((char) currentChar);
        }

        return data.toString();
    }

}
