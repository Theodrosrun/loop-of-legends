package ch.heigvd;
import ch.heigvd.controller.*;

import ch.heigvd.controller.keyManager.KEY;
import ch.heigvd.controller.keyManager.KeyHandler;
import ch.heigvd.entity.snake.Snake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int boardSize = 20;

        KeyHandler keyHandler = new KeyHandler();

        Position p1 = new Position(-3,boardSize/2, DIRECTION.RIGHT, ' ');
        Snake s1 = new Snake(p1, (short) 5);

        Position p2 = new Position(boardSize/2,0, DIRECTION.DOWN, ' ');
        Snake s2 = new Snake(p2, (short) 5);

        Board board = new Board(boardSize, boardSize, (short)2);
        board.setSnakes(s1, s2);
        board.deploySnakes();

        Printer.Print(board.getBoard());
        Logger logger = LoggerFactory.getLogger(Main.class);

        logger.debug("Hello World!");

        DIRECTION dir = s1.getDirection();

        while (true){
            KEY key = keyHandler.getKey();
            if (key == KEY.QUIT){
                break;
            }
            if (key != KEY.NONE){
                dir = KEY.keyToDirection(key);
            }

            s1.setNextDirection(dir);
            s1.step();
            board.deploySnakes();
            Printer.Print(board.getBoard());
            Thread.sleep(500);
        }

        System.out.println("Exiting...");
        System.exit(0);
    }
}