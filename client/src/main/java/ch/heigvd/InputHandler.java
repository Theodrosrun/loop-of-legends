package ch.heigvd;

import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class InputHandler {

    private InputProvider inputProvider;
    private KeyStroke key = null;
    private final Semaphore wait = new Semaphore(0);

    private boolean pause = false;

    private final int READ_FREQUENCY;
    private final InputProvider INPUT_PROVIDER;
    boolean stopRequest = false;

    /**
     * Constructor and runner for the InputHandler
     *
     * @param inputProvider the input provider
     * @param readFrequency the frequency at which the input is read in milliseconds
     */
    public InputHandler(InputProvider inputProvider, int readFrequency) {
        READ_FREQUENCY = readFrequency;
        INPUT_PROVIDER = inputProvider;
        Thread thread = new Thread(this::run);
        thread.start();
    }

    private void run() {
        while (!stopRequest) {

            if (pause) {
                try {
                    wait.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            KeyStroke input = null;
            try {
                input = INPUT_PROVIDER.readInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (key != input) {
                key = input;
            }
            try {
                Thread.sleep(READ_FREQUENCY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public KeyStroke getKeyStroke() {
        return key;
    }

    public KEY getKey() {
        return KEY.parseKeyStroke(key);
    }

    public void resetKey() {
        key = null;
    }

    public void pauseHandler() {
        pause = true;
    }

    public void restoreHandler() {
        pause = false;
        wait.release();
    }

    public void stopRequest() {
        stopRequest = true;
    }


    public static boolean isDirection(KeyStroke key) {
        switch (key.getKeyType()) {
            case ArrowUp:
            case ArrowDown:
            case ArrowLeft:
            case ArrowRight:
                return true;
            default:
                return false;

        }
    }
}
