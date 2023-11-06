package ch.heigvd;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.util.concurrent.locks.ReentrantLock;

import ch.heigvd.KEY;


import com.googlecode.lanterna.input.KeyStroke;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;


public class Client {

    private static final int READ_FREQUENCY = 25; // Milliseconds
    private static Terminal terminal;
    private static Screen screen = null;
    private static TextGraphics text;
    private static byte buffer [] = new byte[1024];

    private ReentrantLock mutex = new ReentrantLock() ;

    public static void main(String[] args) throws IOException, InterruptedException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        text = screen.newTextGraphics();
        text.putString(0, 0, "Type a key !");
        screen.refresh();
        Thread keyHandlerThread = new Thread(Client::keyHandler);
        Thread recieverThread = new Thread(Client::recieve);


    }

    private boolean setStream(byte[] stream) {
        if (stream.length > buffer.length) {
            return false;
        }
        mutex.lock();
        buffer = stream;
        mutex.unlock();
        return true;
    }
    private static void recieve() {

    }

    private static void keyHandler() {
        KeyStroke key = null;
        KeyStroke lastKey = null;
        while (true) {

            KeyStroke input = null;
            try {
                input = terminal.readInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (key != input) {
               key = input;
               sendKey(key);
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void print(String s) {
        try {
            terminal.clearScreen();
            text.putString(0, 0, s);
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static KEY parseKeyStroke(KeyStroke key) {
        return switch (key.getKeyType()) {
            case ArrowUp -> KEY.UP;
            case ArrowDown -> KEY.DOWN;
            case ArrowLeft -> KEY.LEFT;
            case ArrowRight -> KEY.RIGHT;
            case Enter -> KEY.ENTER;
            case Character -> switch (key.getCharacter()) {
                case 'q' -> KEY.QUIT;
                case 'r' -> KEY.READY;
                default -> KEY.NONE;
            };
            default -> KEY.NONE;
        };
    }

    private static void sendKey(KeyStroke keyStroke){

        KEY key = parseKeyStroke(keyStroke);
        String s = "DIR"+key.ordinal();
        send(s);
    }

    private static void send(String s) {

    }
}