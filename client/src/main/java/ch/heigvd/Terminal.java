package ch.heigvd;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.TerminalResizeListener;


import java.io.IOException;

public class Terminal implements InputProvider {
    private static com.googlecode.lanterna.terminal.Terminal terminal;
    private static Screen screen = null;
    private static TextGraphics text;

    public Terminal() {
        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            terminal.addResizeListener(new TerminalResizeListener() {
                @Override
                public void onResized(com.googlecode.lanterna.terminal.Terminal terminal, TerminalSize terminalSize) {
                    screen.doResizeIfNecessary();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        text = screen.newTextGraphics();
    }

    public void helloTerminal() {
        print("Hello Terminal!");
    }

    public void print(String s) {
        try {
            screen.setCursorPosition(null);
            String[] lines = s.split("\n");
            int terminalHeight = terminal.getTerminalSize().getRows();
            int terminalWidth = terminal.getTerminalSize().getColumns();

            while (lines.length + 2 > terminalHeight || lines[0].length() + 2 > terminalWidth) {
                text.putString(0,0,"Terminal too small, please resize\n");
                text.putString(0,1,"Current size: " + terminalWidth + "x" + terminalHeight + "\n");
                text.putString(0,2,"Required size: " + lines[0].length() + "x" + lines.length + "\n");
                terminalHeight = terminal.getTerminalSize().getRows();
                terminalWidth = terminal.getTerminalSize().getColumns();
                screen.refresh();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            for (int i = 0; i < lines.length; i++) {
                text.putString(0, i, lines[i]);
            }
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
