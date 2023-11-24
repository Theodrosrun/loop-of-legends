package ch.heigvd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import static java.lang.System.exit;

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

    public Exit(Socket socket, BufferedWriter serverOutput, BufferedReader serverInput) {
        this.serverOutput = serverOutput;
        this.serverInput = serverInput;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket != null) {
            closeServer();
        }
    }

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
