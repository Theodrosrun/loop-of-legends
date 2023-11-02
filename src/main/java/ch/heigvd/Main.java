package ch.heigvd;
import ch.heigvd.Printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Printer.Print(new Board(10, 10, (short)1));
        Logger logger = LoggerFactory.getLogger(Main.class);

        logger.debug("Hello World!");
        while (true){
            Thread.sleep(500);
        }
    }
}