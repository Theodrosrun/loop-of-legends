package ch.heigvd;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;


import java.io.IOException;

public class Terminal implements InputProvider {
    private static com.googlecode.lanterna.terminal.Terminal terminal;
    private static Screen screen = null;
    private static TextGraphics text;

    public Terminal() {
        try{
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        text = screen.newTextGraphics();
    }

    public void helloTerminal() {
        print("Hello Terminal!");
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

    @Override
    public KeyStroke pollInput() throws IOException {
        return null;
    }

    @Override
    public KeyStroke readInput() throws IOException {
        return terminal.readInput();
    }
}
