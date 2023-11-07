package ch.heigvd;

import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

public class InputHandler {

    private InputProvider inputProvider;
    private KeyStroke key = null;
    boolean stopRequest;

    /**
     * Constructor and runner for the InputHandler
     * @param inputProvider the input provider
     * @param readFrequency the frequency at which the input is read in milliseconds
     */
    public InputHandler(InputProvider inputProvider, int readFrequency) {

        while (true && !stopRequest) {

            KeyStroke input = null;
            try {
                input = inputProvider.readInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (key != input) {
                key = input;
            }
            try {
                Thread.sleep(readFrequency);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopRequest() {
        stopRequest = true;
    }


}
