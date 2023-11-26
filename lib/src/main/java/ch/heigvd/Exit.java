package ch.heigvd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import static java.lang.System.exit;

/**
 * This class is used to close the connection properly
 */
public class Exit implements Runnable {

    /**
     * The socket used to communicate with the server
     */
    private Socket socket;
    /**
     * The output streams used to communicate with the server
     */
    private BufferedWriter serverOutput;

    /**
     * The input stream used to communicate with the server
     */
    private BufferedReader serverInput;

    /**
     * The constructor of the class
     * @param socket the socket used to communicate with the server/client
     * @param serverOutput the output stream used to communicate with the server/client
     * @param serverInput the input stream used to communicate with the server/client
     */
    public Exit(Socket socket, BufferedWriter serverOutput, BufferedReader serverInput) {
        this.serverOutput = serverOutput;
        this.serverInput = serverInput;
        this.socket = socket;
    }

    /**
     * The run method of the thread
     */
    @Override
    public void run() {
        if (socket != null) {
            closeServer();
        }
    }

    /**
     * This method is used to close the connection properly
     */
    private void closeServer() {
        try {

            serverOutput.write(Message.setCommand(Message.QUIT));
            serverOutput.flush();
            serverOutput.close();
            serverInput.close();
            socket.close();
        } catch (IOException e) {
            exit(1);
        }
    }
}
