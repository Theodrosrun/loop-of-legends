package ch.heigvd;
import ch.heigvd.controller.*;

import ch.heigvd.controller.keyManager.KEY;
import ch.heigvd.controller.keyManager.KeyHandler;
import ch.heigvd.entity.snake.Snake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int boardWidth = 50;
        int boardHeight = 20;

        KeyHandler keyHandler = new KeyHandler();

        Position p1 = new Position(0,boardHeight/2, DIRECTION.RIGHT, ' ');
        Snake s1 = new Snake(p1, (short) 5);

        Position p2 = new Position(boardWidth/2,0, DIRECTION.DOWN, ' ');
        Snake s2 = new Snake(p2, (short) 5);

        Board board = new Board(boardWidth, boardHeight, (short)2, (short)20, (short)200);
        board.setSnakes(s1);
        board.deploySnakes();

        Printer.Print(board);
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
            if (s1.isAlive() == false){
                break;
            }
            board.deploySnakes();
            board.deployFood();
            Printer.Print(board);
            System.out.println(s1.getScore());
            Thread.sleep(100);
        }

        System.out.println("Exiting...");
        System.exit(0);
    }
}