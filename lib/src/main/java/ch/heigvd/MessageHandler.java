package ch.heigvd;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageHandler {

    private BufferedWriter serverOutput;

    public MessageHandler(BufferedWriter serverOutput){
        this.serverOutput = serverOutput;
    }
    public void send(String command){
        try {
            serverOutput.write(command);
            serverOutput.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
